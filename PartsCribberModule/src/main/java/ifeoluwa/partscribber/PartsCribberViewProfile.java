package ifeoluwa.partscribber;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PartsCribberViewProfile extends AppCompatActivity
{
    EditText my_username, my_firstname, my_lastname, my_email, my_usertype;
    String username, first_name, last_name, email;
    ActionBar actionBar;
    boolean clearance = false;
    boolean editable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewprofile);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"+getString(R.string.edit_profile_header)+"</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        my_username = (EditText) findViewById(R.id.edit_profile_username);
        my_firstname = (EditText) findViewById(R.id.edit_profile_firstname);
        my_lastname = (EditText) findViewById(R.id.edit_profile_lastname);
        my_email = (EditText) findViewById(R.id.edit_profile_email);
        my_usertype = (EditText) findViewById(R.id.user_status);

        User user = UserSession.getInstance(this).getUser();

        my_username.setText(user.getUsername());
        my_firstname.setText(user.getFirstname());
        my_lastname.setText(user.getLastname());
        my_email.setText(user.getEmail());
        my_usertype.setText(user.getUsertype());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.home:
                User user = UserSession.getInstance(this).getUser();
                if (user.getUsertype().equals("Admin"))
                {
                    Intent adminhome = new Intent(this, PartsCribberAdminMenu.class);
                    adminhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    redirect(adminhome);
                    break;
                }
                else if(user.getUsertype().equals("Student"))
                {
                    Intent studenthome = new Intent(this, PartsCribberStudentMenu.class);
                    studenthome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    redirect(studenthome);
                    break;
                }
                else
                {
                    //Do not respond.
                    break;
                }

            case R.id.profile:
                //Do not respond here.
                break;

            case R.id.password:
                Intent passwordActivity = new Intent(this, PartsCribberChangePassword.class);
                redirect(passwordActivity);
                break;

            case R.id.log_out:
                finish();
                UserSession.getInstance(getApplicationContext()).logout();
                Intent login = new Intent(this, PartsCribberLogin.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void editProfileInfo(View view)
    {
        if (editable == true)
        {
            Toast.makeText(getBaseContext(), "Edit Mode is already Activated",Toast.LENGTH_LONG).show();
        }
        else
        {
            User user = UserSession.getInstance(this).getUser();
            if (user.getUsertype().equals("Admin"))
            {
                my_username.setCursorVisible(true);
                my_username.setFocusableInTouchMode(true);
                my_username.setInputType(InputType.TYPE_CLASS_TEXT);
                my_username.requestFocus(); //to trigger the soft input
                my_username.setTypeface(null, Typeface.ITALIC);
            }
            my_firstname.setCursorVisible(true);
            my_firstname.setFocusableInTouchMode(true);
            my_firstname.setInputType(InputType.TYPE_CLASS_TEXT);
            my_firstname.requestFocus(); //to trigger the soft input
            my_firstname.setTypeface(null, Typeface.ITALIC);

            my_lastname.setCursorVisible(true);
            my_lastname.setFocusableInTouchMode(true);
            my_lastname.setInputType(InputType.TYPE_CLASS_TEXT);
            my_lastname.requestFocus(); //to trigger the soft input
            my_lastname.setTypeface(null, Typeface.ITALIC);

            my_email.setCursorVisible(true);
            my_email.setFocusableInTouchMode(true);
            my_email.setInputType(InputType.TYPE_CLASS_TEXT);
            my_email.requestFocus(); //to trigger the soft input
            my_email.setTypeface(null, Typeface.ITALIC);

            editable = true;
            Toast.makeText(getBaseContext(), "Edit Mode Activated",Toast.LENGTH_SHORT).show();
        }
    }

    public void saveProfileInfo(View view)
    {
        if (editable == false)
        {
            Toast.makeText(getBaseContext(), "No changes made.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            clearance = true;
            User user = UserSession.getInstance(this).getUser();
            String userid = String.valueOf(user.getUserID());
            username = my_username.getText().toString();
            first_name = my_firstname.getText().toString();
            last_name = my_lastname.getText().toString();
            email = my_email.getText().toString();

            if (username.equals("") && first_name.equals("") && last_name.equals("") && email.equals(""))
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Empty Update Request");
                builder.setMessage("All fields are required.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                if(user.getUsertype().equals("Admin"))
                {
                    if(username.equals("") || !adminUsernameValidation(username))
                    {
                        clearance = false;
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle("Something went wrong");
                        builder.setMessage("Your Admin ID is INVALID.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                else
                {
                    if (username.equals("") || !studentUserNameValidation(username))
                    {
                        clearance = false;
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle("Something went wrong");
                        builder.setCancelable(false);
                        builder.setMessage("Your Student ID is INVALID.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

                if(!clearance) //if clearance is false
                {
                    //just be looking
                }
                else //if clearance = true
                {
                    if (!namesValidation(first_name))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle("Something went wrong");
                        builder.setMessage("First name should be more than 2 characters with letters only");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                        if (!namesValidation(last_name))
                        {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                            builder.setTitle("Something went wrong");
                            builder.setMessage("Last name should be more than 2 characters with letters only");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                            android.support.v7.app.AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else
                        {
                            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                            if (!email.matches(emailPattern))
                            {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                                builder.setTitle("Something went wrong");
                                builder.setMessage("Email Address is Invalid");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else
                            {
                                String method = "update_user";
                                new UserInfoBackgroundTasks(this).execute(method,userid,username,first_name,last_name,email);
                                //finish();
                            }
                        }
                    }
                }
            }
        }
    }

    public void logout(View view)
    {
        finish();
        UserSession.getInstance(getApplicationContext()).logout();
        Intent login = new Intent(this, PartsCribberLogin.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
    }

    class UserInfoBackgroundTasks extends AsyncTask<String, Void, String>
    {
        Context ctx;
        android.app.AlertDialog.Builder builder;
        private Activity activity;
        private android.app.AlertDialog loginDialog;

        public UserInfoBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new android.app.AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Please wait...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String method = params[0];
            if (method.equals("update_user"))
            {
                String user_id = params[1];
                String username = params[2];
                String first_name = params[3];
                String last_name = params[4];
                String email = params[5];
                try
                {
                    String add_url_info = "http://partscribdatabase.tech/androidconnect/updateuser.php";
                    URL url = new URL(add_url_info);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    String data =
                            URLEncoder.encode("user_id", "UTF-8")+"="+
                            URLEncoder.encode(user_id, "UTF-8")+"&"+
                            URLEncoder.encode("username", "UTF-8")+"="+
                            URLEncoder.encode(username, "UTF-8")+"&"+
                            URLEncoder.encode("first_name", "UTF-8") + "=" +
                            URLEncoder.encode(first_name, "UTF-8") + "&" +
                            URLEncoder.encode("last_name", "UTF-8") + "=" +
                            URLEncoder.encode(last_name, "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" +
                            URLEncoder.encode(email, "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while((line = bufferedReader.readLine())!= null)
                    {
                        stringBuilder.append(line+"\n");
                    }
                    bufferedReader.close();
                    is.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result)
        {
            loginDialog.dismiss();
            if(TextUtils.isEmpty(result))
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                builder.setMessage("Connection Error.");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                String code = "";
                String message = "";
                String user_id = "";
                String username = "";
                String firstname = "";
                String lastname = "";
                String email = "";
                String usertype = "";
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject JO = jsonArray.getJSONObject(0);

                    // Server always responds with this values
                    message = JO.getString("message");
                    code = JO.getString("code");
                    try
                    {
                        user_id = JO.getString("userid");
                        username = JO.getString("username");
                        firstname = JO.getString("firstname");
                        lastname = JO.getString("lastname");
                        email = JO.getString("email");
                        usertype = JO.getString("usertype");
                    }
                    catch (Exception e)
                    {
                        // Server did not respond  with these values
                    }
                    if (code.equals("update_true"))
                    {
                        editable = false;
                        my_username.setCursorVisible(false);
                        my_username.setFocusableInTouchMode(false);
                        my_username.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_username.requestFocus(); //to trigger the soft input
                        my_username.setTypeface(null, Typeface.BOLD);

                        my_firstname.setCursorVisible(false);
                        my_firstname.setFocusableInTouchMode(false);
                        my_firstname.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_firstname.requestFocus(); //to trigger the soft input
                        my_firstname.setTypeface(null, Typeface.BOLD);

                        my_lastname.setCursorVisible(false);
                        my_lastname.setFocusableInTouchMode(false);
                        my_lastname.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_lastname.requestFocus(); //to trigger the soft input
                        my_lastname.setTypeface(null, Typeface.BOLD);

                        my_email.setCursorVisible(false);
                        my_email.setFocusableInTouchMode(false);
                        my_email.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_email.requestFocus(); //to trigger the soft input
                        my_email.setTypeface(null, Typeface.BOLD);

                        int castedUserID = Integer.valueOf(user_id);
                        User user = new User(castedUserID, username, firstname, lastname, email, usertype);
                        UserSession.getInstance(ctx).userLogin(user);

                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Successful Profile Update");
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else if (code.equals("update_false"))
                    {
                        showDialog("Update Failed", message);
                        User user = UserSession.getInstance(ctx).getUser();
                        if (user.getUsertype().equals("Admin"))
                        {
                            my_username.setCursorVisible(true);
                            my_username.setFocusableInTouchMode(true);
                            my_username.setInputType(InputType.TYPE_CLASS_TEXT);
                            my_username.requestFocus(); //to trigger the soft input
                            my_username.setTypeface(null, Typeface.ITALIC);
                        }
                        my_firstname.setCursorVisible(true);
                        my_firstname.setFocusableInTouchMode(true);
                        my_firstname.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_firstname.requestFocus(); //to trigger the soft input
                        my_firstname.setTypeface(null, Typeface.ITALIC);

                        my_lastname.setCursorVisible(true);
                        my_lastname.setFocusableInTouchMode(true);
                        my_lastname.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_lastname.requestFocus(); //to trigger the soft input
                        my_lastname.setTypeface(null, Typeface.ITALIC);

                        my_email.setCursorVisible(true);
                        my_email.setFocusableInTouchMode(true);
                        my_email.setInputType(InputType.TYPE_CLASS_TEXT);
                        my_email.requestFocus(); //to trigger the soft input
                        my_email.setTypeface(null, Typeface.ITALIC);
                    }
                    else
                    {
                        showDialog("Unknown Error Occured", "Unknown Error");
                    }
                }
                catch (JSONException e)
                {
                    showDialog("Unknown Error Occurred", message);
                    e.printStackTrace();
                }
            }
        }

        public void showDialog(String title, String message)
        {
            builder = new android.app.AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setCancelable(false);
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public void onBackPressed()
    {
        if(editable == true)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to exit? Unsaved changes will be lost.");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            finish();
        }
    }

    public void redirect(final Intent newscreen)
    {
        if(editable == true)
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit this page? Unsaved changes will be lost.");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    startActivity(newscreen);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            startActivity(newscreen);
        }
    }

    public boolean studentUserNameValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int num = 0;
        int space = 0;
        int other = 0;

        char firstCharacter = x.charAt(0);

        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if (Character.isDigit(ch[i]))
            {
                num ++ ;
            }
            else if(Character.isSpaceChar(ch[i]))
            {
                space ++ ;
            }
            else
            {
                other ++;
            }
        }
        int sum = letter + num;
        int unwantedCharacters = space + other;
        if(sum == 9 && (firstCharacter == 'N' || firstCharacter == 'n') && letter == 1 && num == 8 && unwantedCharacters < 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean adminUsernameValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int space = 0;
        int num = 0;
        int other = 0;
        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if(Character.isDigit(ch[i]))
            {
                num ++ ;
            }
            else if(Character.isSpaceChar(ch[i]))
            {
                space ++ ;
            }
            else
            {
                other ++;
            }
        }
        int sum = letter + num;
        int unwantedCharacters = space + other;

        if(sum < 6 || unwantedCharacters > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean namesValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int space = 0;
        int num = 0;
        int other = 0;
        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if(Character.isDigit(ch[i]))
            {
                num ++ ;
            }
            else if(Character.isSpaceChar(ch[i]))
            {
                space ++ ;
            }
            else
            {
                other ++;
            }
        }
        int sum = letter;
        int unwantedCharacters = num + other + space;

        if(sum < 2 || unwantedCharacters > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
