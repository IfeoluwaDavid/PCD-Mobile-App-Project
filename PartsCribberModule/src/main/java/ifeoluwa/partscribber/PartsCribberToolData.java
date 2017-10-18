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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashSet;

public class PartsCribberToolData extends AppCompatActivity
{
    Intent intent;
    String selectedItem;
    String jsonstring;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ActionBar actionBar;
    Item itemData;

    EditText et_itemSerialNo, et_qtyAvailable, et_qtyRented, et_qtyTotal, et_category;
    TextView tv_itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_tooldata);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        intent = getIntent();
        selectedItem = intent.getStringExtra("selectedItem");

        new ToolDataBackgroundTasks(this).execute();
    }

    class ToolDataBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public ToolDataBackgroundTasks(Context ctx)
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
            json_url = "http://partscribdatabase.tech/androidconnect/fetchSelectedItemData.php";
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

                String data = URLEncoder.encode("item_name", "UTF-8") + "=" + URLEncoder.encode(selectedItem, "UTF-8");
                //Log.d("Debug", " CastedItemID = "  + selectedItem);

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
                JSONObject JO = jsonArray.getJSONObject(0);

                String itemID, itemName, serialNo, qtyAvailable, qtyRented, qtyTotal, itemCategory;

                itemID = JO.getString("item_id");
                itemName = JO.getString("item_name");
                serialNo = JO.getString("serial_no");
                qtyAvailable = JO.getString("available_qty");
                qtyRented = JO.getString("rented_qty");
                qtyTotal = JO.getString("total_qty");
                itemCategory = JO.getString("category");

                /*int CastedItemID = Integer.parseInt(itemID);
                int CastedQtyAvailable = Integer.parseInt(qtyAvailable);
                int CastedQtyRented = Integer.parseInt(qtyRented);
                int CastedQtyTotal = Integer.parseInt(qtyTotal);

                //itemData = new Item(CastedItemID,itemName,serialNo,CastedQtyAvailable,CastedQtyRented,CastedQtyTotal,itemCategory);*/

                tv_itemName = (TextView) findViewById(R.id.item_name_header);
                et_itemSerialNo = (EditText) findViewById(R.id.serial_no);
                et_qtyAvailable = (EditText) findViewById(R.id.qty_available);
                et_qtyRented = (EditText) findViewById(R.id.qty_rented);
                et_qtyTotal = (EditText) findViewById(R.id.qty_total);
                et_category = (EditText) findViewById(R.id.item_category);

                Log.d("Debug", " CastedItemID = "  + itemID);
                Log.d("Debug", " itemName = "  + itemName);
                Log.d("Debug", " serialNo = "  + serialNo);
                Log.d("Debug", " CastedQtyAvailable = "  + qtyAvailable);
                Log.d("Debug", " CastedQtyRented = "  + qtyRented);
                Log.d("Debug", " CastedQtyTotal = "  + qtyTotal);
                Log.d("Debug", " itemCategory = "  + itemCategory);

                tv_itemName.setText(itemName);
                et_itemSerialNo.setText("Serial Number: " + serialNo);
                et_qtyAvailable.setText("Available Quantity: " + qtyAvailable);
                et_qtyRented.setText("Rented Quantity: " + qtyRented);
                et_qtyTotal.setText("Total Quantity: " + qtyTotal);
                et_category.setText("Item Category: " + itemCategory);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
