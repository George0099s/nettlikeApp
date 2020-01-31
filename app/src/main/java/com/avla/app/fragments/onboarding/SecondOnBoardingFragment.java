package com.avla.app.fragments.onboarding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.avla.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondOnBoardingFragment extends Fragment {


    public SecondOnBoardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_on_boarding, container, false);
    }

}
