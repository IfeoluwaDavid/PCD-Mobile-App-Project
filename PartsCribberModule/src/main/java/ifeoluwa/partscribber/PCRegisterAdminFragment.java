package ifeoluwa.partscribber;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class PCRegisterAdminFragment extends Fragment
{

    View finder;
    EditText admin_username, admin_firstname, admin_lastname;
    EditText admin_email, admin_password, admin_confirmpassword;
    String username, first_name, last_name, email, password, confirmpassword;

    public PCRegisterAdminFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        finder = inflater.inflate(R.layout.pcregisteradmin_fragment, container, false);

        admin_username = (EditText) finder.findViewById(R.id.newadmin_username);
        admin_password = (EditText) finder.findViewById(R.id.newadmin_password);
        admin_confirmpassword = (EditText) finder.findViewById(R.id.newadmin_confirmpassword);
        admin_firstname = (EditText) finder.findViewById(R.id.newadmin_firstname);
        admin_lastname = (EditText) finder.findViewById(R.id.newadmin_lastname);
        admin_email = (EditText) finder.findViewById(R.id.newadmin_email);

        final Button adminRegister = (Button) finder.findViewById(R.id.register_newadmin_button);
        adminRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                // Just like you were doing
                username = admin_username.getText().toString();
                password = admin_password.getText().toString();
                confirmpassword = admin_confirmpassword.getText().toString();
                first_name = admin_firstname.getText().toString();
                last_name = admin_lastname.getText().toString();
                email = admin_email.getText().toString();

                if (username.equals("") && password.equals("") && confirmpassword.equals("") &&
                        first_name.equals("") && last_name.equals("") && email.equals(""))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setTitle("Empty Registration Request");
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
                    if(username.equals("") || !adminUsernameValidation(username))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        builder.setTitle("Something went wrong");
                        builder.setMessage("Your Admin ID is INVALID.");
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
                        if(password.equals("") || !passwordValidation(password))
                        {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builder.setTitle("Something went wrong");
                            builder.setMessage("Your new password should be between 8 to 15 characters long, without spaces.");
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
                            if(!confirmpassword.equals(password))
                            {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                builder.setTitle("Something went wrong");
                                builder.setMessage("Your Passwords don't match");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else
                            {
                                if (!namesValidation(first_name))
                                {
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                            new RegisterAdminBackgroundTasks(getActivity()).execute();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        return finder;
    }

    class RegisterAdminBackgroundTasks extends AsyncTask<String, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public RegisterAdminBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView) dialogView.findViewById(R.id.tv_progress_dialog)).setText("Please wait...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
            json_url = "http://partscribdatabase.tech/androidconnect/registeradmin.php";
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+
                                URLEncoder.encode(username, "UTF-8")+"&"+
                                URLEncoder.encode("password", "UTF-8")+"="+
                                URLEncoder.encode(password, "UTF-8") + "&" +
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
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        new RegisterAdminBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject JO = jsonArray.getJSONObject(0);

                    String message = JO.getString("message");
                    String code = JO.getString("code");

                    if (code.equals("reg_true"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Successful Registration");
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                admin_username.setText("");
                                admin_password.setText("");
                                admin_confirmpassword.setText("");
                                admin_email.setText("");
                                admin_firstname.setText("");
                                admin_lastname.setText("");
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else if (code.equals("reg_false"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Something went wrong!");
                        builder.setMessage(message);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                new RegisterAdminBackgroundTasks(ctx).execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Unidentified Error Occurred");
                        builder.setMessage("Please try again.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                new RegisterAdminBackgroundTasks(ctx).execute();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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
                catch (JSONException e)
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Unknown Application Error Occurred");
                    builder.setMessage("Please try again.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            new RegisterAdminBackgroundTasks(ctx).execute();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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

    public boolean passwordValidation(String x)
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
        int sum = letter + num + other;

        if(sum < 8 || sum > 15 || space > 0)
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
