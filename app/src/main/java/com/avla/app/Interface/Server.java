package com.avla.app.Interface;

import com.avla.app.Model.EmailValidate;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Server {
    @POST("public_api/auth/check_email_registered")
    Call<EmailValidate> addEmail(@Query("token") String token, @Body String email);
}
