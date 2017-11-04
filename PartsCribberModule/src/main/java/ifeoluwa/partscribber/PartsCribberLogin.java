package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PartsCribberLogin extends AppCompatActivity
{
    EditText login_username_value, login_password_value;
    String username, password;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_login);
        actionBar = getSupportActionBar();
        actionBar.hide();

        //if the user is already logged in we will directly start the profile activity
        User user = UserSession.getInstance(this).getUser();

        if(UserSession.getInstance(this).isLoggedIn())
        {
            if (user.getUsertype().equals("Admin"))
            {
                finish();
                startActivity(new Intent(this, PartsCribberAdminMenu.class));
                //return;
            }
            else if(user.getUsertype().equals("Student"))
            {
                finish();
                startActivity(new Intent(this, PartsCribberStudentMenu.class));
                //return;
            }
        }
        login_username_value = (EditText) findViewById(R.id.login_username);
        login_password_value = (EditText) findViewById(R.id.login_password);
    }

    public void afterLogin(View view)
    {
        username = login_username_value.getText().toString();
        password = login_password_value.getText().toString();

        if (username.equals("") && password.equals(""))
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Empty Login Request");
            builder.setMessage("Username and Password are required.");
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
            String method = "login";
            UserInfoBackgroundTasks userInfoBackgroundTasks = new UserInfoBackgroundTasks(this);
            userInfoBackgroundTasks.execute(method, username, password);
        }
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
