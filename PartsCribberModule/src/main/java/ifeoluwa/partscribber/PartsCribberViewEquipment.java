package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.ArrayList;
import java.util.HashSet;

public class PartsCribberViewEquipment extends AppCompatActivity
implements PCViewAllToolsFragment.PCViewAllToolsFragmentInterface, PCSelectCategoryFragment.PCSelectCategoryFragmentInterface
{
    TabLayout tabLayout;
    ViewPager viewPager;
    View mView;
    AlertDialog dialog;
    AlertDialog.Builder mBuilder, builder;
    ViewPagerAdapter viewPagerAdapter;

    ArrayAdapter<String> adapter;
    AutoCompleteTextView ac_category;
    EditText et_itemname, et_serialno, et_stockquantity;
    ActionBar actionBar;
    String jsonstring;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Intent intent;
    String[] categoryArray;
    String item_name, serial_no, total_qty, category;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewequipments);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragments(new PCViewAllToolsFragment(), "All Equipment");
        viewPagerAdapter.addFragments(new PCSelectCategoryFragment(), "All Categories");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void viewToolData(String selectedItem)
    {
        intent = new Intent(this, PartsCribberViewToolData.class);
        intent.putExtra("selectedItem", String.valueOf(selectedItem));
        startActivity(intent);
    }

    @Override
    public void viewCategoryData(String selectedCategory)
    {
        intent = new Intent(this, PartsCribberSelectTool.class);
        intent.putExtra("selectedCategory", selectedCategory);
        startActivity(intent);
    }

    public void addItem(View view)
    {
        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.addequipment_alertdialog, null);

        mBuilder.setCancelable(false);
        mBuilder.setView(mView);

        dialog = mBuilder.create();
        dialog.show();

        et_itemname = (EditText) mView.findViewById(R.id.item_name_value);
        et_serialno = (EditText) mView.findViewById(R.id.serial_no_value);
        et_stockquantity = (EditText) mView.findViewById(R.id.quantity_value);
        ac_category = (AutoCompleteTextView) mView.findViewById(R.id.category_value);

        new fetchCategoryBackgroundTasks(this).execute();
    }

    public void cancelBtn(View view)
    {
        dialog.dismiss();
    }

    public void enterBtn (View view)
    {
        item_name = et_itemname.getText().toString();
        serial_no = et_serialno.getText().toString();
        total_qty = et_stockquantity.getText().toString();
        category = ac_category.getText().toString();

        if (item_name.equals("") && serial_no.equals("") && total_qty.equals("") && category.equals(""))
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Empty Update Request");
            builder.setMessage("All fields are required.");
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
            if (item_name.equals("") || !lengthValidation(item_name))
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setMessage("Item name is required and should be 3 or more characters.");
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
                if (serial_no.equals("") || !serialNumberValidation(serial_no))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage("Serial number is required and has to be exactly 6 digits.");
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
                    if (total_qty.equals("") || !digitValidation(total_qty))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setMessage("Stock quantity has to be at least 1.");
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
                        if (category.equals("") || !lengthValidation(category))
                        {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                            builder.setMessage("Item Category is required and should be 3 or more characters.");
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
                            String method = "update_inventory";
                            new AddNewItemBackgroundTasks(this).execute(method,item_name,serial_no,total_qty,category);
                        }
                    }
                }
            }
        }
    }

    public boolean lengthValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int space = 0;
        int num = 0;
        int other = 0;
        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if(Character.isDigit(ch[i]))
            {
                num ++ ;
            }
            else if(Character.isSpaceChar(ch[i]))
            {
                space ++ ;
            }
            else
            {
                other ++;
            }
        }
        int sum = letter + num + space;
        int unwantedCharacters = other;

        if(sum < 3 || unwantedCharacters > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean serialNumberValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int space = 0;
        int num = 0;
        int other = 0;
        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if(Character.isDigit(ch[i]))
            {
                num ++ ;
            }
            else if(Character.isSpaceChar(ch[i]))
            {
                space ++ ;
            }
            else
            {
                other ++;
            }
        }
        int sum = num;
        int unwantedCharacters = letter + space + other;

        if(sum < 6 || unwantedCharacters > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
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

    class fetchCategoryBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public fetchCategoryBackgroundTasks(Context ctx)
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
            json_url = "http://partscribdatabase.tech/androidconnect/fetchCategoryData.php";
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
                        new fetchCategoryBackgroundTasks(ctx).execute();
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
                HashSet<String> arraylistofCategoryObjects = new HashSet<String>();

                try
                {
                    jsonObject = new JSONObject(jsonstring);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;

                    String itemCategory;

                    while(count < jsonArray.length())
                    {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        itemCategory = JO.getString("category");
                        arraylistofCategoryObjects.add(itemCategory);
                        count++;
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                categoryArray = new String[arraylistofCategoryObjects.size()];
                int index = 0;
                for (String item : arraylistofCategoryObjects)
                {
                    categoryArray[index] = item;
                    index ++;
                }

                adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,categoryArray);
                ac_category.setAdapter(adapter);
            }
        }
    }

    class AddNewItemBackgroundTasks extends AsyncTask<String, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;

        public AddNewItemBackgroundTasks(Context ctx)
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
            json_url = "http://partscribdatabase.tech/androidconnect/updateinventory.php";
        }

        @Override
        protected String doInBackground(String... params)
        {
            String method = params[0];
            if (method.equals("update_inventory"))
            {
                String item_name = params[1];
                String serial_no = params[2];
                String total_qty = params[3];
                String category = params[4];
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    String data =
                            URLEncoder.encode("item_name", "UTF-8") + "=" +
                                    URLEncoder.encode(item_name, "UTF-8") + "&" +
                                    URLEncoder.encode("serial_no", "UTF-8") + "=" +
                                    URLEncoder.encode(serial_no, "UTF-8") + "&" +
                                    URLEncoder.encode("total_qty", "UTF-8") + "=" +
                                    URLEncoder.encode(total_qty, "UTF-8") + "&" +
                                    URLEncoder.encode("category", "UTF-8") + "=" +
                                    URLEncoder.encode(category, "UTF-8");

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
                        new AddNewItemBackgroundTasks(ctx).execute();
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
                        builder.setTitle("Successfully Added");
                        builder.setMessage(message);
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
