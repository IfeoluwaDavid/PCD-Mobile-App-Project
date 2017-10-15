package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartsCribberAdminMenu extends AppCompatActivity
{
    HashMap<String, List<String>> Admin_Menu;
    List<String> Admin_List;
    ExpandableListView exp_list;
    AdminMenuAdapter adapter;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_adminmenu);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        Admin_Menu = AdminMenuDataProvider.getInfo();
        Admin_List = new ArrayList<String>(Admin_Menu.keySet());

        //Initialization of Array Class Object
        adapter = new AdminMenuAdapter(this, Admin_Menu, Admin_List);
        exp_list.setAdapter(adapter);

        exp_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id)
            {
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("Add New Student"))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberRegisterStudent.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("Add New Admin"))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberRegisterAdmin.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("Rent Equipment"))
                {
                    Toast.makeText(getBaseContext(), Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition),Toast.LENGTH_LONG).show();
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("Return Equipment"))
                {
                    Toast.makeText(getBaseContext(), Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition),Toast.LENGTH_LONG).show();
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("View/Edit My Profile"))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberViewProfile.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("Change Password"))
                {
                    Toast.makeText(getBaseContext(), Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition),Toast.LENGTH_LONG).show();
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("View All Equipment"))
                {
                    Toast.makeText(getBaseContext(), Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition),Toast.LENGTH_LONG).show();
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals("View Rented Equipment"))
                {
                    Toast.makeText(getBaseContext(), Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition),Toast.LENGTH_LONG).show();
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
