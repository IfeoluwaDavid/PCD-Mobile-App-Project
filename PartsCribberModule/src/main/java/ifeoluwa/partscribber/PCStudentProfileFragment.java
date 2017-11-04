package ifeoluwa.partscribber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */

public class PCStudentProfileFragment extends Fragment
{
    View finder;
    private PCStudentProfileFragment.PCStudentProfileFragmentInterface mListener;

    public PCStudentProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        finder = inflater.inflate(R.layout.pcstudentprofile_fragment, container, false);

        String profileStudentID = mListener.passStudentID();
        String fname = mListener.getStudentFirstName();
        String lname = mListener.getStudentLastName();
        String possessionqty = mListener.getStudentPossessionqty();
        String email = mListener.getStudentEmail();
        String status = mListener.getStudentStatus();

        TextView studentIDheader = (TextView) finder.findViewById(R.id.textView3);
        EditText studentfullname = (EditText) finder.findViewById(R.id.editText);
        EditText studentpossessionqty = (EditText) finder.findViewById(R.id.editText2);
        EditText studentemail = (EditText) finder.findViewById(R.id.editText3);
        EditText studentstatus = (EditText) finder.findViewById(R.id.editText4);

        studentIDheader.setText(profileStudentID);
        studentfullname.setText("Full Name: " + fname + " " + lname);
        studentpossessionqty.setText("Possession Qty: " + possessionqty);
        studentemail.setText("Email: " + email);
        studentstatus.setText("Status: " + status);

        return finder;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onAttach(Context ctx)
    {
        super.onAttach(ctx);
        try
        {
            mListener = (PCStudentProfileFragment.PCStudentProfileFragmentInterface)ctx;
        }
        catch (ClassCastException c)
        {
            throw new ClassCastException(ctx.toString() + " should implememt PCStudentProfileFragmentInterface");
        }
    }

    interface PCStudentProfileFragmentInterface
    {
        String passStudentID();
        String getStudentFirstName();
        String getStudentLastName();
        String getStudentPossessionqty();
        String getStudentEmail();
        String getStudentStatus();
    }
}
