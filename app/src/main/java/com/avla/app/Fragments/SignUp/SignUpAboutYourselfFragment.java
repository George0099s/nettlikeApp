package com.avla.app.Fragments.SignUp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.avla.app.Constants;
import com.avla.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpAboutYourselfFragment extends Fragment {
    private static final String TAG = "SignUpAboutYourselfFrag";
    private SharedPreferences sharedPreferences;
    private EditText aboutYourselfED;

    public SignUpAboutYourselfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_about_yourself, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        aboutYourselfED = view.findViewById(R.id.about_yourself_ed);
        sharedPreferences = this.getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void setUserData() {
     sharedPreferences.edit().putString("about yourself", aboutYourselfED.getText().toString()).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        setUserData();
        super.onDetach();
    }
}
