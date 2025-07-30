package com.ivision.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    @FormUrlEncoded
    @POST
    Call<JsonObject> login(
            @Url String url,
            @Field("username") String username,
            @Field("password") String password,
            @Field("playerId") String playerId
    );

    @POST()
    Call<JsonObject> getList(
            @Url String url
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getResultList(
            @Url String url,
            @Field("sid") String sid
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getReviewList(
            @Url String url,
            @Field("sid") String sid
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getBannerList(
            @Url String url,
            @Field("gid") String userId
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("sid") String userId
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> getList(
            @Url String url,
            @Field("sid") String userId,
            @Field("sub_id") String sub_id
    );

    @FormUrlEncoded
    @POST()
    Call<JsonObject> admissionInquiry(
            @Url String url,
            @Field("fname") String fname,
            @Field("mname") String mname,
            @Field("lname") String lname,
            @Field("lst_study") String lst_study,
            @Field("admission") String admission,
            @Field("percentage") String percentage,
            @Field("mobile") String mobile
    );
}