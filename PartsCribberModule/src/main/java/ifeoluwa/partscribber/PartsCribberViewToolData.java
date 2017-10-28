package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

public class PartsCribberViewToolData extends AppCompatActivity
{
    Intent intent;
    String selectedItem, jsonstring, alreadyhas;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ActionBar actionBar;
    String validatedID, validItemQuantity, studentIDvalue, globalSelectedItemAvailableQuantity;
    AlertDialog.Builder mBuilder;
    AlertDialog dialog;
    View mView;
    EditText et_itemSerialNo, et_qtyAvailable, et_qtyRented, et_qtyTotal, et_category, studentID, itemQuantity;
    TextView tv_itemName;
    Button viewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewtooldata);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        intent = getIntent();
        selectedItem = intent.getStringExtra("selectedItem");
        alreadyhas = intent.getStringExtra("alreadyhas");
        validatedID = intent.getStringExtra("theID");
        ((Button)findViewById(R.id.add_item_to_cart)).setText(TextUtils.isEmpty(validatedID) ? R.string.rent_item : R.string.add_item_to_cart);

        viewCart = (Button) findViewById(R.id.view_cart_button);
        if(!TextUtils.isEmpty(validatedID))
        {
            viewCart.setVisibility(View.VISIBLE);
        }
        new ToolDataBackgroundTasks(this).execute();
    }

    public void updateItem (View view)
    {
        Intent intent = new Intent(PartsCribberViewToolData.this, PartsCribberUpdateItem.class);
        intent.putExtra("selectedItem", String.valueOf(selectedItem));
        startActivity(intent);
    }

    public void addtoCartANDrentItem (View view)
    {
        if(TextUtils.isEmpty(validatedID))
        {
            IDprompt();//rent item - no id
        }
        else
        {
            new relationshipCheckBackgroundTask(this).execute();
        }
    }

    public void IDprompt()
    {
        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.studentid_alertdialog, null);

        studentID = (EditText) mView.findViewById(R.id.enter_student_id);
        mBuilder.setCancelable(true);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    public void validateBtn (View view)
    {
        if(!studentID.getText().toString().isEmpty())
        {
            studentIDvalue = studentID.getText().toString();
            new ItemInfoBackgroundTasks(this).execute();
            dialog.dismiss();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Student ID is Required", Toast.LENGTH_SHORT).show();
        }
    }

    public void idExitButton (View view)
    {
        dialog.dismiss();
    }

    public void QuantityPrompt()
    {
        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.itemquantity_alertdialog, null);

        itemQuantity = (EditText) mView.findViewById(R.id.enter_quantity);
        mBuilder.setCancelable(false);
        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    public void quantityEnterBtn (View view)
    {
        if(itemQuantity.getText().toString().isEmpty())
        {
            Toast.makeText(getBaseContext(), "Quantity is Required.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!digitValidation(itemQuantity.getText().toString()))
            {
                Toast.makeText(getBaseContext(), "Quantity is expected to be 1 or more", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(Integer.valueOf(itemQuantity.getText().toString()) > 3)
                {
                    dialog.dismiss();
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage("You can only rent up to 3 of the same equipment at once.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            QuantityPrompt();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    if(Integer.valueOf(itemQuantity.getText().toString()) > Integer.valueOf(globalSelectedItemAvailableQuantity))
                    {
                        dialog.dismiss();
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setMessage("Only "+globalSelectedItemAvailableQuantity+" available at the moment.");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                QuantityPrompt();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                        validItemQuantity = itemQuantity.getText().toString();
                        new alterCartBackgroundTask(this).execute();
                        dialog.dismiss();
                    }
                }
            }
        }
    }

    public void quantityCancelBtn (View view)
    {
        dialog.dismiss();
    }

    public void viewCart (View view)
    {
        Intent intent = new Intent(PartsCribberViewToolData.this, PartsCribberStudentCart.class);
        intent.putExtra("theID", String.valueOf(validatedID));
        startActivity(intent);
    }

    public boolean digitValidation(String x)
    {
        int intValueofX = Integer.parseInt(x);
        if(intValueofX < 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /*@Override
    protected void onRestart()
    {
        super.onRestart();

    }*/

    @Override
    protected void onRestart()
    {
        super.onRestart();
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

                String itemName, serialNo, qtyAvailable, qtyRented, qtyTotal, itemCategory;

                itemName = JO.getString("item_name");
                serialNo = JO.getString("serial_no");
                qtyAvailable = JO.getString("available_qty");
                qtyRented = JO.getString("rented_qty");
                qtyTotal = JO.getString("total_qty");
                itemCategory = JO.getString("category");

                tv_itemName = (TextView) findViewById(R.id.item_name_header);
                et_itemSerialNo = (EditText) findViewById(R.id.serial_no);
                et_qtyAvailable = (EditText) findViewById(R.id.qty_available);
                et_qtyRented = (EditText) findViewById(R.id.qty_rented);
                et_qtyTotal = (EditText) findViewById(R.id.qty_total);
                et_category = (EditText) findViewById(R.id.item_category);

                tv_itemName.setText(itemName);
                et_itemSerialNo.setText("Serial Number: " + serialNo);
                et_qtyAvailable.setText("Available Quantity: " + qtyAvailable);
                et_qtyRented.setText("Rented Quantity: " + qtyRented);
                et_qtyTotal.setText("Total Quantity: " + qtyTotal);
                et_category.setText("Item Category: " + itemCategory);

                globalSelectedItemAvailableQuantity = qtyAvailable;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
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
            //jsonstring = result;

            String code = "";
            String message = "";

            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);

                message = JO.getString("message");
                code = JO.getString("code");

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
                            validatedID = studentIDvalue;
                            dialog.dismiss();
                            ((Button)findViewById(R.id.add_item_to_cart)).setText(R.string.add_item_to_cart);
                            viewCart.setVisibility(View.VISIBLE);
                            Toast.makeText(getBaseContext(), "You can now add item to cart", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            IDprompt();
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
                            IDprompt();
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
                            IDprompt();
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

    class alterCartBackgroundTask extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        android.app.AlertDialog.Builder builder;
        private Activity activity;
        private android.app.AlertDialog loginDialog;

        public alterCartBackgroundTask(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new android.app.AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Loading...");
            loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
            json_url = "http://partscribdatabase.tech/androidconnect/updateStudentCart.php";
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

                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+
                        URLEncoder.encode(validatedID, "UTF-8")+"&"+
                        URLEncoder.encode("quantity", "UTF-8")+"="+
                        URLEncoder.encode(validItemQuantity, "UTF-8")+"&"+
                        URLEncoder.encode("selectedItem", "UTF-8") + "=" +
                        URLEncoder.encode(selectedItem, "UTF-8");

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

            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);

                message = JO.getString("message");
                code = JO.getString("code");

                if (code.equals("update_true"))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Successfully Added");
                    builder.setMessage(message);
                    builder.setCancelable(true);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            Intent intent = new Intent(PartsCribberViewToolData.this, PartsCribberStudentCart.class);
                            intent.putExtra("theID", String.valueOf(validatedID));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            //check something here
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else if (code.equals("update_false"))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Something went wrong");
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            QuantityPrompt();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Non-server side problem ");
                    builder.setMessage("Unknown application error occurred, Please Try Again.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            QuantityPrompt();
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

    class relationshipCheckBackgroundTask extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        android.app.AlertDialog.Builder builder;
        private Activity activity;
        private android.app.AlertDialog loginDialog;

        public relationshipCheckBackgroundTask(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new android.app.AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Loading...");
            loginDialog = builder.setView(dialogView).setCancelable(false).setTitle("Please Wait").show();
            json_url = "http://partscribdatabase.tech/androidconnect/relationshipcheck.php";
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

                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+
                        URLEncoder.encode(validatedID, "UTF-8")+"&"+
                        URLEncoder.encode("selectedItem", "UTF-8") + "=" +
                        URLEncoder.encode(selectedItem, "UTF-8");

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

            String localalreadyhas = "";

            try
            {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject JO = jsonArray.getJSONObject(0);

                localalreadyhas = JO.getString("alreadyhas");

                alreadyhas = localalreadyhas;
                if(Integer.valueOf(alreadyhas) >= 6)//add to cart - with id
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setMessage("Student has already reached limit for this item ("+ alreadyhas +" "+ selectedItem +"s).");
                    builder.setCancelable(true);
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
                    QuantityPrompt();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
