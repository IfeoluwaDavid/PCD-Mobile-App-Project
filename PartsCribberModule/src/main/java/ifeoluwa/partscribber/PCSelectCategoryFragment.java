package ifeoluwa.partscribber;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PCSelectCategoryFragment extends Fragment
{
    View finder;
    String jsonstring;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listView;
    ActionBar actionBar;
    Intent intent;
    SearchView editText;
    private PCSelectCategoryFragment.PCSelectCategoryFragmentInterface mListener;

    public PCSelectCategoryFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        finder = inflater.inflate(R.layout.pcselectcategory_fragment, container, false);
        return finder;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new CategoryInfoBackgroundTasks(getActivity()).execute();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    class CategoryInfoBackgroundTasks extends AsyncTask<Void, Void, String>
    {
        String json_url;
        String JSON_STRING;
        Context ctx;
        AlertDialog.Builder builder;
        private Activity activity;
        private AlertDialog loginDialog;
        ArrayAdapter<String> adapter;
        HashSet<String> hashsetofCategoryObjects = new HashSet<String>();
        List<String> list;

        public CategoryInfoBackgroundTasks(Context ctx)
        {
            this.ctx = ctx;
            activity = (Activity)ctx;
        }

        @Override
        protected void onPreExecute()
        {
            builder = new AlertDialog.Builder(activity);
            View dialogView = LayoutInflater.from(this.ctx).inflate(R.layout.progress_dialog, null);
            ((TextView)dialogView.findViewById(R.id.tv_progress_dialog)).setText("Please Wait...");
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
                        new CategoryInfoBackgroundTasks(getActivity()).execute();
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
                    int count = 0;

                    String itemCategory;

                    while(count < jsonArray.length())
                    {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        itemCategory = JO.getString("category");
                        hashsetofCategoryObjects.add(itemCategory.toUpperCase());
                        count++;
                    }

                    list = new ArrayList<String>(hashsetofCategoryObjects);
                    listView = (ListView) finder.findViewById(R.id.listview);
                    adapter=new ArrayAdapter<String>(ctx,R.layout.viewalltools_rowlayout,R.id.viewalltools_itemnametext,list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            String selectedCategory = (String) parent.getItemAtPosition(position);
                            mListener.viewCategoryData(selectedCategory);
                        }
                    });

                    editText = (SearchView)finder.findViewById(R.id.txtsearch);
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
        }
    }

    @Override
    public void onAttach(Context ctx)
    {
        super.onAttach(ctx);
        try
        {
            mListener = (PCSelectCategoryFragment.PCSelectCategoryFragmentInterface)ctx;
        }
        catch (ClassCastException c)
        {
            throw new ClassCastException(ctx.toString() + " should implememt PCSelectCategoryFragmentInterface");
        }
    }

    interface  PCSelectCategoryFragmentInterface
    {
        void viewCategoryData(String selectedCategory);
    }
}
