package org.ranjanistic.attorney.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.ranjanistic.attorney.R;

import javax.annotation.Nullable;

public class FragmentTextProcessorA extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_text_processor_a, container,false);
        return root;
    }
}
