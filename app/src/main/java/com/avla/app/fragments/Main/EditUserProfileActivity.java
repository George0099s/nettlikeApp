package com.avla.app.fragments.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.User;
import com.avla.app.model.UserPayload;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditUserProfileActivity";
    private static final int IMAGE_REQUEAST = 1;
    private EditText firstName, lastName, age, jobTitle, aboutYourself, twitterLink, facebookLink;
    private String token;
    private TextView email;
    private Button saveData;
    private ImageView editUserImg;
    private Uri imageUri;
    private Bitmap userImageFile;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        initViews();
    }

    private void initViews() {
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        age = findViewById(R.id.age);
        jobTitle = findViewById(R.id.job_title);
        email = findViewById(R.id.email);
        aboutYourself = findViewById(R.id.about_yourself_ed);
        twitterLink = findViewById(R.id.twitter_link);
        facebookLink = findViewById(R.id.facebook_link);
        saveData = findViewById(R.id.save_data);
        editUserImg = findViewById(R.id.edit_img_profile);
        editUserImg.setOnClickListener(this::onClick);
        saveData.setOnClickListener(this::onClick);
        token = getIntent().getStringExtra("token");
        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.execute();
    }

    private void onClick(View view) {
       switch (view.getId()){
           case R.id.save_data:
               saveUserData(token,
                       firstName.getText().toString(),
                       lastName.getText().toString(),
                       String.valueOf(UserPayload.getCurrentYear() - Integer.parseInt(age.getText().toString())),
                       jobTitle.getText().toString(),
                       aboutYourself.getText().toString(),
                       twitterLink.getText().toString(),
                       facebookLink.getText().toString());
               break;
           case R.id.edit_img_profile:
                selectImage();
               break;
       }

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEAST);
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_REQUEAST && data != null
//                && data.getData() != null) {
//            Uri uri = data.getData();
//            File file =new File(String.valueOf(getFileStreamPath(String.valueOf(uri))));
//
//            try {
//
//                InputStream inputStream = getContentResolver().openInputStream(uri);
//                FileInputStream inputStreamf = this.openFileInput("com.android.providers.downloads.documents/document/33");
//                Log.d(TAG, "onActivityResult: filestream  " + inputStreamf);
//
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
////                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
//                File file = new File("com.android.providers.downloads.documents/document/33.jpg");
//                Log.d(TAG, "onActivityResult: 123" + bitmap.toString() + " file   " + file);
////                updateImage(imageToString(bitmap));
//                updateImage(file);
////                UpdateImage updateImage = new UpdateImage(imageToString(bitmap));
////                updateImage.execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Log.d(TAG, "onActivityResult: uri   " + uri);
//
//
//        }
//    }

    private void updateImage(Bitmap userImageFile) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IServer service = retrofit.create(IServer.class);

        Call<User> updateImage = service.updateUserImage(token, userImageFile);
        updateImage.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User object = response.body();
                Toast.makeText(EditUserProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditUserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData(String token,
                              String firstName,
                              String lastName,
                              String age,
                              String jobTitle,
                              String aboutYourself,
                              String twitterLink,
                              String facebookLink) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASIC_URL) // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            IServer service = retrofit.create(IServer.class);
            Call<User> call = service.saveUserData(token, firstName, lastName, age, jobTitle, aboutYourself, twitterLink, facebookLink);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User object = response.body();

                    if(object.getOk() == true){
                        Toast.makeText(EditUserProfileActivity.this, "Data changed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditUserProfileActivity.this, "Something went wrong" + object.getOk(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(TAG, "onResponse: signUp fail " + t.getMessage());

                }
            });



        }

        private String imageToString(Bitmap bitmap){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[]imgBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgBytes,  Base64.DEFAULT);
        }
    private class GetUserInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        String token = getIntent().getStringExtra("token");
        @Override
        protected Void doInBackground(String... strings) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASIC_URL) // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                    .build();

            IServer service = retrofit.create(IServer.class);
            Call<User> call = service.getUserInfo(token);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User object = response.body();
                    UserPayload userPayload = object.getPayload();
                    firstName.setText(userPayload.getFirstName());
                    lastName.setText(userPayload.getLastName());
                    age.setText(userPayload.getAge());
                    jobTitle.setText(userPayload.getJobTitle());
                    email.setText(userPayload.getEmail());
                    aboutYourself.setText(userPayload.getDesctiption());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d(TAG, "onResponse: signUp fail " + t.getMessage());
                }
            });

            return null;
        }
    }


//    private class UpdateImage extends AsyncTask<Void, Void, Void> {
//        String s;
//
//        public UpdateImage(String bitmap) {
//            this.s = bitmap;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.getavla.com/") // Адрес сервера
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            IServer service = retrofit.create(IServer.class);
//
//            Call<User> updateImage = service.updateUserImage(token, s);
//            updateImage.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    User object = response.body();
//                    Log.d(TAG, "onResponse: " + response.message());
////                     Toast.makeText(EditUserProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                    Toast.makeText(EditUserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            return null;
//
//        }
//    }

}

