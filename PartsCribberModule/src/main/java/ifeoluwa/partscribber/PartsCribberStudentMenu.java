package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PartsCribberStudentMenu extends AppCompatActivity
{
    ListView listView;
    ListView listView2;
    ArrayAdapter<String> adapter;
    Intent intent;
    ActionBar actionBar;
    TextView username;

    String[] studentmenu = {"View Available Item", "My Current Rentals"};
    String[] studentmenu2 = {"View/Edit Personal Info","Change My Password"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_studentmenu);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        username = (TextView) findViewById(R.id.welcomeusername);
        User user = UserSession.getInstance(this).getUser();
        username.setText(user.getFirstname()+" "+user.getLastname());

        listView = (ListView) findViewById(R.id.studentmenulistview);
        adapter = new ArrayAdapter<String>(this, R.layout.studentmenu_parentlayout,studentmenu);
        listView.setAdapter(adapter);

        listView2 = (ListView) findViewById(R.id.studentmenulistview2);
        adapter = new ArrayAdapter<String>(this, R.layout.studentmenu_parentlayout,studentmenu2);
        listView2.setAdapter(adapter);

        //NO LISTENER FOR LISTVIEW 1 YET.

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (parent.getItemAtPosition(position).equals("View/Edit Personal Info"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberViewProfile.class);
                    startActivity(intent);
                }
                if(parent.getItemAtPosition(position).equals("Change My Password"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberChangePassword.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to exit the application?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                int id = android.os.Process.myPid();
                android.os.Process.killProcess(id);
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
