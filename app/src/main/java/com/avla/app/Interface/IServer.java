package com.avla.app.Interface;

import com.avla.app.Model.EmailValidate;
import com.avla.app.Model.ModelLocation;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IServer {
    @FormUrlEncoded
    @POST("public_api/auth/check_email_registered")
    Call<EmailValidate> checkIfEmailRegistered(@Query("token") String token, @Field("email") String email);

    @GET("public_api/auth/create_token")
    Call<String> createToken();


    @GET("public_api/auth/countries")
    Call<ModelLocation> getAllCountries(@Query("token") String token);

    @GET("public_api/auth/countries/{country_id}/cities")
    Call<ModelLocation> getAllCities(@Path("country_id") String country_id,@Query("token") String token);

    @FormUrlEncoded
    @POST("public_api/auth/sign_up_with_email")
    Call<EmailValidate> signUpWithEmail(@Query("token") String token, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("public_api/auth/log_in_with_email")
    Call<EmailValidate> loginWithEmail(@Query("token") String token, @Field("email") String email, @Field("password") String password);


}
