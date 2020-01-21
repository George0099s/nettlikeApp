package com.avla.app.Interface;

import com.avla.app.Model.EmailPojo;
import com.avla.app.Model.ModelLocation;
import com.avla.app.Model.ModelTag;
import com.avla.app.Model.Payload;
import com.avla.app.Model.Token;
import com.avla.app.Model.User;

import org.json.JSONArray;

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
    Call<EmailPojo> checkIfEmailRegistered(@Query("token") String token, @Field("email") String email);

    @FormUrlEncoded
    @POST("public_api/auth/sign_up_with_email")
    Call<EmailPojo> signUpWithEmail(@Query("token") String token, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("public_api/auth/log_in_with_email")
    Call<EmailPojo> loginWithEmail(@Query("token") String token, @Field("email") String email, @Field("password") String password);

    @GET("public_api/auth/validate_token")
    Call<Payload> validateToken(@Query("token") String token);

    @POST("public_api/auth/create_token")
    Call<Token> createToken();

    @POST("public_api/dialogs/fetch")
    Call<Token> getAllDialogs(@Query("token") String token);

    @GET("public_api/auth/countries")
    Call<ModelLocation> getAllCountries(@Query("token") String token);

    @GET("public_api/auth/countries/{country_id}/cities")
    Call<ModelLocation> getAllCities(@Path("country_id") String country_id,@Query("token") String token);

    @GET("public_api/tags/discover")
    Call<ModelTag> getAllTags(@Query("token") String token);


    @FormUrlEncoded
    @POST("public_api/account/update")
    Call<User> sendUserData(@Query("token") String token,
                            @Field("last_name") String firstName,
                            @Field("last_name") String lastName,
                            @Field("description") String aboutYourself,
                            @Field("job_title") String jobTitle,
                            @Field("tags") JSONArray tagList);


    @GET("public_api/account/info")
    Call<User> getUserInfo(@Query("token") String token);





}
