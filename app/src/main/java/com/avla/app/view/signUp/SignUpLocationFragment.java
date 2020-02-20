package com.avla.app.view.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.avla.app.R;
import com.avla.app.model.UserSingleton;

public class SignUpLocationFragment extends Fragment {
    private static final String TAG = "SignUpLocationFragment";
    private TextView selectCountry;
    private UserSingleton user = UserSingleton.INSTANCE;
    public SignUpLocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_location, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user.getCountry() != null){
            selectCountry.setText(String.format("%s, %s", user.getCountry(), user.getCity()));
        }
    }

    private void initViews(View view) {

        selectCountry = view.findViewById(R.id.select_country);
        selectCountry.setOnClickListener(this::onClick);
    }
    private void onClick(View view){
        Intent intent = new Intent(getContext(), SignUpCountryActivity.class);
        intent.putExtra("token", getActivity().getIntent().getStringExtra("token"));
        startActivity(intent);
    }

}
