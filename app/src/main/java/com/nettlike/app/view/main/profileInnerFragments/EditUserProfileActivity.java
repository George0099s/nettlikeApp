package com.nettlike.app.view.main.profileInnerFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.UserTagAdapter;
import com.nettlike.app.data.AppDatabase;
import com.nettlike.app.data.TokenDao;
import com.nettlike.app.model.Payload;
import com.nettlike.app.model.PayloadTag;
import com.nettlike.app.model.UserModel;
import com.nettlike.app.model.UserPayload;
import com.nettlike.app.model.UserSingleton;
import com.nettlike.app.view.signUp.SignUpChooseCategoryActivity;
import com.nettlike.app.view.signUp.SignUpCountryActivity;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditUserProfileActivity";
    private AppDatabase db;
    private ArrayList<String> tagListName = new ArrayList<>();
    private ArrayList<String> tagListId = new ArrayList<>();
    private JSONArray tagJson;
    private RecyclerView tagRecyclerView;
    private UserTagAdapter tagAdapter;
    private static final int IMAGE_REQUEAST = 1;
    private EditText firstName, lastName, age, jobTitle, aboutYourself, twitterLink, facebookLink;
    private String token, userPictureId;
    private TextView email, chooseTags, chooseCountry;
    private Button saveData, addTags;
    private Activity activity;
    private TokenDao tokenDao;
    private ImageView editUserImg;
    private SharedPreferences mPrefs;
    private UserSingleton user = UserSingleton.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        initViews();
        rxRequest();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getTags();
    }

    private void initViews() {
        tagListName = new ArrayList<>();
        tagListId = new ArrayList<>();
        activity = this;
        token = getIntent().getStringExtra("token");
        chooseTags = findViewById(R.id.choose_tags);
        chooseTags.setOnClickListener(this::onClick);
        chooseCountry = findViewById(R.id.select_country);
        chooseCountry.setOnClickListener(this::onClick);



        tagRecyclerView = findViewById(R.id.edit_recycler_Tag);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "avlaDB")
                .allowMainThreadQueries()
                .build();
        tokenDao = db.tokenDao();
        db.close();
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        age = findViewById(R.id.age);
        jobTitle = findViewById(R.id.follower_username);
        email = findViewById(R.id.email);
        aboutYourself = findViewById(R.id.about_yourself_ed);
        twitterLink = findViewById(R.id.twitter_link);
        facebookLink = findViewById(R.id.facebook_link);
        saveData = findViewById(R.id.save_data);
        editUserImg = findViewById(R.id.edit_img_profile);
        editUserImg.setOnClickListener(this::onClick);
        saveData.setOnClickListener(this::onClick);
        findViewById(R.id.log_out_btn).setOnClickListener(this::onClick);
        token = getIntent().getStringExtra("token");
        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        age.setText(user.getAge());
        jobTitle.setText(user.getJobTitle());
        email.setText(user.getEmail());
        aboutYourself.setText(user.getAboutMyself());
        Glide.with(EditUserProfileActivity.this).load(user.getPictureId()).apply(RequestOptions.circleCropTransform()).into(editUserImg);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_data:
                Observable.just(( saveUserData(token,
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        age.getText().toString(),
                        jobTitle.getText().toString(),
                        aboutYourself.getText().toString(),
                        user.getCountry(),
                        user.getCity(),
                        user.getTagList(),
                        twitterLink.getText().toString(),
                        facebookLink.getText().toString())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();


                break;
            case R.id.edit_img_profile:
                selectImage();
                break;
            case R.id.log_out_btn:
                logOut();
                break;
            case R.id.select_country:
                Intent intent = new Intent(this, SignUpCountryActivity.class);
                intent.putExtra("token", getIntent().getStringExtra("token"));
                startActivity(intent);
                break;
            case R.id.choose_tags:
                Intent intent2 = new Intent(this, SignUpChooseCategoryActivity.class);
                intent2.putExtra("token", token);
                startActivity(intent2);
                break;
        }

    }

    private void logOut() {
        mPrefs.edit().putBoolean("isLogIn", false).apply();
        deleteToken();
        tokenDao.deleteAll();
        user.setExist(false);
        user.clearAll();
        finish();
    }

    private void deleteToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<Payload> call = service.deleteToken(token);
        call.enqueue(new Callback<Payload>() {
            @Override
            public void onResponse(Call<Payload> call, Response<Payload> response) {
                Payload object = response.body();
            }

            @Override
            public void onFailure(Call<Payload> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
    }

    private void selectImage() {
        requestStoragePermission();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_PICK);
        startActivityForResult(intent, IMAGE_REQUEAST);
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEAST && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            OutputStream os = null;

            try {
                InputStream is = getContentResolver().openInputStream(uri);
                String type = getContentResolver().getType(uri);

                File result = new File(getFilesDir(), user.getUserId() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
                os = new BufferedOutputStream(new FileOutputStream(result));

                Bitmap pictureBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri); // obtaining the Bitmap
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG,50 , os); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                updateImage(result);
                os.flush();
                os.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateImage(File userImageFile) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), userImageFile);
        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("file", userImageFile.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);
        Call<UserModel> updateImage = service.updateUserImage(token, requestImg);
        updateImage.enqueue(new Callback<UserModel>() {

            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel object = response.body();
                Log.d(TAG, "onResponse: " + response.body());
                Toast.makeText(EditUserProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(EditUserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean saveUserData(String token,
                              String firstName,
                              String lastName,
                              String age,
                              String jobTitle,
                              String aboutYourself,
                              String country,
                              String city,
                              JSONArray tagJson,
                              String twitterLink,
                              String facebookLink) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<UserModel> call = service.saveUserData(token, firstName, lastName, age, jobTitle, aboutYourself,country, city,tagJson, twitterLink, facebookLink);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel object = response.body();

                assert object != null;
                if (object.getOk()) {
                    Toast.makeText(EditUserProfileActivity.this, "Data changed", Toast.LENGTH_SHORT).show();
                    rxRequest();
                    finish();
                } else {
                    Toast.makeText(EditUserProfileActivity.this, "Something went wrong" + object.getOk(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

            }
        });
        return true;
    }


    private void rxRequest() {
        Observable.just((getUserInfo()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private String getUserInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<UserModel> call = service.getUserInfo(token);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel object = response.body();
                UserPayload userPayload = object.getPayload();
                JSONArray tagsJsonArray = new JSONArray();
                List<PayloadTag> tags = userPayload.getTags();
                for (int i = 0; i < tags.size(); i++) {
                    tagsJsonArray.put(tags.get(i).getName());
                }
                user.setPictureId(userPayload.getPictureUrl());
                user.setFirstName(userPayload.getFirstName());
                user.setLastName(userPayload.getLastName());
                user.setAge(userPayload.getAge());
                user.setJobTitle(userPayload.getJobTitle());
                user.setEmail(userPayload.getEmail());
                user.setAboutMyself(userPayload.getDesctiption());
                user.setTagList(tagsJsonArray);

                chooseCountry.setText(String.format("%s, %s", user.getCountry(), user.getCity()));
                getTags();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onResponse: signUp fail " + t.getStackTrace());
            }
        });
        return userPictureId;
    }
    private void getTags(){
        tagJson = new JSONArray();
       tagListName = user.getTagsName();
        for (int i = 0; i < tagListName.size(); i++) {
            tagJson.put(tagListName.get(i));
        }
       tagAdapter = new UserTagAdapter(tagJson,this);
       tagRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
       tagRecyclerView.setAdapter(tagAdapter);

    }
}

