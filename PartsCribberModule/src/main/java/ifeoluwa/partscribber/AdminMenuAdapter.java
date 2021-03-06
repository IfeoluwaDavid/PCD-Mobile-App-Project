package ifeoluwa.partscribber;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

public class AdminMenuAdapter extends BaseExpandableListAdapter
{
    private Context ctx;
    private HashMap<String, List<String>> Admin_Menu;
    private List<String> Admin_List;

    public AdminMenuAdapter(Context ctx, HashMap<String, List<String>> Movies_category, List<String> Movies_list)
    {
        this.ctx = ctx;
        this.Admin_Menu = Movies_category;
        this.Admin_List = Movies_list;
    }

    @Override
    public int getGroupCount()
    {
        //This will return the number of list in the variable "Movie_list"
        return Admin_List.size();
    }

    @Override
    public int getChildrenCount(int i)
    {
        //Returns the number of sub-titles available in each list.
        return Admin_Menu.get(Admin_List.get(i)).size();
    }

    @Override
    public Object getGroup(int i)
    {
        //Returns current title available in the list.
        return Admin_List.get(i);
    }

    @Override
    public Object getChild(int parent, int child)
    {
        return Admin_Menu.get(Admin_List.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int i)
    {
        return i;
    }

    @Override
    public long getChildId(int parent, int child)
    {
        return child;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentview)
    {
        ExpandableListView mExpandableListView = (ExpandableListView) parentview;
        mExpandableListView.expandGroup(parent);

        String group_title = (String) getGroup(parent);
        if(convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.adminmenu_parentlayout, parentview, false);
        }
        TextView parent_textview = (TextView) convertView.findViewById(R.id.adminmenuparenttext);
        parent_textview.setTypeface(null, Typeface.BOLD);
        parent_textview.setText(group_title);
        return convertView;
    }

    @Override //Returns a view for each sub category
    public View getChildView(int parent, int child, boolean lastchild, View convertView, ViewGroup parentview)
    {
        String child_title = (String) getChild(parent, child);
        if(convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.adminmenu_childlayout, parentview, false);
        }
        TextView child_textview = (TextView) convertView.findViewById(R.id.adminmenuchildtext);
        child_textview.setText(child_title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1)
    {
        return true;
    }
}