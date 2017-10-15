package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;

public class PartsCribberRegisterStudent extends AppCompatActivity
{
    EditText registered_username, registered_firstname, registered_lastname;
    EditText registered_email, registered_password, registered_confirmpassword;
    String username, first_name, last_name, email, password, confirmpassword;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_registerstudent);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        registered_username = (EditText) findViewById(R.id.register_username_hint);
        registered_firstname = (EditText) findViewById(R.id.register_firstname_hint);
        registered_lastname = (EditText) findViewById(R.id.register_lastname_hint);
        registered_email = (EditText) findViewById(R.id.register_email_hint);
        registered_password = (EditText) findViewById(R.id.register_password_hint);
        registered_confirmpassword = (EditText) findViewById(R.id.register_confirmpassword_hint);
    }

    public void userRegister(View view)
    {
        username = registered_username.getText().toString();
        password = registered_password.getText().toString();
        first_name = registered_firstname.getText().toString();
        last_name = registered_lastname.getText().toString();
        email = registered_email.getText().toString();
        confirmpassword = registered_confirmpassword.getText().toString();

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
            if(username.equals("") || studentUserNameValidation(username) == false)
            {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Something went wrong");
                builder.setMessage("Your Student ID is INVALID.");
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
                    builder.setMessage("Your Password must be up to 8 Characters without spaces.");
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
                        if (!password.equals(confirmpassword))
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
                                        String method = "register_student";
                                        BackgroundTasks backgroundTasks = new BackgroundTasks(this);
                                        backgroundTasks.execute(method,username,password,first_name,last_name,email);
                                        //finish();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean studentUserNameValidation(String x)
    {
        char[] ch = x.toCharArray();
        int letter = 0;
        int num = 0;
        int space = 0;
        int other = 0;

        char firstCharacter = x.charAt(0);

        for(int i = 0; i < x.length(); i++)
        {
            if(Character.isLetter(ch[i]))
            {
                letter ++ ;
            }
            else if (Character.isDigit(ch[i]))
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
        if(sum == 9 && (firstCharacter == 'N' || firstCharacter == 'n') && letter == 1 && num == 8 && unwantedCharacters < 1)
        {
            return true;
        }
        else
        {
            return false;
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

        if(sum < 8 || space > 0)
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
