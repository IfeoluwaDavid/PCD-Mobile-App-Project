package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;

public class PartsCribberRegisterAdmin extends AppCompatActivity
{
    EditText admin_username, admin_firstname, admin_lastname;
    EditText admin_email, admin_password, admin_confirmpassword;
    String username, first_name, last_name, email, password, confirmpassword;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_registeradmin);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        admin_username = (EditText) findViewById(R.id.newadmin_username);
        admin_password = (EditText) findViewById(R.id.newadmin_password);
        admin_confirmpassword = (EditText) findViewById(R.id.newadmin_confirmpassword);
        admin_firstname = (EditText) findViewById(R.id.newadmin_firstname);
        admin_lastname = (EditText) findViewById(R.id.newadmin_lastname);
        admin_email = (EditText) findViewById(R.id.newadmin_email);
    }

    public void adminRegister(View view)
    {
        username = admin_username.getText().toString();
        password = admin_password.getText().toString();
        confirmpassword = admin_confirmpassword.getText().toString();
        first_name = admin_firstname.getText().toString();
        last_name = admin_lastname.getText().toString();
        email = admin_email.getText().toString();

        if (username.equals("") && password.equals("") && confirmpassword.equals("") &&
                first_name.equals("") && last_name.equals("") && email.equals(""))
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Empty Registration Request");
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
            if(username.equals("") || !adminUsernameValidation(username))
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Something went wrong");
                builder.setMessage("Your Admin ID is INVALID.");
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
                if(password.equals("") || !passwordValidation(password))
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Something went wrong");
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
                    if(!confirmpassword.equals(password))
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle("Something went wrong");
                        builder.setMessage("Your Passwords don't match");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                        if (!namesValidation(first_name))
                        {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                            builder.setTitle("Something went wrong");
                            builder.setMessage("First name should be more than 2 characters with letters only");
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
                            if (!namesValidation(last_name))
                            {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                                builder.setTitle("Something went wrong");
                                builder.setMessage("Last name should be more than 2 characters with letters only");
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
                                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                                if (!email.matches(emailPattern))
                                {
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                                    builder.setTitle("Something went wrong");
                                    builder.setMessage("Email Address is Invalid");
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
                                    String method = "register_admin";
                                    UserInfoBackgroundTasks userInfoBackgroundTasks = new UserInfoBackgroundTasks(this);
                                    userInfoBackgroundTasks.execute(method,username,password,first_name,last_name,email);
                                    //finish();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean adminUsernameValidation(String x)
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
        int sum = letter + num;
        int unwantedCharacters = space + other;

        if(sum < 6 || unwantedCharacters > 0)
        {
            return false;
        }
        else
        {
            return true;
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

    public boolean namesValidation(String x)
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
        int sum = letter;
        int unwantedCharacters = num + other + space;

        if(sum < 2 || unwantedCharacters > 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
