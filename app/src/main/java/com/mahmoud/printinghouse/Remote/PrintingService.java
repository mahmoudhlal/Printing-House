package com.mahmoud.printinghouse.Remote;

import com.mahmoud.printinghouse.models.SimpleResponse.SimpleResponse;
import com.mahmoud.printinghouse.models.authResponse.AuthResponse;
import com.mahmoud.printinghouse.models.messagesResponse.MessagesResponse;
import com.mahmoud.printinghouse.models.orderResponse.OrderResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PrintingService {

    @POST("register")
    Call<AuthResponse> register(
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("password") String password
    );


    @POST("login")
    Call<AuthResponse> Login(
            @Query("email") String email,
            @Query("password") String password
    );

    @Multipart
    @POST("add-order")
    Call<SimpleResponse> addGiftOrTshirt(
            @Header("Authorization") String Authorization,
            @Query("type") String type,
            @Query("copies") String copies,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("add-order")
    Call<SimpleResponse> addFile(
            @Header("Authorization") String Authorization,
            @Query("type") String type,
            @Query("copies") String copies,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("paper_size") String paper_size,
            @Query("paper_type") String paper_type,
            @Query("print_type") String print_type,
            @Part MultipartBody.Part image
    );

    @POST("add-order")
    Call<SimpleResponse> addShield(
            @Header("Authorization") String Authorization,
            @Query("type") String type,
            @Query("content") String content,
            @Query("shield_type") String shield_type,
            @Query("shield_image") String shield_image,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @POST("add-message")
    Call<SimpleResponse> sendMsg(
            @Header("Authorization") String Authorization,
            @Query("message") String message,
            @Query("user_id") String user_id,
            @Query("order_id") String order_id
    );

    @GET("orders")
    Call<OrderResponse> getOrders(
            @Header("Authorization") String Authorization,
            @Query("page") int page
    );

    @GET("messages")
    Call<MessagesResponse> getMessages(
            @Header("Authorization") String Authorization,
            @Query("page") int page
    );


/*
    @POST("login")
    Call<AuthResponse> Login(
            @Query("email") String email,
            @Query("password") String password,
            @Query("token") String fcm_token_android
    );

    @POST("logout")
    Call<StringResponse> logout(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("update-profile")
    Call<AuthResponse> UpdateProfile(
            @Header("Authorization") String token,
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("address") String address,
            @Query("city_id") Integer cityId,
            */
/*@Query("region") String region,*//*

            @Part MultipartBody.Part image
    );

    @POST("update-profile")
    Call<AuthResponse> UpdateProfile(
            @Header("Authorization") String token,
            @Query("name") String name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("address") String address,
            @Query("city_id") Integer cityId
    );


    @GET("complaint/index")
    Call<ComplaintsResponse> getComplaints(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Query("page") int page
    );

    @GET("search")
    Call<ComplaintsResponse> search(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Query("word") String word,
            @Query("page") int page
    );
    @GET("home")
    Call<HomeResponse> getHomeData(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization
    );

    @GET("surveys")
    Call<SurveysResponse> getSurveys(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization
    );


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("complaint/{id}")
    Call<SingleComplaintResponse> getComplainDetails(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int id
    );


    @POST("forget-password")
    Call<ForgetPasswordResponse> forget(
            @Query("phone") String phone
    );

    @POST("update-password")
    Call<AuthResponse> resetPassword(
            @Query("phone") String phone,
            @Query("password") String password
    );

    @GET("problems")
    Call<ProblemsResponse> getProblems(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization
    );

    @Multipart
    @POST("representative-approval/{id}")
    Call<StringResponse> sendComplainApproval(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int id,
            @Part MultipartBody.Part file
    );

    @POST("representative-approval/{id}")
    Call<StringResponse> sendComplainApproval(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int id
    );

    */
/**
     *
     * @param Authorization user token
     * @param localization language
     * @param complainId complain id
     * @param contacted 1 or 0
     * @param date date
     * @return String response
     *//*

    @POST("representative-accept/{id}")
    Call<StringResponse> acceptComplain(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int complainId,
            @Query("contact_with_owner") int contacted,
            @Query("date") String date
    );


    @POST("surveys/{id}")
    Call<StringResponse> addSurveys(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int complainId,
            @Query("surveys") String surveys
    );

    @Multipart
    @POST("problem/{id}")
    Call<AddProblemResponse> addProblem(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int complainId,
            @Path("problem_id") int problemId,
            @Query("quantity") int quantity,
            @Part MultipartBody.Part[] attach
    );

    @POST("problem/{id}")
    Call<AddProblemResponse> addProblem(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int complainId,
            @Path("problem_id") int problemId,
            @Query("quantity") int quantity
    );


    @DELETE("cancel-complaint/{id}")
    Call<StringResponse> deleteComplaint(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization,
            @Path("id") int complainId
    );


    @GET("notifications-count")
    Call<NotificationsCountResponse> getNotificationsCount(
            @Header("Authorization") String Authorization,
            @Header("X-localization") String localization
    );
*/

}
