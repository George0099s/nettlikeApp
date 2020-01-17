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
public class SignUpNameFragment extends Fragment {
    private static final String TAG = "SignUpNameFragment";
    private EditText firstNameED, lastNameED, ageED;

    private SharedPreferences sharedpreferences;

    public SignUpNameFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up_name, container, false);


        sharedpreferences = this.getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        firstNameED = view.findViewById(R.id.first_name_ed);
        lastNameED = view.findViewById(R.id.last_name_ed);
        ageED = view.findViewById(R.id.age_ed);

    }

    @Override
    public void onPause() {
        super.onPause();
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

    private void setUserData() {
        sharedpreferences.edit().putString("first name", firstNameED.getText().toString()).commit();
        sharedpreferences.edit().putString("last name", lastNameED.getText().toString()).commit();
        sharedpreferences.edit().putString("age", ageED.getText().toString()).commit();
    }

}
