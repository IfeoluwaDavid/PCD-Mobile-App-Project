package ifeoluwa.partscribber;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class PartsCribberLogin extends AppCompatActivity
{
    EditText login_username_value, login_password_value;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_login);

        //if the user is already logged in we will directly start the profile activity
        if (PartsCribberUserSession.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, PartCribberAdminMenu.class));
            return;
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
            BackgroundTasks backgroundTasks = new BackgroundTasks(this);
            backgroundTasks.execute(method, username, password);
        }
    }

    public void openRegister(View view)
    {
        startActivity(new Intent(this, PartsCribberRegister.class));
    }
}
