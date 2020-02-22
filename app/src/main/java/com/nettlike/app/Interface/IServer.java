package com.nettlike.app.Interface;

import com.nettlike.app.model.EmailPojo;
import com.nettlike.app.model.FollowerModel;
import com.nettlike.app.model.MessageModel;
import com.nettlike.app.model.ModelAddPostImage;
import com.nettlike.app.model.ModelEvent;
import com.nettlike.app.model.ModelLocation;
import com.nettlike.app.model.ModelPost;
import com.nettlike.app.model.ModelPostComms;
import com.nettlike.app.model.ModelTag;
import com.nettlike.app.model.Payload;
import com.nettlike.app.model.Token;
import com.nettlike.app.model.UserModel;
import com.nettlike.app.model.dialog.DiaModel;
import com.nettlike.app.model.dialog.DialogModel;
import com.nettlike.app.model.people.PeopleModel;

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

    @GET("public_api/auth/delete_token")
    Call<Payload> deleteToken(@Query("token") String token);

    @POST("public_api/auth/create_token")
    Call<Payload> createToken();

    @GET("public_api/dialogs/fetch")
    Call<DiaModel> getAllDialogs(@Query("token") String token);

    @GET("public_api/dialogs/{dialog_id}/fetch")
    Call<MessageModel> getAllMessages(@Path("dialog_id")String dialogId,
                                      @Query("limit") int limit,
                                      @Query("offset") int offset,
                                      @Query("token") String token);

    @GET("public_api/auth/countries")
    Call<ModelLocation> getAllCountries(@Query("token") String token);

    @GET("public_api/auth/countries/{country_id}/cities")
    Call<ModelLocation> getAllCities(@Path("country_id") String country_id,@Query("token") String token);

    @GET("public_api/tags/discover")
    Call<ModelTag> getAllTags(@Query("token") String token);

    @FormUrlEncoded
    @POST("public_api/account/update")
    Call<UserModel> sendRegistartionKey(@Query("token") String token,
                                        @Field("android_registration_key") String registrationKey);
    @FormUrlEncoded
    @POST("public_api/account/update")
    Call<UserModel> sendUserData(@Query("token") String token,
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
    Call<UserModel> saveUserData(@Query("token") String token,
                                 @Field("first_name") String firstName,
                                 @Field("last_name") String lastName,
                                 @Field("birth_year") String age,
                                 @Field("job_title") String jobTitle,
                                 @Field("description") String aboutYourself,
                                 @Field("country") String country,
                                 @Field("city") String city,
                                 @Field("tags") JSONArray tags,
                                 @Field("twitter_url") String twitterLink,
                                 @Field("facebook_url") String facebookLink);

    @GET("public_api/account/info")
    Call<UserModel> getUserInfo(@Query("token") String token);

    @GET("public_api/activities/fetch")
    Call<com.nettlike.app.model.activity.Model> getActivity(@Query("token") String token,
                                                            @Query("offset") int offset,
                                                            @Query("limit") int limit);

    @GET("public_api/people/{account_id}/info")
    Call<com.nettlike.app.model.Model> getAnotherUserInfo(@Path("account_id") String accountId,
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


    @POST("public_api/dialogs/{dialog_id}/see_all")
    Call<MessageModel> markDialogAsSeen(@Path("dialog_id")String dialogId,
                                        @Query("token") String token);


    @GET("public_api/events/discover")
    Call<ModelEvent> getEvent(@Query("token") String token,
                              @Query("tags") JSONArray tags,
                              @Query("offset") int offset,
                              @Query("query") String query,
                              @Query("token")  int limit);

    @Multipart
    @POST("public_api/events/{event_id}/add_image")
    Call<ModelEvent> updateEventImage(
            @Path("event_id")String eventId,
            @Query("token") String token,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("public_api/posts/{post_id}/add_image")
    Call<ModelAddPostImage> updatePostImage(
            @Path("post_id")String postId,
            @Query("token") String token,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("public_api/account/upload_account_picture")
    Call<UserModel> updateUserImage(
            @Query("token") String token,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/public_api/events/create")
    Call<ModelEvent> sendEventInfo(
            @Query("token") String token,
            @Field("name") String eventName,
            @Field("description") String eventDescription,
            @Field("start_date") String eventDate,
            @Field("start_time") String eventTime);

    @FormUrlEncoded
    @POST("/public_api/posts/create")
    Call<ModelPost> sendPostInfo(
            @Query("token") String token,
            @Field("name") String eventName,
            @Field("tags") JSONArray tags,
            @Field("description") String eventDescription);
//    @FormUrlEncoded
//    @POST("/public_api/events/create")
//    Call<UserModel> sendEventInfo(
//            @Query("token") String token,
//            @Field("name") String eventName,
//            @Field("description") String eventDescription,
//            @Field("start_date") String eventDate,
//            @Field("start_time") String eventTime,
//            @Part MultipartBody.Part file);
    @GET("public_api/account/get_picture")
    Call<ResponseBody> getUserImage(@Query("picture_id") String userPictureId);

    @POST("public_api/people/{account_id}/save")
    Call<Token> saveDeleteUser(@Path("account_id")String accountId,
                               @Query("token") String token);

    @POST("public_api/posts/{post_id}/save")
    Call<Token> saveDeletePost(@Path("post_id")String postId,
                               @Query("token") String token);

    @POST("public_api/people/{account_id}/start_dialog")
    Call<DialogModel> startDia(@Path("account_id")String accountId,
                               @Query("token") String token);

    @GET("public_api/posts/discover")
    Call<ModelPost> getPosts(@Query("token") String token,
                             @Query("tags") JSONArray tags,
                             @Query("category") String category,
                             @Query("offset") int offset,
                             @Query("limit") int limit,
                             @Query("query") String query);
    @FormUrlEncoded
    @POST("public_api/posts/{post_id}/comment")
    Call<ModelPostComms> sendComment(@Path("post_id")String postId,
                                     @Query("token") String token,
                                     @Field("name") String name,
                                     @Field("description") String description);


    @GET("public_api/posts/{post_id}/info")
    Call<ModelPostComms> getPostInfo(@Path("post_id")String postId,
                                     @Query("token") String token);


    @POST("public_api/posts/{post_id}/delete_or_hide")
    Call<ModelPost> hideDeletePost(@Path("post_id")String postId,
                                   @Query("token") String token);

    @POST("public_api/events/{event_id}/delete")
    Call<ModelEvent> deleteEvent(@Path("event_id")String postId,
                                 @Query("token") String token);

     @POST("public_api/dialogs/{dialog_id}/delete")
    Call<DialogModel> deleteDialog(@Path("dialog_id")String dialogId,

                                   @Query("token") String token);

    @GET("public_api/people/discover")
    Call<PeopleModel> getAllPeople(@Query("category") String category,
                                   @Query("tags") ArrayList<String> tags,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit,
                                   @Query("query") String query,
                                   @Query("token") String token);

    @GET("public_api/people/{account_id}/followers")
    Call<FollowerModel> getFollowers(@Path("account_id") String accountId,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit,
                                     @Query("token") String token);

    @GET("public_api/people/{account_id}/following")
    Call<FollowerModel> getFollowing(@Path("account_id") String accountId,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit,
                                     @Query("token") String token);

}
