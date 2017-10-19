package ifeoluwa.partscribber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ifeoluwa David on 2017-10-16.
 */

public class CategoryMenuAdapter extends ArrayAdapter
{
    private List list = new ArrayList();
    CategoryMenuAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    public void add(String object)
    {
        // super.add(object);
        list.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent)
    {
        View row;
        row = convertview;
        CategoryHolder categoryHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
            row = layoutInflater.inflate(R.layout.viewcategory_rowlayout, parent, false);
            categoryHolder = new CategoryHolder();
            categoryHolder.tx_category = row.findViewById(R.id.viewcategory_categoryname);
            row.setTag(categoryHolder);
        }
        else
        {
            categoryHolder = (CategoryHolder) row.getTag();
        }

        String itemcategory = (String)list.get(position);
        categoryHolder.tx_category.setText(itemcategory);
        return row;
    }

    static class CategoryHolder
    {
        TextView tx_category;
    }
}
