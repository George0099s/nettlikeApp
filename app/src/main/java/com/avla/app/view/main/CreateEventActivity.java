package com.avla.app.view.main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.avla.app.Constants;
import com.avla.app.Interface.IServer;
import com.avla.app.R;
import com.avla.app.model.EventPayload;
import com.avla.app.model.ModelEvent;
import com.avla.app.model.UserSingleton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private static final String TAG = "CreateEventActivity";
    private static final int IMAGE_REQUEAST = 1;
    //    private TextView
    private EditText eventName, eventDescription, eventAddress;
    private String eventDate, eventTime, token;
    private TextView selectDate, selectTime;
    private int startYear, starthMonth, startDay;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ImageView eventPhoto;
    private Button createEvent;
    private File eventImageFile;
    private UserSingleton user = UserSingleton.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initView();
    }

    private void initView() {

        token = getIntent().getStringExtra("token");
        createEvent = findViewById(R.id.add_event_create_event);
        createEvent.setOnClickListener(this::onclick);
        eventName = findViewById(R.id.add_post_name);
        eventDescription = findViewById(R.id.add_event_description);
        eventAddress = findViewById(R.id.add_event_address);
        eventPhoto = findViewById(R.id.add_event_picture);
        eventPhoto.setOnClickListener(this::onclick);
        selectDate = findViewById(R.id.add_event_date);
        selectTime = findViewById(R.id.add_event_time);
        selectTime.setOnClickListener(this::onclick);
        selectDate.setOnClickListener(this::onclick);
        startYear = 2020;
        starthMonth = 1;
        startDay = 1;
        timePickerDialog = new TimePickerDialog(
                this,android.R.style.Theme_Holo_Light_Dialog, this::onTimeSet ,12, 0, true);
        datePickerDialog = new DatePickerDialog(
                this, android.R.style.Theme_Holo_Light_Dialog, CreateEventActivity.this, startYear, starthMonth, startDay);
    }

    private void onclick(View view) {
        switch (view.getId()){
            case R.id.add_event_date:
                datePickerDialog.show();
                break;

            case R.id.add_event_time:
                timePickerDialog.show();
                break;

            case R.id.add_event_picture:
                requestStoragePermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_PICK);
                startActivityForResult(intent, IMAGE_REQUEAST);
                break;
            case R.id.add_event_create_event:
                Observable.just(createEvent(
                        token,
                        eventName.getText().toString(),
                        eventDescription.getText().toString(),
                        eventDate,
                        eventTime))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                finish();
        }
    }

    private Boolean createEvent(String eventName, String eventDescription, String eventDate, String eventTime, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), eventPhoto);
//        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("file", eventPhoto.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);
        Call<ModelEvent> updateImage = service.sendEventInfo(eventName, eventDescription, eventDate, eventTime, token);
        updateImage.enqueue(new Callback<ModelEvent>() {

            @Override
            public void onResponse(Call<ModelEvent> call, Response<ModelEvent> response) {
                ModelEvent modelEvent = response.body();
                EventPayload eventPayload = modelEvent.getPayload();

                Observable.just(sendEvenImage(eventPayload.getId(), eventImageFile))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }

            @Override
            public void onFailure(Call<ModelEvent> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
        return true;
    }

    private Boolean sendEvenImage(String id, File eventPhoto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), eventPhoto);
        MultipartBody.Part requestImg = MultipartBody.Part.createFormData("image", eventPhoto.getName(), requestBody);
        IServer service = retrofit.create(IServer.class);
        Call<ModelEvent> updateImage = service.updateEventImage(id, token, requestImg);
        updateImage.enqueue(new Callback<ModelEvent>() {

            @Override
            public void onResponse(Call<ModelEvent> call, Response<ModelEvent> response) {
                ModelEvent modelEvent = response.body();
                EventPayload eventPayload = modelEvent.getPayload();
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<ModelEvent> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEAST && data != null
                && data.getData() != null) {
            Uri uri = data.getData();
            OutputStream os = null;

            try {
                InputStream is = getContentResolver().openInputStream(uri);
                String type = getContentResolver().getType(uri);

                 File file = new File(getFilesDir(), user.getUserId() + "."
                        + MimeTypeMap.getSingleton().getExtensionFromMimeType(type));
                os = new BufferedOutputStream(new FileOutputStream(file));

                Bitmap pictureBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri); // obtaining the Bitmap
                int dimensionInPixel = 300;
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                eventPhoto.getLayoutParams().height = dimensionInDp;
                eventPhoto.getLayoutParams().width = dimensionInDp;
                eventPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                eventPhoto.setBackground(null);
                eventPhoto.setImageBitmap(pictureBitmap);

                pictureBitmap.compress(Bitmap.CompressFormat.JPEG,50 , os); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                this.eventImageFile = file;
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        eventDate  = String.format("%02d-%02d-%02d",year , month, dayOfMonth);
        selectDate.setText(eventDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            eventTime = String.format("%02d:%02d", hourOfDay, minute);
            selectTime.setText(eventTime);
    }
}
