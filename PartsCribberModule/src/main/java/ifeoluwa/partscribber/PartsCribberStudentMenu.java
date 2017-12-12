package ifeoluwa.partscribber;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
