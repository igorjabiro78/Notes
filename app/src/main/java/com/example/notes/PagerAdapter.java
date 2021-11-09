package com.example.notes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int behavior,int numberOfTabs){
        super(fm,behavior);
        this.numberOfTabs = numberOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainActivity();
            case 1:
                return new Deleted_Notes();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
