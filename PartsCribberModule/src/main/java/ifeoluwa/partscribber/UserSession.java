package ifeoluwa.partscribber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

//here for this class we are using a singleton pattern

public class UserSession
{
    //the constants
    private static final String SHARED_PREF_NAME = "sharedPreference";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_FIRSTNAME = "keyfirstname";
    private static final String KEY_LASTNAME = "keylastname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_USERTYPE = "keyusertype";

    private static UserSession mInstance;
    private static Context mCtx;

    private UserSession(Context context)
    {
        mCtx = context;
    }

    public static synchronized UserSession getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new UserSession(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getUserID());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_FIRSTNAME, user.getFirstname());
        editor.putString(KEY_LASTNAME, user.getLastname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_USERTYPE, user.getUsertype());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User
        (
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_FIRSTNAME, null),
                sharedPreferences.getString(KEY_LASTNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_USERTYPE, null)
        );
    }

    //this method will logout the user
    public void logout()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}