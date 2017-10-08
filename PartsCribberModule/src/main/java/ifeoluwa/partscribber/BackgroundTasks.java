package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
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

public class BackgroundTasks extends AsyncTask<String, Void, String>
{
    Context ctx;
    AlertDialog.Builder builder;
    private Activity activity;
    public BackgroundTasks(Context ctx)
    {
        this.ctx = ctx;
        activity = (Activity)ctx;
    }

    @Override
    protected void onPreExecute()
    {
        builder = new AlertDialog.Builder(activity);
        // activity = (Activity)ctx;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String method = params[0];
        if (method.equals("register"))
        {
            String username = params[1];
            String password = params[2];
            String first_name = params[3];
            String last_name = params[4];
            String email = params[5];
            try
            {
                String add_url_info = "http://partscribdatabase.tech/androidconnect/register.php";
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
        try
        {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            JSONObject JO = jsonArray.getJSONObject(0);

            String code = JO.getString("code");
            String message = JO.getString("message");
            String username = JO.getString("username");
            String firstname = JO.getString("firstname");
            String lastname = JO.getString("lastname");
            String email = JO.getString("email");

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
                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, PartCribberAdminMenu.class);

                User user = new User (username, firstname, lastname, email);

                PartsCribberUserSession.getInstance(ctx).userLogin(user);
                activity.startActivity(intent);
                activity.finish();
            }
            else if (code.equals("login_false"))
            {
                showDialog("Login Failed", message, code);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void showDialog(String title, String message, String code)
    {
        builder.setTitle(title);
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
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
