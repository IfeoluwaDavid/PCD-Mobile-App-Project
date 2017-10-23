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

/**
 * Created by Ifeoluwa David on 2017-10-09.
 */

public class StudentMenuAdapter extends BaseExpandableListAdapter
{
    private Context ctx;
    private HashMap<String, List<String>> Student_Menu;
    private List<String> Student_List;

    public StudentMenuAdapter(Context ctx, HashMap<String, List<String>> Movies_category, List<String> Movies_list)
    {
        this.ctx = ctx;
        this.Student_Menu = Movies_category;
        this.Student_List = Movies_list;
    }

    @Override
    public int getGroupCount()
    {
        //This will return the number of list in the variable "Movie_list"
        return Student_List.size();
    }

    @Override
    public int getChildrenCount(int i)
    {
        //Returns the number of sub-titles available in each list.
        return Student_Menu.get(Student_List.get(i)).size();
    }

    @Override
    public Object getGroup(int i)
    {
        //Returns current title available in the list.
        return Student_List.get(i);
    }

    @Override
    public Object getChild(int parent, int child)
    {
        return Student_Menu.get(Student_List.get(parent)).get(child);
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
            convertView = inflator.inflate(R.layout.studentmenu_parentlayout, parentview, false);
        }
        TextView parent_textview = (TextView) convertView.findViewById(R.id.studentmenutext);
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
            convertView = inflator.inflate(R.layout.studentmenu_childlayout, parentview, false);
        }
        TextView child_textview = (TextView) convertView.findViewById(R.id.studentmenuchildtext);
        child_textview.setText(child_title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1)
    {
        return true;
    }
}
