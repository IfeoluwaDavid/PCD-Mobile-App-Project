package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartsCribberViewAllTools extends AppCompatActivity
{
    String jsonstring;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ViewAllToolsAdapter viewAllToolsAdapter;
    ArrayAdapter<String> adapter;
    ListView listView;
    ActionBar actionBar;
    Intent intent;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewalltools);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        new fetchAllToolsBackgroundTasks(this).execute();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        recreate();
    }

    class fetchAllToolsBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;
        List<String> arraylistofCategoryObjects = new ArrayList<String>();
        List<String> listItems = new ArrayList<String>();
        String[] items;

        public fetchAllToolsBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Fetching Server Data");
            loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
            json_url = "http://partscribdatabase.tech/androidconnect/fetchAllTools.php";
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
            jsonstring = result;
            count = 0;

            listView =(ListView)findViewById(R.id.listview);
            final SearchView editText = (SearchView)findViewById(R.id.txtsearch);
            editText.setIconified(false);
            editText.clearFocus();
            initList();

            try
            {
                jsonObject = new JSONObject(jsonstring);
                jsonArray = jsonObject.getJSONArray("server_response");

                String itemName;

                while(count < jsonArray.length())
                {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    itemName = JO.getString("item_name");
                    arraylistofCategoryObjects.add(itemName);
                    adapter.add(itemName);
                    count++;
                }

                TextView amount = (TextView) findViewById(R.id.all_available_tools);
                amount.setText("All Equipments ("+count+")");

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedItem = parent.getItemAtPosition(position).toString();
                        intent = new Intent(PartsCribberViewAllTools.this, PartsCribberViewToolData.class);
                        intent.putExtra("selectedItem", String.valueOf(selectedItem));
                        startActivity(intent);
                    }
                });

                editText.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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

                editText.setOnSearchClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        editText.onActionViewExpanded();
                    }
                });
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        public void initList()
        {
            items = new String[arraylistofCategoryObjects.size()];
            for(int i=0; i < arraylistofCategoryObjects.size(); i++)
            {
                items[i] = arraylistofCategoryObjects.get(i);
            }
            listItems=new ArrayList<>(Arrays.asList(items));
            adapter=new ArrayAdapter<String>(ctx, R.layout.viewalltools_rowlayout,R.id.viewalltools_itemnametext, listItems);
            listView.setAdapter(adapter);
        }
    }
}
