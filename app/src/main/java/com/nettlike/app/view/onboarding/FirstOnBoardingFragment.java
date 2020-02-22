package com.nettlike.app.view.onboarding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nettlike.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstOnBoardingFragment extends Fragment {


    public FirstOnBoardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_onboard, container, false);
    }

}
