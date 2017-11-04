package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
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

public class PartsCribberStudentInfo extends AppCompatActivity implements PCStudentProfileFragment.PCStudentProfileFragmentInterface
{
    ActionBar actionBar;
    Intent intent;
    String selectedID;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;

    String jsonstring, fname, lname, possessionqty, email, status;
    JSONObject jsonObject;
    JSONArray jsonArray;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_studentinfo);

        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        intent = getIntent();
        selectedID = intent.getStringExtra("selectedID");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        viewPagerAdapter.addFragments(new PCStudentProfileFragment(), "Profile");
        viewPagerAdapter.addFragments(new PCStudentPossessionFragment(), "Possessions");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        new UserInfoBackgroundTasks(this).execute();
    }

    class UserInfoBackgroundTasks extends AsyncTask<String, Void, String>
    {
        Context ctx;
        String json_url, JSON_STRING;
        android.app.AlertDialog.Builder builder;
        private Activity activity;
        private android.app.AlertDialog loginDialog;

        public UserInfoBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity) ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new android.app.AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView) dialogView.findViewById(R.id.tv_progress_dialog)).setText("Connecting to Server");
            loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
            json_url = "http://partscribdatabase.tech/androidconnect/fetchStudentProfile.php";
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

                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(selectedID, "UTF-8");

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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result)
        {
            loginDialog.dismiss();
            jsonstring = result;

            try
            {
                jsonObject = new JSONObject(jsonstring);
                jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);

                fname = JO.getString("firstname");
                lname = JO.getString("lastname");
                possessionqty = JO.getString("possessionQty");
                email = JO.getString("email");
                status = JO.getString("status");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String passStudentID()
    {
        return selectedID;
    }

    @Override
    public String getStudentFirstName()
    {
        return fname;
    }

    @Override
    public String getStudentLastName()
    {
        return lname;
    }

    @Override
    public String getStudentPossessionqty()
    {
        return  possessionqty;
    }

    @Override
    public String getStudentEmail()
    {
        return email;
    }

    @Override
    public String getStudentStatus()
    {
        return status;
    }
}
