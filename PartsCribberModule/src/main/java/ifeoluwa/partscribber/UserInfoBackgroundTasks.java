package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

/**
 * Created by Ifeoluwa David on 2017-10-03.
 */

public class UserInfoBackgroundTasks extends AsyncTask<String, Void, String>
{
    Context ctx;
    AlertDialog.Builder builder;
    private Activity activity;
    private AlertDialog loginDialog;

    public UserInfoBackgroundTasks(Context ctx)
    {
        this.ctx = ctx;
        activity = (Activity)ctx;
    }

    @Override
    protected void onPreExecute()
    {
        builder = new AlertDialog.Builder(activity);
        View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
        ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Connecting to Server");
        loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        String method = params[0];
        if (method.equals("register_student"))
        {
            String username = params[1];
            String password = params[2];
            String first_name = params[3];
            String last_name = params[4];
            String email = params[5];
            try
            {
                String add_url_info = "http://partscribdatabase.tech/androidconnect/registerstudent.php";
                URL url = new URL(add_url_info);
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
        }
        else if (method.equals("register_admin"))
        {
            String username = params[1];
            String password = params[2];
            String first_name = params[3];
            String last_name = params[4];
            String email = params[5];
            try
            {
                String add_url_info = "http://partscribdatabase.tech/androidconnect/registeradmin.php";
                URL url = new URL(add_url_info);
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
        }
        else if (method.equals("update_user"))
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
        else if (method.equals("login"))
        {
            String username = params[1];
            String password = params[2];
            try
            {
                //SENDING DATA TO SERVER STARTS HERE
                String login_url = "http://partscribdatabase.tech/androidconnect/login.php";
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+
                        URLEncoder.encode(username, "UTF-8")+"&"+
                        URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                //RESPONSE FROM SERVER STARTS HERE
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
        else if (method.equals("changepassword"))
        {
            String myuserid = params[1];
            String oldpassword = params[2];
            String newpassword = params[3];
            try
            {
                //SENDING DATA TO SERVER STARTS HERE
                String login_url = "http://partscribdatabase.tech/androidconnect/changepassword.php";
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                String data =
                        URLEncoder.encode("user_id", "UTF-8")+"="+
                        URLEncoder.encode(myuserid, "UTF-8")+"&"+
                        URLEncoder.encode("password", "UTF-8")+"="+
                        URLEncoder.encode(oldpassword, "UTF-8")+"&"+
                        URLEncoder.encode("newpassword", "UTF-8") + "=" +
                        URLEncoder.encode(newpassword, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                //RESPONSE FROM SERVER STARTS HERE
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

            if (code.equals("reg_true"))
            {
                showDialog("Registration Success",message,code);
            }
            else if (code.equals("reg_false"))
            {
                showDialog("Registration Failed",message,code);
            }
            else if (code.equals("login_true"))
            {
                if (usertype.equals("Admin"))
                {
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity, PartsCribberAdminMenu.class);

                    int castedUserID = Integer.valueOf(user_id);
                    User user = new User(castedUserID, username, firstname, lastname, email, usertype);
                    UserSession.getInstance(ctx).userLogin(user);

                    activity.startActivity(intent);
                    activity.finish();
                }
                else
                {
                    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity, PartsCribberStudentMenu.class);

                    int castedUserID = Integer.valueOf(user_id);
                    User user = new User(castedUserID, username, firstname, lastname, email, usertype);

                    UserSession.getInstance(ctx).userLogin(user);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
            else if (code.equals("login_false"))
            {
                showDialog("Login Failed", message, code);
            }
            else if (code.equals("update_true"))
            {
                int castedUserID = Integer.valueOf(user_id);
                User user = new User(castedUserID, username, firstname, lastname, email, usertype);
                UserSession.getInstance(ctx).userLogin(user);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                builder.setTitle("Successful Profile Update");
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
            else if (code.equals("update_false"))
            {
                showDialog("Update Failed", message, code);
            }
            else if (code.equals("changepassword_true"))
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                builder.setTitle("Changed Password Successfully");
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
            else if (code.equals("changepassword_false"))
            {
                showDialog("Sorry! Something went wrong.", message, code);
            }
            else
            {
                showDialog("Unknown Error Occured", "Unknown Error", "Unknown Error");
            }
        }
        catch (JSONException e)
        {
            showDialog("Unknown Error Occurred", message, code);
            e.printStackTrace();
        }
    }

    public void showDialog(String title, String message, String code)
    {
        builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        Log.d("Login BG Task", "Server message - " + message + " | Server code - " + code + " | Server title" + title);
        if (code.equals("reg_true"))
        {
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    activity.finish();
                }
            });
            builder.show();
        }
        else if (code.equals("reg_false"))
        {
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
        else if (code.equals("login_false"))
        {
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
        else
        {
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
}