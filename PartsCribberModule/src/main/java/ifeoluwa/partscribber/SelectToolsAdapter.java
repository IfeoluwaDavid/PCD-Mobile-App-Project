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
 * Created by Ifeoluwa David on 2017-10-14.
 */

public class ItemMenuAdapter extends ArrayAdapter
{
    private List list = new ArrayList();
    ItemMenuAdapter(Context context, int resource)
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
        ItemHolder itemHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
            row = layoutInflater.inflate(R.layout.selecttools_rowlayout, parent, false);
            itemHolder = new ItemHolder();
            itemHolder.tx_itemname = row.findViewById(R.id.viewtools_itemnametext);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemHolder) row.getTag();
        }

        String itemname = (String) list.get(position);
        itemHolder.tx_itemname.setText(itemname);
        return row;
    }

    static class ItemHolder
    {
        TextView tx_itemname;
    }

}