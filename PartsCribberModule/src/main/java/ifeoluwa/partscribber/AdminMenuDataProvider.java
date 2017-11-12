package ifeoluwa.partscribber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ifeoluwa David on 2017-10-09.
 */

public class AdminMenuDataProvider
{
    public static HashMap<String, List<String>> getInfo()
    {
        HashMap <String, List<String>> Admin_Menu = new HashMap <String, List<String>>();

        List<String> users_and_tools = new ArrayList<String>();
        users_and_tools.add("Add/Search Users");
        users_and_tools.add("Add/Search Tools");

        List<String> rentals_and_returns = new ArrayList<String>();
        rentals_and_returns.add("Rent Equipment");
        rentals_and_returns.add("Return Equipment");

        List<String> profile_settings = new ArrayList<String>();
        profile_settings.add("View/Edit My Profile");
        profile_settings.add("Change My Password");

        Admin_Menu.put("ADD & SEARCH", users_and_tools);
        Admin_Menu.put("RENTALS & RETURNS", rentals_and_returns);
        Admin_Menu.put("PROFILE SETTINGS", profile_settings);

        return Admin_Menu;
    }
}
