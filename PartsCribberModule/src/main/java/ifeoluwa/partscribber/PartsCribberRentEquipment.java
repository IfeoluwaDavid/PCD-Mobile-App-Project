package ifeoluwa.partscribber;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;

public class PartsCribberRentEquipment extends AppCompatActivity
{
    String jsonstring;
    View mView;
    Intent intent;
    AlertDialog.Builder mBuilder;
    AlertDialog dialog;
    String studentIDvalue;
    ActionBar actionBar;
    EditText studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_rentequipment);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.studentid_alertdialog, null);
        studentID = (EditText) mView.findViewById(R.id.enter_student_id);
        Button validateBtn = (Button) mView.findViewById(R.id.validate_button);
        Button cancelBtn = (Button) mView.findViewById(R.id.cancel_button);

        mBuilder.setCancelable(false);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    public void validateBtn(View view)
    {
        if(!studentID.getText().toString().isEmpty())
        {
            studentIDvalue = studentID.getText().toString();
            Toast.makeText(getBaseContext(), studentIDvalue, Toast.LENGTH_SHORT).show();
            new ItemInfoBackgroundTasks(this).execute();
            dialog.dismiss();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Failed to find student", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelBtn(View view)
    {
        dialog.dismiss();
        finish();
    }

    class ItemInfoBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        android.app.AlertDialog.Builder builder;
        private Activity activity;
        private android.app.AlertDialog loginDialog;

        public ItemInfoBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new android.app.AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Searching");
            loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
            json_url = "http://partscribdatabase.tech/androidconnect/findstudent.php";
        }

        @Override
        protected String doInBackground(Void... voids)
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

                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(studentIDvalue, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(JSON_STRING + "\n");
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
            jsonstring = result;

            String code = "";
            String message = "";
            //String firstname = "";
            //String lastname = "";

            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);

                // Server always responds with this values
                message = JO.getString("message");
                code = JO.getString("code");
                /*try
                {
                    firstname = JO.getString("firstname");
                    lastname = JO.getString("lastname");
                }
                catch (Exception e)
                {
                    //Server did not respond  with these values
                }*/

                if (code.equals("valid"))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Found this student");
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            recreate();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else if (code.equals("invalid"))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Unable to find student");
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            recreate();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Unknown Error Occurred");
                    builder.setMessage("Please Try Again.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            recreate();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
