package ifeoluwa.partscribber;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StudentHoldsAdapter extends ArrayAdapter
{
    private List list = new ArrayList();
    StudentHoldsAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    public void add(PartsCribberActualHolders.HoldObjects object)
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
        StudentHoldsAdapter.ItemHolder itemHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
            row = layoutInflater.inflate(R.layout.studentholds_rowlayout, parent, false);
            itemHolder = new StudentHoldsAdapter.ItemHolder();
            itemHolder.tx_username = row.findViewById(R.id.studentholds_username);
            itemHolder.tx_quantityheld = row.findViewById(R.id.studentholds_quantityheld);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder = (StudentHoldsAdapter.ItemHolder) row.getTag();
        }

        PartsCribberActualHolders.HoldObjects users = (PartsCribberActualHolders.HoldObjects) this.getItem(position);//list.get(position);
        itemHolder.tx_username.setText(users.getUsername());
        itemHolder.tx_quantityheld.setText(users.getQuantityHeld());
        return row;
    }


    static class ItemHolder
    {
        TextView tx_username;
        TextView tx_quantityheld;
    }
}
