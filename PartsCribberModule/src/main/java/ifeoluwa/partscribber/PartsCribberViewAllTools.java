package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class PartsCribberViewAllTools extends AppCompatActivity
{
    //Intent intent;
    //String jsonstringFromAdmin;

    String jsonstring;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ItemMenuAdapter itemMenuAdapter;
    ListView listView;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewalltools);

//        intent = getIntent();
//        jsonstringFromAdmin = intent.getStringExtra("resultFromAdmin");

        //display = (TextView) findViewById(R.id.textView);

        listView = (ListView) findViewById(R.id.listview);
        itemMenuAdapter = new ItemMenuAdapter(this, R.layout.viewtools_rowlayout);
        listView.setAdapter(itemMenuAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        new ItemInfoBackgroundTasks(this).execute();
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
            json_url = "http://partscribdatabase.tech/androidconnect/fetchitemdata.php";
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
//            Intent intento = new Intent(PartsCribberAdminMenu.this, PartsCribberViewAllTools.class);
//            intento.putExtra("resultFromAdmin", result);
           // Log.d("Debug", result);
            loginDialog.dismiss();
            jsonstring = result;
           // display.setText(jsonstring);

            Log.d("Debug", jsonstring);
            try
            {
                jsonObject = new JSONObject(jsonstring);
                jsonArray = jsonObject.getJSONArray("server_response");
                //jsonArray  = new JSONObject(jsonstring).getJSONArray("server_response");
                int count = 0;
                Log.d("Debug", " jsonArray is null ? " + (jsonArray == null));

                String itemID, itemName, serialNo, qtyAvailable, qtyRented, qtyTotal, itemCategory;

                while(count < jsonArray.length())
                {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    Log.d("Debug", " jo = "  + JO.toString());

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
