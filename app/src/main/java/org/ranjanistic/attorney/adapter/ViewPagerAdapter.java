package org.ranjanistic.attorney.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.ranjanistic.attorney.TextProcessor;
import org.ranjanistic.attorney.fragment.FragmentTextProcessorA;
import org.ranjanistic.attorney.fragment.FragmentTextProcessorB;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, int behaviour){
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
