package com.nettlike.app.view.main;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nettlike.app.Constants;
import com.nettlike.app.Interface.IServer;
import com.nettlike.app.R;
import com.nettlike.app.adapter.UserTagAdapter;
import com.nettlike.app.model.ModelAddPostImage;
import com.nettlike.app.model.ModelPost;
import com.nettlike.app.model.Post;
import com.nettlike.app.model.PostPayload;
import com.nettlike.app.model.UserSingleton;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContentFragment extends Fragment {
    private static final String TAG = "CreatePostActivity";
    private static final int IMAGE_REQUEAST = 1;
    private EditText postName, postDescription;
    private TextView createPost;
    private JSONArray postTagId = new JSONArray();
    private ArrayList<String> postTagIdArrayList = new ArrayList<>();
    private ArrayList<String> postTagName = new ArrayList<>();
    private ImageView addPostPhoto, removePhoto, postPhoto;
    private String token;
    private TextView addTags;
    private RecyclerView tagRecycler;
    private UserTagAdapter userTagAdapter;
    private UserSingleton user = UserSingleton.INSTANCE;
    private File postImage;
    private String postId;
    private Post post = null;
    public AddContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        addPostPhoto = view.findViewById(R.id.add_post_add_photo);
        addPostPhoto.setOnClickListener(this::onclick);
        removePhoto = view.findViewById(R.id.add_post_remove_photo);
        removePhoto.setOnClickListener(this::onclick);
        postPhoto = view.findViewById(R.id.add_post_photo);
        addTags = view.findViewById(R.id.add_post_tags);
        addTags.setOnClickListener(this::onclick);
        token = getActivity().getIntent().getStringExtra("token");
        postName = view.findViewById(R.id.add_post_name);
        postDescription = view.findViewById(R.id.add_post_description);
        tagRecycler = view.findViewById(R.id.add_post_recycler);
        tagRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tagRecycler.setHasFixedSize(true);
        createPost = view.findViewById(R.id.add_post_create_post);
        createPost.setOnClickListener(this::onclick);
    }

    private void onclick(View view) {
        switch (view.getId()){
            case R.id.add_post_create_post:
                if (postTagId.length() == 0 || postName.getText().toString().isEmpty() || postDescription.getText().toString().isEmpty()) {
                    if (postTagId.length() == 0) {
                        addTags.requestFocus();
                        addTags.setError("You need at least 1 tag");
                    }
                    if (postName.getText().toString().isEmpty()) {
                        postName.requestFocus();
                        postName.setError("The post title is missing");
                    }
                    if (postDescription.getText().toString().isEmpty()) {
                        postDescription.requestFocus();
                        postDescription.setError("The post content is missing");
                    }
                } else {

                    Observable.fromCallable(new CallableAddPost(postName.getText().toString(), postDescription.getText().toString()))
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                    postDescription.setText("");
                    postName.setText("");
                    postTagId = new JSONArray();
                    postTagIdArrayList = new ArrayList<>();
                }
                break;
            case R.id.add_post_tags:
                Intent intent = new Intent(getActivity(), GetTagsActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("tags_id", postTagIdArrayList);
                intent.putExtra("tags_name", postTagName);
                startActivityForResult(intent, 42);
                break;
            case R.id.add_post_add_photo:
                requestStoragePermission();
                break;
            case R.id.add_post_remove_photo:
                postPhoto.setVisibility(View.GONE);
                removePhoto.setVisibility(View.GONE);
                addPostPhoto.setVisibility(View.VISIBLE);
        }
    }


    private Boolean sendPostInfo(String postName, String postDescription) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        postTagId,
        IServer service = retrofit.create(IServer.class);
        Call<ModelPost> sendPostInfo= service.sendPostInfo(token, postName,postTagId, postDescription);
        sendPostInfo.enqueue(new Callback<ModelPost>() {

            @Override
            public void onResponse(Call<ModelPost> call, Response<ModelPost> response) {
                ModelPost modelPost = response.body();
                PostPayload postPayload = modelPost.getPayload();
                post = postPayload.getPost();
                if (post.getId() != null)
                postId = post.getId();
                else
                    postId = post.getPost().getId();
                if (postImage != null){
                Observable.fromCallable(new CallableAddPostImg(postId, postImage))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
//                    sendPostImage(postId, postImage);
                } else {
                    Intent intent = new Intent(getActivity(), PostInfoActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("post id", postId);
                    intent.putExtra("post", post);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<ModelPost> call, Throwable t) {
                Log.d(TAG, "onFailure:123 " + t.getMessage());

            }
        });
        return true;
    }
    private File sendPostImage(String id, File eventPhoto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), eventPhoto);
        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("image", eventPhoto.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);
        Call<ModelAddPostImage> updateImage = service.updatePostImage(id, token, requestImg);
        Log.d(TAG, "sendPostImage: "  + updateImage.request().url());
        updateImage.enqueue(new Callback<ModelAddPostImage>() {

            @Override
            public void onResponse(Call<ModelAddPostImage> call, Response<ModelAddPostImage> response) {
                ModelAddPostImage model = response.body();
                if (model != null)
                    if (model.getOk() && model.getPayload()) {
                        Intent intent = new Intent(getActivity(), PostInfoActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("post_id", postId);
                        intent.putExtra("post_picture_url", post.getPictureUrl());
                        intent.putExtra("post", post);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "There are some works on server", Toast.LENGTH_SHORT).show();
                    }
                else Toast.makeText(getContext(), "There are some works on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelAddPostImage> call, Throwable t) {
                Log.d(TAG, "onFailure: img " + t.getMessage());
            }
        });
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 42  || requestCode == 42) {
            postTagName = data != null ? data.getStringArrayListExtra("tags_name") : new ArrayList<>();
            postTagIdArrayList = data != null ? data.getStringArrayListExtra("tags_id") : new ArrayList<>();
            JSONArray tagNames = new JSONArray();

            for (int i = 0; i < postTagName.size(); i++) {
                tagNames.put(postTagName.get(i));
            }
            for (int i = 0; i < postTagIdArrayList.size(); i++) {
                postTagId.put(postTagIdArrayList.get(i));
            }
            userTagAdapter = new UserTagAdapter(tagNames, getActivity());
            tagRecycler.setAdapter(userTagAdapter);
        }
        if (requestCode == IMAGE_REQUEAST && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            OutputStream os = null;
            try{
                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                String type = getActivity().getContentResolver().getType(uri);

                File file = new File(getActivity().getFilesDir(), user.getUserId() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
                os = new BufferedOutputStream(new FileOutputStream(file));
                Bitmap pictureBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri); // obtaining the Bitmap
                int dimensionInPixel = 150;
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                addPostPhoto.setVisibility(View.GONE);
                removePhoto.setVisibility(View.VISIBLE);
                postPhoto.setVisibility(View.VISIBLE);
                postPhoto.getLayoutParams().height = dimensionInDp;
                postPhoto.getLayoutParams().width = dimensionInDp;
                postPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                postPhoto.setBackground(null);
                postPhoto.setImageBitmap(pictureBitmap);
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG,50 , os); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                postImage = file;
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    private void requestStoragePermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getActivity(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent();
                            intent2.setType("image/*");
                            intent2.setAction(intent2.ACTION_PICK);
                            startActivityForResult(intent2, IMAGE_REQUEAST);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getActivity(), "You cant add picture without permission", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private class CallableAddPost implements Callable<Boolean>{

        String postName;
        String postDescription;

        public CallableAddPost( String postName, String postDescription) {
            this.postName = postName;
            this.postDescription = postDescription;

        }

        @Override
        public Boolean call() throws Exception {
            return sendPostInfo(postName, postDescription);
        }
    }
    private class CallableAddPostImg implements Callable<File>{

        String postId;
        File postImage;

        public CallableAddPostImg(String postId, File postImage) {
            this.postId = postId;
            this.postImage = postImage;
        }
        @Override
        public File call() throws Exception {
            return sendPostImage(postId, postImage);
        }
    }

}
