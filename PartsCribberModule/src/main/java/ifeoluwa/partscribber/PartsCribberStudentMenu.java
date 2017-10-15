package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

public class PartsCribberStudentMenu extends AppCompatActivity
{
    ListView listView;
    ArrayAdapter<String> adapter;
    Intent intent;
    ArrayList<String> thelist = new ArrayList<String>();
    ActionBar actionBar;
    TextView username;

    String[] studentmenu = {"View Available Tools", "My Current Rentals","My Rental History","View/Edit Profile Settings"};

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

        intent = new Intent(PartsCribberStudentMenu.this, PartsCribberViewProfile.class);

        listView = (ListView) findViewById(R.id.studentmenulistview);
        adapter = new ArrayAdapter<String>(this, R.layout.studentmenu_parentlayout,studentmenu);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (parent.getItemAtPosition(position).equals("View/Edit Profile Settings"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberViewProfile.class);
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
                /*Intent intent=new Intent(this, PartsCribberLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                int id= android.os.Process.myPid();
                android.os.Process.killProcess(id);
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
