package ifeoluwa.partscribber;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class PCViewAllUsers extends Fragment
{
    View finder;
    String jsonstring,selectedID;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayAdapter<String> adapter;
    ListView listView;
    SearchView searchView;
    ActionBar actionBar;
    Intent intent;
    int count;
    android.support.v7.app.AlertDialog dialog;

    String fname, lname, possessionqty, email, status;
    android.support.v7.app.AlertDialog.Builder mBuilder, builder;
    View mView;
    TextView studentIDheader;
    EditText studentfullname, studentpossessionqty, studentemail, studentstatus;

    public PCViewAllUsers()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        finder = inflater.inflate(R.layout.pcviewallusers_fragment, container, false);
        new fetchAllStudentsBackgroundTasks(getActivity()).execute();
        return finder;
    }

    class fetchAllStudentsBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;
        List<String> arraylistofStudentObjects = new ArrayList<String>();

        public fetchAllStudentsBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Please wait...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
            json_url = "http://partscribdatabase.tech/androidconnect/fetchAllStudents.php";
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            try
            {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
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
                        new fetchAllStudentsBackgroundTasks(ctx).execute();
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
                jsonstring = result;
                count = 0;

                listView =(ListView) finder.findViewById(R.id.listview);
                searchView = (SearchView) finder.findViewById(R.id.searchView);
                searchView.setIconified(false);
                searchView.clearFocus();

                try
                {
                    jsonObject = new JSONObject(jsonstring);
                    jsonArray = jsonObject.getJSONArray("server_response");

                    String username;

                    while(count < jsonArray.length())
                    {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        username = JO.getString("username");
                        arraylistofStudentObjects.add(username.toUpperCase());
                        count++;
                    }

                    adapter=new ArrayAdapter<String>(ctx, R.layout.viewalltools_rowlayout,R.id.viewalltools_itemnametext, arraylistofStudentObjects);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            selectedID = parent.getItemAtPosition(position).toString();
                            new UserInfoBackgroundTasks(getActivity()).execute();
                        }
                    });

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
                    {
                        @Override
                        public boolean onQueryTextSubmit(String query)
                        {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText)
                        {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });

                    searchView.setOnSearchClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            searchView.onActionViewExpanded();
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
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
            ((TextView) dialogView.findViewById(R.id.tv_progress_dialog)).setText("Please wait...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
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
                        new UserInfoBackgroundTasks(ctx).execute();
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

                    mBuilder = new android.support.v7.app.AlertDialog.Builder(activity);

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    mView = inflater.inflate(R.layout.studentinfo_alertdialog, null);

                    studentIDheader = (TextView) mView.findViewById(R.id.textView3);
                    studentfullname = (EditText) mView.findViewById(R.id.editText);
                    studentpossessionqty = (EditText) mView.findViewById(R.id.editText2);
                    studentemail = (EditText) mView.findViewById(R.id.editText3);
                    studentstatus = (EditText) mView.findViewById(R.id.editText4);

                    studentIDheader.setText(selectedID);
                    studentfullname.setText(getString(R.string.full_name) + ": " + fname + " " + lname);
                    studentpossessionqty.setText(getString(R.string.possession_qty) + ": " + possessionqty);
                    studentemail.setText(getString(R.string.email) + ": " + email);
                    studentstatus.setText(getString(R.string.status) + ": " + status);

                    mBuilder.setCancelable(false);
                    mBuilder.setView(mView);

                    dialog = mBuilder.create();
                    dialog.show();

                    final Button cancelBtn = (Button) mView.findViewById(R.id.button3);
                    cancelBtn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(final View v)
                        {
                            dialog.dismiss();
                        }
                    });

                    final Button rentalInfo = (Button) mView.findViewById(R.id.button2);
                    rentalInfo.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(final View v)
                        {
                            Intent intent = new Intent(getActivity(), PartsCribberReturnEquipment.class);
                            intent.putExtra("theID", selectedID);
                            startActivity(intent);
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
