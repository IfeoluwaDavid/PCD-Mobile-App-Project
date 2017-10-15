package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class PartsCribberViewProfile extends AppCompatActivity
{
    EditText my_username, my_firstname, my_lastname, my_email, my_usertype;
    String username, first_name, last_name, email;
    String previousUsername, previousFirstname, previousLastname, previousEmail;
    ActionBar actionBar;
    ArrayList<String> newUserData = new ArrayList<String>();
    boolean editable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_viewprofile);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        my_username = (EditText) findViewById(R.id.edit_profile_username);
        my_firstname = (EditText) findViewById(R.id.edit_profile_firstname);
        my_lastname = (EditText) findViewById(R.id.edit_profile_lastname);
        my_email = (EditText) findViewById(R.id.edit_profile_email);
        my_usertype = (EditText) findViewById(R.id.user_status);
        //editsaveButton = (Button) findViewById(R.id.edit_save_button);

        User user = UserSession.getInstance(this).getUser();

        my_username.setText(user.getUsername());
        my_firstname.setText(user.getFirstname());
        my_lastname.setText(user.getLastname());
        my_email.setText(user.getEmail());
        my_usertype.setText(user.getUsertype());
    }

    public void editProfileInfo(View view)
    {
        if (editable == true)
        {
            Toast.makeText(getBaseContext(), "Edit Mode is already Activated",Toast.LENGTH_LONG).show();
        }
        else
        {
            User user = UserSession.getInstance(this).getUser();
            if (user.getUsertype().equals("Admin"))
            {
                my_username.setCursorVisible(true);
                my_username.setFocusableInTouchMode(true);
                my_username.setInputType(InputType.TYPE_CLASS_TEXT);
                my_username.requestFocus(); //to trigger the soft input
                my_username.setTypeface(null, Typeface.ITALIC);
            }
            my_firstname.setCursorVisible(true);
            my_firstname.setFocusableInTouchMode(true);
            my_firstname.setInputType(InputType.TYPE_CLASS_TEXT);
            my_firstname.requestFocus(); //to trigger the soft input
            my_firstname.setTypeface(null, Typeface.ITALIC);

            my_lastname.setCursorVisible(true);
            my_lastname.setFocusableInTouchMode(true);
            my_lastname.setInputType(InputType.TYPE_CLASS_TEXT);
            my_lastname.requestFocus(); //to trigger the soft input
            my_lastname.setTypeface(null, Typeface.ITALIC);

            my_email.setCursorVisible(true);
            my_email.setFocusableInTouchMode(true);
            my_email.setInputType(InputType.TYPE_CLASS_TEXT);
            my_email.requestFocus(); //to trigger the soft input
            my_email.setTypeface(null, Typeface.ITALIC);

            previousUsername = my_username.getText().toString();
            previousFirstname = my_firstname.getText().toString();
            previousLastname = my_lastname.getText().toString();
            previousEmail = my_email.getText().toString();

            editable = true;
            Toast.makeText(getBaseContext(), "Edit Mode Activated",Toast.LENGTH_LONG).show();
        }
    }

    public void saveProfileInfo(View view)
    {
        if(editable == false)
        {
            Toast.makeText(getBaseContext(), "No Changes Made Yet",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(previousUsername == my_username.getText().toString() &&
               previousFirstname == my_firstname.getText().toString() &&
               previousLastname == my_lastname.getText().toString() &&
               previousEmail == my_email.getText().toString())
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Hmmm. Wait?");
                builder.setMessage("No Changes Made.");
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
                my_username.setCursorVisible(false);
                my_username.setFocusableInTouchMode(false);
                my_username.setInputType(InputType.TYPE_CLASS_TEXT);
                my_username.requestFocus(); //to trigger the soft input
                my_username.setTypeface(null, Typeface.BOLD);

                my_firstname.setCursorVisible(false);
                my_firstname.setFocusableInTouchMode(false);
                my_firstname.setInputType(InputType.TYPE_CLASS_TEXT);
                my_firstname.requestFocus(); //to trigger the soft input
                my_firstname.setTypeface(null, Typeface.BOLD);

                my_lastname.setCursorVisible(false);
                my_lastname.setFocusableInTouchMode(false);
                my_lastname.setInputType(InputType.TYPE_CLASS_TEXT);
                my_lastname.requestFocus(); //to trigger the soft input
                my_lastname.setTypeface(null, Typeface.BOLD);

                my_email.setCursorVisible(false);
                my_email.setFocusableInTouchMode(false);
                my_email.setInputType(InputType.TYPE_CLASS_TEXT);
                my_email.requestFocus(); //to trigger the soft input
                my_email.setTypeface(null, Typeface.BOLD);

                User user = UserSession.getInstance(this).getUser();

                String userid = String.valueOf(user.getUserID());
                username = my_username.getText().toString();
                first_name = my_firstname.getText().toString();
                last_name = my_lastname.getText().toString();
                email = my_email.getText().toString();

                editable = false;

                String method = "update_user";
                BackgroundTasks backgroundTasks = new BackgroundTasks(this);
                backgroundTasks.execute(method,userid,username,first_name,last_name,email);
            }
        }
    }

    public ArrayList<String> getNewUserData()
    {
        return newUserData;
    }

    public void logout(View view)
    {
        finish();
        UserSession.getInstance(getApplicationContext()).logout();
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to exit? Unsaved changes will be lost.");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
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
