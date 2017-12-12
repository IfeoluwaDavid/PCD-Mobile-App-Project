package ifeoluwa.partscribber;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

import android.content.DialogInterface;
import android.content.Intent;
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
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>"+getString(R.string.change_my_password)+"</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentPassword = (EditText) findViewById(R.id.current_password);
        confirmCurrentPassword = (EditText) findViewById(R.id.confirm_current_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
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
                User user = UserSession.getInstance(this).getUser();
                if (user.getUsertype().equals("Admin"))
                {
                    Intent adminhome = new Intent(this, PartsCribberAdminMenu.class);
                    adminhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    redirect(adminhome);
                    break;
                }
                else if(user.getUsertype().equals("Student"))
                {
                    Intent studenthome = new Intent(this, PartsCribberStudentMenu.class);
                    studenthome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    redirect(studenthome);
                    break;
                }
                else
                {
                    //Do not respond.
                    break;
                }

            case R.id.profile:
                Intent profileActivity = new Intent(this, PartsCribberViewProfile.class);
                redirect(profileActivity);
                break;

            case R.id.password:
                //Do Nothing Here
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

    public void savePasswordChanges (View view)
    {
        oldpassword = currentPassword.getText().toString();
        confirmoldpassword = confirmCurrentPassword.getText().toString();
        newpassword = newPassword.getText().toString();
        confirmnewpassword = confirmNewPassword.getText().toString();

        if (oldpassword.equals("") && confirmoldpassword.equals("") && newpassword.equals("") && confirmnewpassword.equals(""))
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.empty_request));
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
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
                builder.setMessage(getString(R.string.unmatched_currentpassword));
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
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
                    builder.setMessage(getString(R.string.unmatched_currentpassword));
                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
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
                        builder.setMessage(getString(R.string.password_length));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
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
                            builder.setMessage(getString(R.string.unmatched_newpasswords));
                            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
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

    public void redirect(final Intent newscreen)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.exit_confirm));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                startActivity(newscreen);
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
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

    /*public void onBackPressed()
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
    }*/
}
