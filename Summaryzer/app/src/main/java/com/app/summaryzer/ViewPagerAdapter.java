package com.app.summaryzer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ViewPagerAdapter(@NonNull FragmentManager fragmentManager, int behaviour){
        super(fragmentManager,behaviour);
    }
    @NonNull
    @Override
    public Fragment getItem(int position){
        switch (position){
            case TextProcessor.FRAG_A: return new FragmentTextProcessorA();
            case TextProcessor.FRAG_B: return new FragmentTextProcessorB();
        }
        return new FragmentTextProcessorA();
    }

    @Override
    public int getCount(){
        return 2;
    }
}
