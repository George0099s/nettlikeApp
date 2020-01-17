package com.avla.app.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String first_name;
    private String last_name;
    private String age;
    private String patronymic_name;
    private String phone;
    private String gender;
    private String lang;
    private String country;
    private String city;
    public User(Context context) {

    }

    public User() {

    }

    protected User(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        age = in.readString();
        patronymic_name = in.readString();
        phone = in.readString();
        gender = in.readString();
        lang = in.readString();
        country = in.readString();
        city = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPatronymic_name() {
        return patronymic_name;
    }

    public void setPatronymic_name(String patronymic_name) {
        this.patronymic_name = patronymic_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(patronymic_name);
        dest.writeString(phone);
        dest.writeString(gender);
        dest.writeString(lang);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
