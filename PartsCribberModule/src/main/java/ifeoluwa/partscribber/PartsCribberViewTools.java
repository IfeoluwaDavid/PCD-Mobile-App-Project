package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class PartsCribberViewTools extends AppCompatActivity
{
    String jsonstring;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Intent intent;
    ItemMenuAdapter itemMenuAdapter;
    ListView listView;
    ActionBar actionBar;
    String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewtools);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        intent = getIntent();
        selectedCategory = intent.getStringExtra("selectedCategory");

        new ItemInfoBackgroundTasks(this).execute();

        listView = (ListView) findViewById(R.id.listview);
        itemMenuAdapter = new ItemMenuAdapter(this, R.layout.viewtools_rowlayout);
        listView.setAdapter(itemMenuAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = String.valueOf(parent.getItemAtPosition(position));
                intent = new Intent(PartsCribberViewTools.this, PartsCribberToolData.class);
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            }
        });
    }

    class ItemInfoBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public ItemInfoBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Fetching Item Data");
            loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
            json_url = "http://partscribdatabase.tech/androidconnect/fetchSelectedCategoryData.php";
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

                String data = URLEncoder.encode("category", "UTF-8") + "=" + URLEncoder.encode(selectedCategory, "UTF-8");

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

            try
            {
                jsonObject = new JSONObject(jsonstring);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                //Log.d("Debug", " jsonArray is null ? " + (jsonArray == null));

                String itemID, itemName, serialNo, qtyAvailable, qtyRented, qtyTotal, itemCategory;

                while(count < jsonArray.length())
                {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    //Log.d("Debug", " jo = "  + JO.toString());

                    itemID = JO.getString("item_id");
                    itemName = JO.getString("item_name");
                    serialNo = JO.getString("serial_no");
                    qtyAvailable = JO.getString("available_qty");
                    qtyRented = JO.getString("rented_qty");
                    qtyTotal = JO.getString("total_qty");
                    itemCategory = JO.getString("category");

                    int CastedItemID = Integer.parseInt(itemID);
                    int CastedQtyAvailable = Integer.parseInt(qtyAvailable);
                    int CastedQtyRented = Integer.parseInt(qtyRented);
                    int CastedQtyTotal = Integer.parseInt(qtyTotal);

                    Item item = new Item(CastedItemID,itemName,serialNo,CastedQtyAvailable,CastedQtyRented,CastedQtyTotal,itemCategory);
                    itemMenuAdapter.add(item);
                    count++;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
