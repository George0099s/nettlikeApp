package com.avla.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Event implements Parcelable {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("youtube_url")
    @Expose
    private String youtubeUrl;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("first_button_text")
    @Expose
    private String firstButtonText;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_button_url")
    @Expose
    private String firstButtonUrl;
    @SerializedName("second_button_text")
    @Expose
    private String secondButtonText;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("second_button_url")
    @Expose
    private String secondButtonUrl;
    @SerializedName("photos")
    @Expose
    private ArrayList<String> photos = new ArrayList<>();
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_by")
    @Expose
    private CreatedBy createdBy;

    protected Event(Parcel in) {
        description = in.readString();
        id = in.readString();
        youtubeUrl = in.readString();
        address = in.readString();
        firstButtonText = in.readString();
        lang = in.readString();
        name = in.readString();
        firstButtonUrl = in.readString();
        secondButtonText = in.readString();
        status = in.readString();
        secondButtonUrl = in.readString();
        createdAt = in.readString();
        startDate = in.readString();
        startTime = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstButtonText() {
        return firstButtonText;
    }

    public void setFirstButtonText(String firstButtonText) {
        this.firstButtonText = firstButtonText;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstButtonUrl() {
        return firstButtonUrl;
    }

    public void setFirstButtonUrl(String firstButtonUrl) {
        this.firstButtonUrl = firstButtonUrl;
    }

    public String getSecondButtonText() {
        return secondButtonText;
    }

    public void setSecondButtonText(String secondButtonText) {
        this.secondButtonText = secondButtonText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecondButtonUrl() {
        return secondButtonUrl;
    }

    public void setSecondButtonUrl(String secondButtonUrl) {
        this.secondButtonUrl = secondButtonUrl;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            SimpleDateFormat newFormat = new SimpleDateFormat("d MMM", Locale.ENGLISH);
            newFormat.setTimeZone(TimeZone.getDefault());
            Log.d("AAA", "getCreatedAt: " + TimeZone.getDefault());
            if(startDate != null) {
                date = df.parse(startDate);
                this.startDate = newFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(youtubeUrl);
        dest.writeString(address);
        dest.writeString(firstButtonText);
        dest.writeString(lang);
        dest.writeString(name);
        dest.writeString(firstButtonUrl);
        dest.writeString(secondButtonText);
        dest.writeString(status);
        dest.writeString(secondButtonUrl);
        dest.writeString(createdAt);
        dest.writeString(startDate);
        dest.writeString(startTime);
        dest.writeString(updatedAt);
    }
}
