package ifeoluwa.partscribber;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Team Name - CPU
Project Name - Parts Crib Database
Member Names - Ifeoluwa David Adese, Mohand Ferawana, Tosin Ajayi
*/

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();

    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public void addFragments(Fragment fragments, String titles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment)
        {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position)
    {
        String tag = mFragmentTags.get(position);
        if (tag == null)
        {
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }

    ViewPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer,String>();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles.get(position);
    }
}