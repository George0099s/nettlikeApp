package com.avla.app.Fragments.SignUp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.avla.app.Constants;
import com.avla.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpChooseTagsFragment extends Fragment {
    private static final String TAG = "SignUpChooseTagsFragmen";
    private SharedPreferences sharedPreferences;
    public SignUpChooseTagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_up_choose_tags, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        TextView s = view.findViewById(R.id.name);
        s.setText(sharedPreferences.getString("first name", ""));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
//        setData();
        super.onDetach();
    }

    private void setData() {
    sharedPreferences.edit().putString("tags", "").commit();

    }

}
