package com.nettlike.app.people.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.model.Account;
import com.nettlike.app.model.Model;
import com.nettlike.app.model.Payload;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.profile.UserTagAdapter;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnotherUserProfileFrament extends Fragment {
    private static final String TAG = "AnotherUserProfileFrame";
    private TextView userJob, aboutUser;
    private ImageView sendEmail, sendFacebook, sendTwitter;
    private String token, userId;
    private RecyclerView tagRecycler;
    private UserTagAdapter userTagAdapter;
    private JSONArray tagsJson = new JSONArray();
    private ProgressDialog pd;
    private String twitterLink, facebookLink, email;
    public AnotherUserProfileFrament() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_another_user_profile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        sendEmail = view.findViewById(R.id.send_email_another);
        sendFacebook = view.findViewById(R.id.send_facebook_another);
        sendTwitter = view.findViewById(R.id.send_twitter_another);
        sendTwitter.setOnClickListener(this::onClick);
        sendFacebook.setOnClickListener(this::onClick);
        sendEmail.setOnClickListener(this::onClick);
        tagRecycler = view.findViewById(R.id.tag_another_user);
        tagRecycler.setHasFixedSize(true);
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userJob = view.findViewById(R.id.user_job);
        aboutUser = view.findViewById(R.id.aboutUser);
        token = getActivity().getIntent().getStringExtra("token");
        userId = getActivity().getIntent().getStringExtra("user id");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());

        rxRequest();
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_email_another:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.send_facebook_another:
                Intent openlink = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.facebook_link) + Uri.parse(facebookLink)));
                startActivity(Intent.createChooser(openlink, "Browser"));
                break;
            case R.id.send_twitter_another:
                Intent openlinkTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.twitter_link) + Uri.parse(twitterLink)));
                startActivity(Intent.createChooser(openlinkTwitter, "Browser"));
                break;
        }
    }

    private void rxRequest(){
        pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();
        Observable.just((getUserInfo(userId, token)))
                .subscribeOn(Schedulers.io())
                .subscribe();

    }




    private Boolean getUserInfo(String id, String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Model> call = service.getAnotherUserInfo(id,token);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model model= response.body();
                Payload payload = model.getPayload();
                Account account = payload.getAccount();
                List<PayloadTag> tags = account.getTags();
                for (int i = 0; i < tags.size(); i++) {
                    tagsJson.put(tags.get(i).getName());
                }
                userJob.setText(account.getJobTitle());
                aboutUser.setText(account.getAboutYourself());
                email = account.getEmail();
                twitterLink = account.getTwitter_url();
                facebookLink = account.getFacebook_url();
                if (twitterLink.isEmpty() || twitterLink == null)
                    sendTwitter.setVisibility(View.GONE);
                if (facebookLink.isEmpty() || facebookLink == null)
                    sendFacebook.setVisibility(View.GONE);
                userTagAdapter = new UserTagAdapter(tagsJson, getContext());
                tagRecycler.setAdapter(userTagAdapter);
                pd.cancel();
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return true;
    }
}
