package ifeoluwa.partscribber;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class PartsCribberStudentCart extends AppCompatActivity {

    Intent intent;
    String validatedID;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partscribber_studentcart);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#01579B'>PartsCribber</font>"));

        intent = getIntent();
        validatedID = intent.getStringExtra("theID");

        TextView theID = (TextView) findViewById(R.id.textView2);
        theID.setText(validatedID);
    }
}
