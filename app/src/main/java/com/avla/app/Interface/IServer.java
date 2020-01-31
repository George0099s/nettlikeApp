package com.avla.app.Interface;

import com.avla.app.model.EmailPojo;
import com.avla.app.model.MessageModel;
import com.avla.app.model.Model;
import com.avla.app.model.ModelLocation;
import com.avla.app.model.ModelTag;
import com.avla.app.model.Payload;
import com.avla.app.model.Token;
import com.avla.app.model.User;
import com.avla.app.model.dialog.DiaModel;
import com.avla.app.model.people.PeopleModel;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("public_api/dialogs/fetch")
    Call<DiaModel> getAllDialogs(@Query("token") String token);

    @GET("public_api/dialogs/{dialog_id}/fetch?offset=0&limit=25")
    Call<MessageModel> getAllMessages(@Path("dialog_id")String dialogId,
                                      @Query("token") String token);

    @GET("public_api/auth/countries")
    Call<ModelLocation> getAllCountries(@Query("token") String token);

    @GET("public_api/auth/countries/{country_id}/cities")
    Call<ModelLocation> getAllCities(@Path("country_id") String country_id,@Query("token") String token);

    @GET("public_api/tags/discover")
    Call<ModelTag> getAllTags(@Query("token") String token);


    @FormUrlEncoded
    @POST("public_api/account/update")
    Call<User> sendUserData(@Query("token") String token,
                            @Field("first_name") String firstName,
                            @Field("last_name") String lastName,
                            @Field("birth_year") String age,
                            @Field("description") String aboutYourself,
                            @Field("country") String country,
                            @Field("city") String city,
                            @Field("job_title") String jobTitle,
                            @Field("tags") JSONArray tagList);

    @FormUrlEncoded
    @POST("public_api/account/update")
    Call<User> saveUserData(@Query("token") String token,
                            @Field("first_name") String firstName,
                            @Field("last_name") String lastName,
                            @Field("birth_year") String age,
                            @Field("job_title") String jobTitle,
                            @Field("description") String aboutYourself,
                            @Field("twitter_url") String twitterLink,
                            @Field("facebook_url") String facebookLink);

    @GET("public_api/account/info")
    Call<User> getUserInfo(@Query("token") String token);

    @GET("public_api/people/{account_id}/info")
    Call<Model> getAnotherUserInfo(@Path("account_id") String accountId,
                                   @Query("token") String token);


    @POST("public_api/people/{account_id}/follow")
    Call<Token> followUnfollow(@Path("account_id")String accountId,
                               @Query("token") String token);

    @FormUrlEncoded
    @POST("public_api/dialogs/{dialog_id}/send")
    Call<MessageModel> sendMessage(@Path("dialog_id")String dialogId,
                            @Field("type") String typeText,
                            @Field("text") String messageBody,
                            @Query("token") String token);


    @Multipart
    @POST("public_api/account/upload_account_picture")
    Call<User> updateUserImage(
            @Query("token") String token,
            @Part MultipartBody.Part file);

    @GET("public_api/account/get_picture")
    Call<ResponseBody> getUserImage(@Query("picture_id") String userPictureId);

    @POST("public_api/people/{account_id}/save")
    Call<Token> saveDeleteUser(@Path("account_id")String accountId,
                               @Query("token") String token);

    @POST("public_api/people/{account_id}/start_dialog")
    Call<Token> startDia(@Path("account_id")String accountId,
                               @Query("token") String token);




    @GET("public_api/people/discover")
    Call<PeopleModel> getAllPeople(@Query("category") String category,
                                   @Query("tags") ArrayList<String> tags,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit,
                                   @Query("query") String query,
                                   @Query("token") String token);

}
