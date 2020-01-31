package com.avla.app.fragments.main.profileInnerFragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.User;
import com.avla.app.model.UserPayload;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditUserProfileActivity";
    private static final int IMAGE_REQUEAST = 1;
    private EditText firstName, lastName, age, jobTitle, aboutYourself, twitterLink, facebookLink;
    private String token, userPictureId;
    private TextView email;
    private Button saveData;
    private ImageView editUserImg;
    private SharedPreferences mPrefs;
    private Uri imageUri;
    private Bitmap userImageFile;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        initViews();
        rxRequest();

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
        findViewById(R.id.log_out_btn).setOnClickListener(this::onClick);
        token = getIntent().getStringExtra("token");
        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
    }

    private void onClick(View view) {
       switch (view.getId()){
           case R.id.save_data:
               saveUserData(token,
                       firstName.getText().toString(),
                       lastName.getText().toString(),
                       age.getText().toString(),
                       jobTitle.getText().toString(),
                       aboutYourself.getText().toString(),
                       twitterLink.getText().toString(),
                       facebookLink.getText().toString());
               break;
           case R.id.edit_img_profile:
                selectImage();
               break;
           case  R.id.log_out_btn:
                logOut();
               break;
       }

    }

    private void logOut() {
        mPrefs.edit().putBoolean("isLogIn", false).apply();
        finish();
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
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
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

                File result = new File(getFilesDir(), uri.getLastPathSegment() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
                Log.d(TAG, "onActivityResult:  result" + result.getPath());
                os = new BufferedOutputStream(new FileOutputStream(result));

                byte[] buffer = new byte[4096];
                int count;

                while ((count = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                File file = new File(getFilesDir(), uri.getLastPathSegment() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
//                uploadFile(uri, file);
                updateImage(new File(getFilesDir(), result.getName()));
                os.flush();
                os.close();
                is.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateImage(File userImageFile){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), userImageFile);
        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("file", userImageFile.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);
        Call<User> updateImage = service.updateUserImage(token, requestImg);
        updateImage.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User object = response.body();
                Log.d(TAG, "onResponse: " + response.body());

                Toast.makeText(EditUserProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
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
                        finish();
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



    private void rxRequest(){
        Observable.just((getUserInfo()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }
    private String getUserInfo(){
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
                userPictureId = userPayload.getPictureId();
                firstName.setText(userPayload.getFirstName());
                lastName.setText(userPayload.getLastName());
                age.setText(userPayload.getAge());
                jobTitle.setText(userPayload.getJobTitle());
                email.setText(userPayload.getEmail());
                aboutYourself.setText(userPayload.getDesctiption());
                Glide.with(EditUserProfileActivity.this).load(Constants.BASIC_URL + "public_api/account/get_picture?picture_id="+userPictureId).into(editUserImg);
            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.d(TAG, "onResponse: signUp fail " + t.getStackTrace());


            }
        });
        return userPictureId;
    }
    private Boolean getUserImage(String userPictureId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);
        Call<ResponseBody> call = service.getUserImage(userPictureId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: url " + call.request().url());
                writeResponseBodyToDisk(response.body());
                Log.d(TAG, "onResponse: " + response.body());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d(TAG, "onResponse: signUp fail " + t.getStackTrace());


            }
        });
        return null;
    }
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getFilesDir() + File.separator + userPictureId);
            Log.d(TAG, "writeResponseBodyToDisk: " + userPictureId);


            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize + " file path" + getFilesDir());
                }
                Bitmap bitmap = BitmapFactory.decodeFile(futureStudioIconFile.getPath());
                editUserImg.setImageBitmap(bitmap);
                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASIC_URL) // Адрес сервера
                    .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                    .build();

            IServer service = retrofit.create(IServer.class);
            Call<ResponseBody> call = service.getUserImage(userPictureId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: url " + call.request().url());
                    writeResponseBodyToDisk(response.body());
                    Log.d(TAG, "onResponse: " + response.body());

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.d(TAG, "onResponse: signUp fail " + t.getStackTrace());


                }
            });
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            editUserImg.setImageBitmap(result);
        }
    }
}

