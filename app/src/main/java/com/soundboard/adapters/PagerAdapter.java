package com.soundboard.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.soundboard.models.Categories;
import com.soundboard.activities.TabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final Categories[] orderedCategories;
    private int numElements;

    public PagerAdapter(FragmentManager fm, int numElements, Categories[] orderedCategories) {
        super(fm);
        this.numElements = numElements;
        this.orderedCategories = orderedCategories;
    }

    @Override
    public TabFragment getItem(int position) {
        return TabFragment.newInstance(orderedCategories[position]);
    }

    @Override
    public int getCount() {
        return numElements;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return orderedCategories[position].name();
    }
}