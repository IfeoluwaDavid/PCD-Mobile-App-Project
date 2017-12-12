package ifeoluwa.partscribber;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartsCribberAdminMenu extends AppCompatActivity
{
    List<String> Admin_List;
    ExpandableListView exp_list;
    AdminMenuAdapter adapter;
    ActionBar actionBar;

    HashMap <String, List<String>> Admin_Menu = new HashMap <String, List<String>>();
    List<String> users_and_tools = new ArrayList<String>();
    List<String> rentals_and_returns = new ArrayList<String>();
    List<String> profile_settings = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_adminmenu);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>PartsCribber</font>"));

        TextView username = (TextView) findViewById(R.id.welcomeusername);
        User user = UserSession.getInstance(this).getUser();
        if (TextUtils.isEmpty(user.getFirstname()))
        {
            finish();
        }
        else
        {
            username.setText(user.getFirstname().toUpperCase()+" "+user.getLastname().toUpperCase()+" ("+user.getUsertype()+")");
        }

        exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        //Admin_Menu = AdminMenuDataProvider.getInfo();

        rentals_and_returns.add(getString(R.string.rent_equipment));
        rentals_and_returns.add(getString(R.string.return_equipment));

        users_and_tools.add(getString(R.string.add_search_users));
        users_and_tools.add(getString(R.string.add_search_tools));

        profile_settings.add(getString(R.string.view_edit_profile));
        profile_settings.add(getString(R.string.change_my_password));

        Admin_Menu.put(getString(R.string.add_and_search), users_and_tools);
        Admin_Menu.put(getString(R.string.rentals_and_returns), rentals_and_returns);
        Admin_Menu.put(getString(R.string.profile_settings), profile_settings);

        Admin_List = new ArrayList<String>(Admin_Menu.keySet());

        //Initialization of Array Class Object
        adapter = new AdminMenuAdapter(this, Admin_Menu, Admin_List);
        exp_list.setAdapter(adapter);

        exp_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id)
            {
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals(getString(R.string.rent_equipment)))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberRentEquipment.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals(getString(R.string.return_equipment)))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberReturnEquipment.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals(getString(R.string.add_search_users)))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberRegisterUser.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals(getString(R.string.add_search_tools)))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberViewEquipment.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals(getString(R.string.view_edit_profile)))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberViewProfile.class);
                    startActivity(intent);
                }
                if(Admin_Menu.get(Admin_List.get(groupPosition)).get(childPosition).equals(getString(R.string.change_my_password)))
                {
                    Intent intent = new Intent(PartsCribberAdminMenu.this, PartsCribberChangePassword.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        setIconInMenu(menu, R.id.home, R.string.home, R.mipmap.homeicon);
        setIconInMenu(menu, R.id.profile, R.string.profile, R.mipmap.profileicon);
        setIconInMenu(menu, R.id.password, R.string.password, R.mipmap.lockicon);
        setIconInMenu(menu, R.id.log_out, R.string.log_out, R.mipmap.logouticon);
        return super.onCreateOptionsMenu(menu);
    }

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId)
    {
        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("   " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(this, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                //Do not respond.
                break;

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
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /*public class AdminMenuDataProvider
    {
        //String menuone = getString(R.string.add_search_users);

        public HashMap<String, List<String>> getInfo()
        {




            //return Admin_Menu;
        }
    }*/


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
