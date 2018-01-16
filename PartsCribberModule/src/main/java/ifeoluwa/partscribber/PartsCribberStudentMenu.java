package ifeoluwa.partscribber;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PartsCribberStudentMenu extends AppCompatActivity
implements PCViewAllToolsFragment.PCViewAllToolsFragmentInterface, PCSelectCategoryFragment.PCSelectCategoryFragmentInterface
{
    TabLayout tabLayout;
    ViewPager viewPager;
    Intent intent;
    android.app.AlertDialog dialog;
    android.app.AlertDialog.Builder builder;
    ViewPagerAdapter viewPagerAdapter;
    ActionBar actionBar;
    TextView username;

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_studentmenu);
        
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>PartsCribber</font>"));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragments(new PCViewAllToolsFragment(), "All Equipment");
        viewPagerAdapter.addFragments(new PCSelectCategoryFragment(), "All Categories");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                Intent intent;

                switch (item.getItemId())
                {
                    case R.id.home:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.viewStudentCart:
                        intent = new Intent(PartsCribberStudentMenu.this, PartsCribberStudentCart.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.viewStudentPossessions:
                        intent = new Intent(PartsCribberStudentMenu.this, PartsCribberReturnEquipment.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.profilesettings:
                        intent = new Intent(PartsCribberStudentMenu.this, PartsCribberViewProfile.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.changepassword:
                        intent = new Intent(PartsCribberStudentMenu.this, PartsCribberChangePassword.class);
                        startActivity(intent);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.logout:
                        finishAffinity();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        UserSession.getInstance(getApplicationContext()).logout();
                        Intent login = new Intent(PartsCribberStudentMenu.this, PartsCribberLogin.class);
                        startActivity(login);
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void myCart(View view)
    {
        intent = new Intent(this, PartsCribberStudentCart.class);
        User user = UserSession.getInstance(this).getUser();
        intent.putExtra("theID", String.valueOf(user.getUsername()));
        startActivity(intent);
    }

    public void myPossessions(View view)
    {
        intent = new Intent(this, PartsCribberReturnEquipment.class);
        User user = UserSession.getInstance(this).getUser();
        intent.putExtra("theID", String.valueOf(user.getUsername()));
        startActivity(intent);
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
                finishAffinity();
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        setIconInMenu(menu, R.id.about, R.string.about, R.mipmap.about);
        setIconInMenu(menu, R.id.help, R.string.help, R.mipmap.help);
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
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            case R.id.about:
                break;

            case R.id.help:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
