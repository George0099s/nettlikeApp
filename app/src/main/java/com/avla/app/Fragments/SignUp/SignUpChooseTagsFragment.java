package com.avla.app.Fragments.SignUp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.avla.app.Adapter.TagAdapter;
import com.avla.app.Authorization.SignUp;
import com.avla.app.Constants;
import com.avla.app.Database.AppDatabase;
import com.avla.app.Entity.TokenEntity;
import com.avla.app.Interface.IServer;
import com.avla.app.Interface.TokenDao;
import com.avla.app.Model.ModelTag;
import com.avla.app.Model.PayloadTag;
import com.avla.app.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpChooseTagsFragment extends Fragment {
    private static final String TAG = "SignUpChooseTagsFragmen";
    private SharedPreferences sharedPreferences;
    private ArrayList<String> tagListName;
    private ArrayList<String> tagListId;
    private AppDatabase db;
    private TokenDao tokenDao;
    public static JSONArray tagJsonList;
    private RecyclerView tagRecyclerView;
    private TagAdapter tagAdapter;
    private EditText jobTitle;
    public SignUpChooseTagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sign_up_choose_tags, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        tagListName = new ArrayList<>();
        tagListId = new ArrayList<>();
        db =  Room.databaseBuilder(getContext(),
                AppDatabase.class, "avlaDB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        List<TokenEntity> tokenList;
        tokenList = tokenDao.getToken();

        getTags(tokenList.get(0).toString());
        tagRecyclerView = view.findViewById(R.id.tag_reycler);
        jobTitle = view.findViewById(R.id.job_title);
        jobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedPreferences.edit().putString("job title", jobTitle.getText().toString());
            }
        });
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
//    sharedPreferences.edit().putString("tags", "").commit();

    }

    private void getTags(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.getavla.com/") // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ModelTag> call = service.getAllTags(token);
        call.enqueue(new Callback<ModelTag>() {
            @Override
            public void onResponse(Call<ModelTag> call, Response<ModelTag> response) {
                ModelTag modelTag = response.body();
                List<PayloadTag> payload = modelTag.getPayload();
                for (int i = 0; i <payload.size(); i++) {
                        if (payload.get(i).getParentIds().size() == 0 )
                        tagListName.add(payload.get(i).getName());
                        tagListId.add(payload.get(i).getId());
                }

                tagAdapter = new TagAdapter(tagListName, tagListId, SignUp.tagList, getContext(), sharedPreferences);
                tagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                tagRecyclerView.setAdapter(tagAdapter);

            }

            @Override
            public void onFailure(Call<ModelTag> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
            }
        });
    }

}
