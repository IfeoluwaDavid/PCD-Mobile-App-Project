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

        List<String> add_new_user = new ArrayList<String>();
        add_new_user.add("Register Student/Admin");
        add_new_user.add("Search Student User");

        List<String> rentals_and_returns = new ArrayList<String>();
        rentals_and_returns.add("Rent Equipment");
        rentals_and_returns.add("Return Equipment");

        List<String> profile_settings = new ArrayList<String>();
        profile_settings.add("View/Edit My Profile");
        profile_settings.add("Change My Password");

        List<String> inventory = new ArrayList<String>();
        inventory.add("Add New Equipment");
        inventory.add("Search/View Equipment");

        Admin_Menu.put("REGISTER/FIND USER", add_new_user);
        Admin_Menu.put("RENTALS & RETURNS", rentals_and_returns);
        Admin_Menu.put("PROFILE SETTINGS", profile_settings);
        Admin_Menu.put("INVENTORY", inventory);

        return Admin_Menu;
    }
}
