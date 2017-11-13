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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    String validatedID, validItemQuantity, studentIDvalue, globalSelectedItemAvailableQuantity, itemID;
    AlertDialog.Builder mBuilder;
    AlertDialog dialog;
    View mView;
    EditText et_itemSerialNo, et_qtyAvailable, et_qtyRented, et_qtyTotal, et_category, studentID, itemQuantity;
    Button viewCart, updateItem, addToCart, viewHolders, deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewtooldata);

        intent = getIntent();
        selectedItem = intent.getStringExtra("selectedItem");
        alreadyhas = intent.getStringExtra("alreadyhas");
        validatedID = intent.getStringExtra("theID");

        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"+selectedItem+"</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User user = UserSession.getInstance(this).getUser();
        addToCart = (Button) findViewById(R.id.add_item_to_cart);
        viewCart = (Button) findViewById(R.id.view_cart_button);
        viewHolders = (Button) findViewById(R.id.view_holders);
        deleteItem = (Button) findViewById(R.id.delete_item);
        updateItem = (Button) findViewById(R.id.update_this_item_data);

        if(!TextUtils.isEmpty(validatedID))
        {
            addToCart.setText(R.string.add_item_to_cart);
            viewCart.setVisibility(View.VISIBLE);

            if(user.getUsertype().equals("Admin"))
            {
                updateItem = (Button) findViewById(R.id.update_this_item_data);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                params.addRule(RelativeLayout.BELOW, R.id.delete_item);
                params.setMargins(15, 15, 15, 0);
                updateItem.setLayoutParams(params);
            }
        }
        else
        {
            addToCart.setText(R.string.rent_item);
        }

        if(user.getUsertype().equals("Student"))
        {
            validatedID = user.getUsername();
            addToCart.setText(R.string.add_item_to_cart);
            viewCart.setVisibility(View.VISIBLE);
            updateItem.setVisibility(View.INVISIBLE);
            viewHolders.setVisibility(View.INVISIBLE);
            deleteItem.setVisibility(View.INVISIBLE);
        }

        new ToolDataBackgroundTasks(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            case R.id.home:
                User user = UserSession.getInstance(this).getUser();
                if (user.getUsertype().equals("Admin"))
                {
                    Intent adminhome = new Intent(this, PartsCribberAdminMenu.class);
                    adminhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(adminhome);
                    break;
                }
                else if(user.getUsertype().equals("Student"))
                {
                    Intent studenthome = new Intent(this, PartsCribberStudentMenu.class);
                    studenthome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(studenthome);
                    break;
                }
                else
                {
                    //Do not respond.
                    break;
                }

            case R.id.profile:
                Intent profileActivity = new Intent(this, PartsCribberViewProfile.class);
                startActivity(profileActivity);
                break;

            case R.id.password:
                Intent passwordActivity = new Intent(this, PartsCribberChangePassword.class);
                startActivity(passwordActivity);
                break;

            case R.id.log_out:
                finish();
                UserSession.getInstance(getApplicationContext()).logout();
                Intent login = new Intent(this, PartsCribberLogin.class);
                startActivity(login);

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void deleteItem(View view)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                new DeleteItemBackgroundTasks(PartsCribberViewToolData.this).execute();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void viewHolders(View view)
    {
        Intent intent = new Intent(PartsCribberViewToolData.this, PartsCribberActualHolders.class);
        intent.putExtra("selectedItem", String.valueOf(selectedItem));
        startActivity(intent);
    }

    public void updateItem (View view)
    {
        Intent intent = new Intent(PartsCribberViewToolData.this, PartsCribberUpdateItem.class);
        intent.putExtra("itemID", itemID);
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
            QuantityPrompt();
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
            new FindStudentBackgroundTasks(this).execute();
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
                Toast.makeText(getBaseContext(), "Quantity must be 1 or more, else, delete item.", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart()
    {
        super.onRestart();
        new ToolDatabyIDBackgroundTasks(this).execute();
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
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Please wait...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
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
                        new ToolDataBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        finish();
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

                    String serialNo, qtyAvailable, qtyRented, qtyTotal, itemCategory;

                    itemID = JO.getString("item_id");
                    serialNo = JO.getString("serial_no");
                    qtyAvailable = JO.getString("available_qty");
                    qtyRented = JO.getString("rented_qty");
                    qtyTotal = JO.getString("total_qty");
                    itemCategory = JO.getString("category");

                    et_itemSerialNo = (EditText) findViewById(R.id.serial_no);
                    et_qtyAvailable = (EditText) findViewById(R.id.qty_available);
                    et_qtyRented = (EditText) findViewById(R.id.qty_rented);
                    et_qtyTotal = (EditText) findViewById(R.id.qty_total);
                    et_category = (EditText) findViewById(R.id.item_category);

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
    }

    class FindStudentBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        android.app.AlertDialog.Builder builder;
        private Activity activity;
        private android.app.AlertDialog loginDialog;

        public FindStudentBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new android.app.AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Searching...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
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
                        new FindStudentBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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

                                User user = UserSession.getInstance(ctx).getUser();
                                if(user.getUsertype().equals("Admin"))
                                {
                                    updateItem = (Button) findViewById(R.id.update_this_item_data);
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                                    params.addRule(RelativeLayout.BELOW, R.id.delete_item);
                                    params.setMargins(15, 15, 15, 0);
                                    updateItem.setLayoutParams(params);
                                }
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
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Adding to Cart...");
            loginDialog = builder.setView(dialogView).setCancelable(false).show();
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
                        new alterCartBackgroundTask(ctx).execute();
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
                        builder.setTitle("Done! Added to Cart");
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
    }

    class ToolDatabyIDBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public ToolDatabyIDBackgroundTasks(Context ctx)
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
            json_url = "http://partscribdatabase.tech/androidconnect/fetchSelectedIDData.php";
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

                String data = URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(itemID, "UTF-8");

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
                        new ToolDataBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        finish();
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

                    String itemName, serialNo, qtyAvailable, qtyRented, qtyTotal, itemCategory;

                    itemName = JO.getString("item_name");
                    serialNo = JO.getString("serial_no");
                    qtyAvailable = JO.getString("available_qty");
                    qtyRented = JO.getString("rented_qty");
                    qtyTotal = JO.getString("total_qty");
                    itemCategory = JO.getString("category");

                    et_itemSerialNo = (EditText) findViewById(R.id.serial_no);
                    et_qtyAvailable = (EditText) findViewById(R.id.qty_available);
                    et_qtyRented = (EditText) findViewById(R.id.qty_rented);
                    et_qtyTotal = (EditText) findViewById(R.id.qty_total);
                    et_category = (EditText) findViewById(R.id.item_category);

                    et_itemSerialNo.setText("Serial Number: " + serialNo);
                    et_qtyAvailable.setText("Available Quantity: " + qtyAvailable);
                    et_qtyRented.setText("Rented Quantity: " + qtyRented);
                    et_qtyTotal.setText("Total Quantity: " + qtyTotal);
                    et_category.setText("Item Category: " + itemCategory);

                    globalSelectedItemAvailableQuantity = qtyAvailable;

                    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"+itemName+"</font>"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    class DeleteItemBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public DeleteItemBackgroundTasks(Context ctx)
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
            json_url = "http://partscribdatabase.tech/androidconnect/deleteItem.php";
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

                String data = URLEncoder.encode("item_id", "UTF-8") + "=" + URLEncoder.encode(itemID, "UTF-8");

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
                        new ToolDataBackgroundTasks(ctx).execute();
                    }
                });
                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        finish();
                    }
                });
                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                String code = "";
                String message = "";
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                    JSONObject JO = jsonArray.getJSONObject(0);

                    // Server always responds with this values
                    message = JO.getString("message");
                    code = JO.getString("code");

                    if (code.equals("update_true"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Successful Update");
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
                    else if(code.equals("update_false"))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                        builder.setTitle("Something went wrong!");
                        builder.setMessage(message);
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
                }
                catch (JSONException e)
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
                    builder.setTitle("Unknown error occured");
                    builder.setMessage("Unknown Error Occurred, Please Try Again.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                    e.printStackTrace();
                }
            }
        }
    }
}
