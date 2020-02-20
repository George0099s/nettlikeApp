package com.avla.app.view.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
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

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.adapter.UserTagAdapter;
import com.avla.app.model.ModelPost;
import com.avla.app.model.PostPayload;
import com.avla.app.model.UserSingleton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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
import rx.schedulers.Schedulers;

public class CreatePostActivity extends AppCompatActivity {
    private static final String TAG = "CreatePostActivity";
    private static final int IMAGE_REQUEAST = 1;
    private EditText postName, postDescription;
    private Button createPost;
    private JSONArray postTagId = new JSONArray();
    private ArrayList<String> postTagid = new ArrayList<>();
    private ArrayList<String> postTagName = new ArrayList<>();
    private ImageView addPostPhoto;
    private String token;
    private TextView addTags;
    private RecyclerView tagRecycler;
    private UserTagAdapter userTagAdapter;
    private UserSingleton user = UserSingleton.INSTANCE;
    private File postImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initView();
    }

    private void initView() {
        addPostPhoto = findViewById(R.id.add_post_add_photo);
        addPostPhoto.setOnClickListener(this::onclick);
        addTags = findViewById(R.id.add_post_tags);
        addTags.setOnClickListener(this::onclick);
        token = getIntent().getStringExtra("token");
        postName = findViewById(R.id.add_post_name);
        postDescription = findViewById(R.id.add_post_description);
        tagRecycler = findViewById(R.id.add_post_recycler);
        tagRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tagRecycler.setHasFixedSize(true);
        createPost = findViewById(R.id.add_post_create_post);
        createPost.setOnClickListener(this::onclick);
    }

    private void onclick(View view) {
        switch (view.getId()){
            case R.id.add_post_create_post:
                Observable.just(sendPostInfo(postName.getText().toString(), postDescription.getText().toString()))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                finish();
                break;
            case R.id.add_post_tags:
                Intent intent = new Intent(this, GetTagsActivity.class);
                intent.putExtra("token", token);
                startActivityForResult(intent, 42);
                break;
            case R.id.add_post_add_photo:
                requestStoragePermission();
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(intent2.ACTION_PICK);
                startActivityForResult(intent2, IMAGE_REQUEAST);
                break;
        }
    }


    private Boolean sendPostInfo(String postName, String postDescription) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        postTagId,
        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> updateImage = service.sendPostInfo(token, postName,postTagId, postDescription);
        updateImage.enqueue(new Callback<ModelPost>() {

            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {
                ModelPost modelPost = response.body();
                PostPayload postPayload = modelPost.getPayload();
                sendPostImage(postPayload.getPost().getId(), postImage);
            }

            @Override
            public void onFailure(Call<ModelPost> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
        return true;
    }
    private Boolean sendPostImage(String id, File eventPhoto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), eventPhoto);
        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("image", eventPhoto.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> updateImage = service.updatePostImage(id, token, requestImg);
        updateImage.enqueue(new Callback<ModelPost>() {

            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {


            }

            @Override
            public void onFailure(Call<ModelPost> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 42  || requestCode == 42) {
           postTagName = data.getStringArrayListExtra("tags_name");
           postTagid = data.getStringArrayListExtra("tags_id");
            JSONArray tagNames = new JSONArray();

            for (int i = 0; i < postTagName.size(); i++) {
                tagNames.put(postTagName.get(i));
            }
            for (int i = 0; i < postTagid.size(); i++) {
                postTagId.put(postTagid.get(i));
            }
            userTagAdapter = new UserTagAdapter(tagNames, this);
            tagRecycler.setAdapter(userTagAdapter);
        }
        if (requestCode == IMAGE_REQUEAST && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            OutputStream os = null;
            try{
                InputStream is = getContentResolver().openInputStream(uri);
                String type = getContentResolver().getType(uri);

                File file = new File(getFilesDir(), user.getUserId() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
                os = new BufferedOutputStream(new FileOutputStream(file));
                Bitmap pictureBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri); // obtaining the Bitmap
                int dimensionInPixel = 300;
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                addPostPhoto.getLayoutParams().height = dimensionInDp;
                addPostPhoto.getLayoutParams().width = dimensionInDp;
                addPostPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                addPostPhoto.setBackground(null);
                addPostPhoto.setImageBitmap(pictureBitmap);
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG,50 , os); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                this.postImage = file;
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        
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
                            Toast.makeText(CreatePostActivity.this, "You cant add picture without permission", Toast.LENGTH_SHORT).show();

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
}
