package ifeoluwa.partscribber;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class PCRegisterStudentFragment extends Fragment
{
    EditText registered_username, registered_firstname, registered_lastname;
    EditText registered_email, registered_password, registered_confirmpassword;
    String username, first_name, last_name, email, password, confirmpassword;
    View finder;

    public PCRegisterStudentFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        finder = inflater.inflate(R.layout.pcregisterstudent_fragment, container, false);
        registered_username = (EditText) finder.findViewById(R.id.register_username_hint);
        registered_firstname = (EditText) finder.findViewById(R.id.register_firstname_hint);
        registered_lastname = (EditText) finder.findViewById(R.id.register_lastname_hint);
        registered_email = (EditText) finder.findViewById(R.id.register_email_hint);
        registered_password = (EditText) finder.findViewById(R.id.register_password_hint);
        registered_confirmpassword = (EditText) finder.findViewById(R.id.register_confirmpassword_hint);

        final Button userRegister = (Button) finder.findViewById(R.id.register_singlestudent_button);
        userRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
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
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                                UserInfoBackgroundTasks userInfoBackgroundTasks = new UserInfoBackgroundTasks(getActivity());
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
            }
        });

        return finder;
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
