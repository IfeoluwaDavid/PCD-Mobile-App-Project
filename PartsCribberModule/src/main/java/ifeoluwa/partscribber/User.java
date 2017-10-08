package ifeoluwa.partscribber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ifeoluwa David on 2017-10-07.
 */

public class User
{
    private String username, firstname, lastname, email;

    public User(String username, String firstname, String lastname, String email)
    {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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
}
