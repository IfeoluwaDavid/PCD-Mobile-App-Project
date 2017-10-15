package ifeoluwa.partscribber;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

public class PartsCribberChangePassword extends AppCompatActivity
{
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_changepassword);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));
    }
}
