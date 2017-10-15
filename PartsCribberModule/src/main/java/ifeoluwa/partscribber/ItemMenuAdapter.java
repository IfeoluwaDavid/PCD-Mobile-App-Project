package ifeoluwa.partscribber;
import android.content.Context;
import android.util.Log;
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

    public void add(Item object)
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
            LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());//.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.viewtools_rowlayout, parent, false);
            itemHolder = new ItemHolder();
            itemHolder.tx_itemname = row.findViewById(R.id.viewtools_itemnametext);
            itemHolder.tx_qtyavailable = row.findViewById(R.id.viewtools_itemqtytext);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemHolder) row.getTag();
        }

        Item item = (Item)list.get(position);
        //Log.d("Debug", item.getItemName());
        //Log.d("Debug", String.valueOf(item.getQtyAvailable()));
        itemHolder.tx_itemname.setText(item.getItemName());
        itemHolder.tx_qtyavailable.setText(String.valueOf(item.getQtyAvailable()));
        return row;
    }

    static class ItemHolder
    {
        TextView tx_itemid, tx_itemname, tx_itemserialno, tx_qtyavailable, tx_qtyrented, tx_qtytotal, tx_category;
    }

}
