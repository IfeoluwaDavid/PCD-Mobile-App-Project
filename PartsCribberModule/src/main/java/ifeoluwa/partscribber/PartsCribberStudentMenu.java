package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartsCribberStudentMenu extends AppCompatActivity
{
    HashMap<String, List<String>> Student_Menu;
    List<String> Student_List;
    ExpandableListView exp_list;
    StudentMenuAdapter adapter;
    ActionBar actionBar;
    TextView username;

    //String[] studentmenu = {"View Available Item", "My Current Rentals"};
    //String[] studentmenu2 = {"View/Edit Personal Info","Change My Password"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_studentmenu);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        username = (TextView) findViewById(R.id.welcomeusername);
        User user = UserSession.getInstance(this).getUser();
        username.setText(user.getFirstname()+" "+user.getLastname()+" ("+user.getUsertype()+")");

        exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        Student_Menu = StudentMenuDataProvider.getInfo();
        Student_List = new ArrayList<String>(Student_Menu.keySet());

        //Initialization of Array Class Object
        adapter = new StudentMenuAdapter(this, Student_Menu, Student_List);
        exp_list.setAdapter(adapter);

        exp_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                return true; // This way the expander cannot be collapsed
            }
        });

        exp_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id)
            {
                if(Student_Menu.get(Student_List.get(groupPosition)).get(childPosition).equals("View/Edit My Profile"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberViewProfile.class);
                    startActivity(intent);
                }
                if(Student_Menu.get(Student_List.get(groupPosition)).get(childPosition).equals("Change My Password"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberChangePassword.class);
                    startActivity(intent);
                }
                if(Student_Menu.get(Student_List.get(groupPosition)).get(childPosition).equals("Add New Equipment"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberAddItem.class);
                    startActivity(intent);
                }
                if(Student_Menu.get(Student_List.get(groupPosition)).get(childPosition).equals("View All Equipment"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberRegisterUser.class);
                    startActivity(intent);
                }
                if(Student_Menu.get(Student_List.get(groupPosition)).get(childPosition).equals("View Equipment by Category"))
                {
                    Intent intent = new Intent(PartsCribberStudentMenu.this, PartsCribberViewEquipment.class);
                    startActivity(intent);
                }
                return false;
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
