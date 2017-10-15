package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;

public class PartsCribberChangePassword extends AppCompatActivity
{
    EditText currentPassword, confirmCurrentPassword, newPassword, confirmNewPassword;
    String oldpassword, confirmoldpassword, newpassword, confirmnewpassword;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_changepassword);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        currentPassword = (EditText) findViewById(R.id.current_password);
        confirmCurrentPassword = (EditText) findViewById(R.id.confirm_current_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
    }

    public void savePasswordChanges (View view)
    {
        oldpassword = currentPassword.getText().toString();
        confirmoldpassword = confirmCurrentPassword.getText().toString();
        newpassword = newPassword.getText().toString();
        confirmnewpassword = confirmNewPassword.getText().toString();

        if (oldpassword.equals("") && confirmoldpassword.equals("") && newpassword.equals("") && confirmnewpassword.equals(""))
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Empty Change Password Request");
            builder.setMessage("All fields are required.");
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
            if(oldpassword.equals("") || confirmoldpassword.equals(""))
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Hold on?");
                builder.setMessage("Both current password fields are required. Can't be empty.");
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
                if (!oldpassword.equals(confirmoldpassword))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Hmmm. Wait?");
                    builder.setMessage("Current password fields are not matching.");
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
                    if (newpassword.equals("") || !passwordValidation(newpassword))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle("Just to be safe");
                        builder.setMessage("Your new password should be between 8 to 15 characters long, without spaces.");
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
                        if (!confirmnewpassword.equals(newpassword))
                        {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                            builder.setTitle("Something went wrong");
                            builder.setMessage("Your Passwords don't match");
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
                            String method = "changepassword";
                            UserInfoBackgroundTasks userInfoBackgroundTasks = new UserInfoBackgroundTasks(this);
                            User user = UserSession.getInstance(this).getUser();
                            String myuserid = String.valueOf(user.getUserID());
                            userInfoBackgroundTasks.execute(method,myuserid,oldpassword,newpassword);
                        }
                    }
                }
            }
        }
    }

    public boolean passwordValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int space = 0;
        int num = 0;
        int other = 0;
        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if(Character.isDigit(ch[i]))
            {
                num ++ ;
            }
            else if(Character.isSpaceChar(ch[i]))
            {
                space ++ ;
            }
            else
            {
                other ++;
            }
        }
        int sum = letter + num + other;

        if(sum < 8 || sum > 15 || space > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wish to leave your password unchanged?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finish();
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
