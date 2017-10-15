package ifeoluwa.partscribber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ifeoluwa David on 2017-10-07.
 */

public class User
{
    private String username, firstname, lastname, email, usertype;
    private int userID;

    public User(int userID, String username, String firstname, String lastname, String email, String usertype)
    {
        this.userID = userID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.usertype = usertype;
    }

    public int getUserID()
    {
        return userID;
    }

    public String getUsername()
    {
        return username;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUsertype()
    {
        return usertype;
    }
}
