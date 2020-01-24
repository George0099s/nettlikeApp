package com.avla.app.fragments.Main;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Base64;
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
import com.avla.app.FileUtils;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.User;
import com.avla.app.model.UserPayload;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class EditUserProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditUserProfileActivity";
    private static final int IMAGE_REQUEAST = 1;
    private EditText firstName, lastName, age, jobTitle, aboutYourself, twitterLink, facebookLink;
    private String token;
    private TextView email;
    private Button saveData;
    private ImageView editUserImg;
    private static final Scheduler THREAD_2 = Schedulers.newThread();

    private SharedPreferences mPrefs;
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

        findViewById(R.id.log_out_btn).setOnClickListener(this::onClick);
        token = getIntent().getStringExtra("token");
        mPrefs = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.execute();
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEAST);
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

                File result = new File(getCacheDir(), uri.getLastPathSegment() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
                os = new BufferedOutputStream(new FileOutputStream(result));

                byte[] buffer = new byte[4096];
                int count;

                while ((count = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }

//                updateImage(result);
                new MultipartUploadRequest(getApplicationContext(), "ww", Constants.BASIC_URL + "public_api/account/upload_account_picture")
                        .addFileToUpload(result.toString(), "file")
                        .addParameter("name", "file")
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

                Log.d(TAG, "onActivityResult: " + result.toString());
//                uploadFile(Uri.parse(String.valueOf(result)));
                Log.d(TAG, "onActivityResult: nnn " + result);
                os.flush();
                os.close();
                is.close();

//                updateImage(os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {



            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

//            String url = "http://yourserver";
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
//                    "yourfile");
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//
//                HttpPost httppost = new HttpPost(url);
//
//                InputStreamEntity reqEntity = new InputStreamEntity(
//                        new FileInputStream(file), -1);
//                reqEntity.setContentType("binary/octet-stream");
//                reqEntity.setChunked(true); // Send in multiple parts if needed
//                httppost.setEntity(reqEntity);
//                HttpResponse response = httpclient.execute(httppost);
//                //Do something with response...
//
//            } catch (Exception e) {
//                // show error
//            }

//            Observable.just((getFile(uri)))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(THREAD_2)
//                    .subscribe();
//            OutputStream os = null;
//            try {
//                InputStream is = getContentResolver().openInputStream(uri);
//                String type = getContentResolver().getType(uri);
//
//                File result = new File(getCacheDir(), uri.getLastPathSegment() + "."
//                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
//                os = new BufferedOutputStream(new FileOutputStream(result));
//
//                byte[] buffer = new byte[4096];
//                int count;
//
//                while ((count = is.read(buffer)) >= 0) {
//                    os.write(buffer, 0, count);
//                }
//
////                updateImage(result);
//
//                Log.d(TAG, "onActivityResult: " + result.toString());
////                uploadFile(Uri.parse(String.valueOf(result)));
//                Log.d(TAG, "onActivityResult: nnn " + result);
//                os.flush();
//                os.close();
//                is.close();
//
////                updateImage(os);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    private String getFile(Uri uri){
        OutputStream os = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            String type = getContentResolver().getType(uri);

            File result = new File(getCacheDir(), uri.getLastPathSegment() + "."
                    + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
            os = new BufferedOutputStream(new FileOutputStream(result));

            byte[] buffer = new byte[4096];
            int count;

            while ((count = is.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    result.getName());

            updateImage(file);
            Log.d(TAG, "onActivityResult: " + file);
//                uploadFile(Uri.parse(String.valueOf(result)));
            Log.d(TAG, "onActivityResult: nnn " + result);
//            uploadFile(uri);
            os.flush();
            os.close();
            is.close();

//                updateImage(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private Boolean updateImage(File userImageFile) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        File file = new File(String.valueOf(userImageFile));
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);


        Call<User> updateImage = service.updateUserImage(token, userImageFile);
        updateImage.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: " + updateImage.request().url());
                User object = response.body();
                Log.d(TAG, "onResponse: " + response.message());
                Toast.makeText(EditUserProfileActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(EditUserProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
        int year = UserPayload.getCurrentYear();
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
    public void dumpImageMetaData(Uri uri) {
        String displayName;
        // The query, because it only applies to a single document, returns only
        // one row. There's no need to filter, sort, or select fields,
        // because we want all fields for one document.
        Cursor cursor =getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows. Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name". This is
                // provider-specific, and might not necessarily be the file name.
                 displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);
                File file = new File(displayName);
//                updateImage(Uri.parse(displayName)) ;
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null. But because an
                // int can't be null, the behavior is implementation-specific,
                // and unpredictable. So as
                // a rule, check if it's null before assigning to an int. This will
                // happen often: The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
            }
        } finally {
            cursor.close();
        }

    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void uploadFile(Uri fileUri) {
        // create upload service client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL) // Адрес сервера
                .addConverterFactory(GsonConverterFactory.create()) // говорим ретрофиту что для сериализации необходимо использовать GSON
                .build();

        IServer service = retrofit.create(IServer.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the com.avla.app.FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}

