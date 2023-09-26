package com.examle.jiang_yan.fast_develop.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examle.jiang_yan.fast_develop.R;

/**
 * Created by jiang_yan on 2016/9/30.
 */
public class TabFragment_1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, null);
        return view;
    }
}
