package ifeoluwa.partscribber;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PartsCribberSplashScreen extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 3000;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_splashscreen);
        actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent homeIntent = new Intent(PartsCribberSplashScreen.this, PartsCribberLogin.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
