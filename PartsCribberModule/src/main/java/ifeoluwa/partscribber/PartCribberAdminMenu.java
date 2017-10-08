package ifeoluwa.partscribber;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PartCribberAdminMenu extends AppCompatActivity
{
    private AlertDialog alertDialog;
    TextView my_username, my_firstname, my_lastname, my_email, my_usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_adminmenu);

        my_username = (TextView) findViewById(R.id.myUsername);
        my_firstname = (TextView) findViewById(R.id.myFirstname);
        my_lastname = (TextView) findViewById(R.id.myLastname);
        my_email = (TextView) findViewById(R.id.myEmail);
        my_usertype = (TextView) findViewById(R.id.myUsertype);

        User user = PartsCribberUserSession.getInstance(this).getUser();

        my_username.setText(user.getUsername());
        my_firstname.setText(user.getFirstname());
        my_lastname.setText(user.getLastname());
        my_email.setText(user.getEmail());

    }

    public void logout(View view)
    {
        finish();
        PartsCribberUserSession.getInstance(getApplicationContext()).logout();
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
                /*Intent intent=new Intent(this, PartsCribberLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
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
