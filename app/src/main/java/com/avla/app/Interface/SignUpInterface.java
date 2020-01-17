package com.avla.app.Interface;

import android.os.Parcelable;

import java.util.HashMap;

public interface SignUpInterface extends Parcelable {
    HashMap<String, String> getUser();
    void saveFirstName(String firstName);
    void saveLastName(String lastName);
    void saveAge(String age);
    void saveAbout(String aboutYourself);
    void saveCountry(String country);
    void saveCity(String city);
}
