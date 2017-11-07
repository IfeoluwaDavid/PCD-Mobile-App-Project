package ifeoluwa.partscribber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ifeoluwa David on 2017-10-31.
 */

public class StudentPossessionsAdapter extends ArrayAdapter
{
    private List list = new ArrayList();
    StudentPossessionsAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    public void add(PartsCribberReturnEquipment.StudentPossessions object)
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
            row = layoutInflater.inflate(R.layout.studentcart_rowlayout, parent, false);
            itemHolder = new ItemHolder();
            itemHolder.tx_itemname = row.findViewById(R.id.view_cart_item_name);
            itemHolder.ed_itemquantity = row.findViewById(R.id.view_cart_item_quantity);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemHolder) row.getTag();
        }

        PartsCribberReturnEquipment.StudentPossessions possessions = (PartsCribberReturnEquipment.StudentPossessions) this.getItem(position);//list.get(position);
        itemHolder.tx_itemname.setText(possessions.getItemname());
        itemHolder.ed_itemquantity.setText(possessions.getPossessionQuantity());
        return row;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }

    static class ItemHolder
    {
        TextView tx_itemname;
        EditText ed_itemquantity;
    }
}