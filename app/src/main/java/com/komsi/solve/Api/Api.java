package com.komsi.solve.Api;

import com.komsi.solve.Model.ResponseBanner;
import com.komsi.solve.Model.ResponseCategory;
import com.komsi.solve.Model.ResponseDetails;
import com.komsi.solve.Model.ResponseForgotPassword;
import com.komsi.solve.Model.ResponseLeaderboard;
import com.komsi.solve.Model.ResponseLogin;
import com.komsi.solve.Model.ResponsePassword;
import com.komsi.solve.Model.ResponseProfile;
import com.komsi.solve.Model.ResponseQuestion;
import com.komsi.solve.Model.ResponseListSoal;
import com.komsi.solve.Model.ResponseMenuHome;
import com.komsi.solve.Model.ResponseTypeList;
import com.komsi.solve.Model.ResponseSignUp;
import com.komsi.solve.Model.ResponseVersion;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @FormUrlEncoded
    @POST("collager/register")
    Call<ResponseSignUp> registerUser(
            @Header("Accept") String accept,
            @Field("name") String name,
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("collager/login")
    Call<ResponseLogin> loginUser(
            @Field("email") String email,
            @Field("password") String password);


    @GET("collager/question/{id}")
    Call<ResponseQuestion> question(
            @Header("Accept") String accept,
            @Header("Authorization") String token,
            @Path("id") int idSoal
    );




    @GET("collager/detail")
    Call<ResponseDetails> detail(
            @Header("Accept") String accept,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @PUT("collager/update")
    Call<ResponseProfile> updateProfil(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("username") String username

    );

    @FormUrlEncoded
    @PUT("collager/upload-avatar")
    Call<ResponseProfile> updateAvatar(
            @Header("Authorization") String token,
            @Field("picture") String picture
    );

    @FormUrlEncoded
    @POST("collager/quiz/store")
    Call<ResponseBody> storeQuiz(
            @Header("Authorization") String token,
            @Field("quiz_id") int quidId,
            @Field("total_score") int score
    );

    @FormUrlEncoded
    @POST("collager/forgot-password")
    Call<ResponseForgotPassword> resetEmail(
            @Header("Accept") String accept,
            @Field("email") String email
    );

    @FormUrlEncoded
    @PUT("collager/update-password")
    Call<ResponsePassword> updatePass(
            @Header("Authorization") String token,
            @Field("password_current") String password_current,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );


    @GET("collager/leaderboard-podium/{quiz_id}")
    Call<ResponseLeaderboard> getPodium(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Path("quiz_id") int id

    );

    @GET("collager/leaderboard-not-podium/{quiz_id}")
    Call<ResponseLeaderboard> getnonPodium(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Path("quiz_id") int id

    );

    @GET("collager/banner")
    Call<ResponseBanner> banner(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @GET("collager/quiztype/{category_id}")
    Call<ResponseCategory> categoryList(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Path("category_id") int idQuiz
    );
    @GET("collager/quiztype/{category_id}")
    Call<ResponseCategory> typeList(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Path("category_id") int idQuiz
    );
    @GET("collager/quiz/{category_id}")
    Call<ResponseTypeList> quiz(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Path("category_id") int idQuiz
    );

    @GET("collager/question/code/{code_quiz}")
    Call<ResponseTypeList> code(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Path("code_quiz") String code_quiz
    );

    @GET("collager/category")
    Call<ResponseMenuHome> category(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @GET("collager/logout")
    Call<ResponseBody> logout(
            @Header("Authorization") String token
    );

    @GET("collager/quiz/{quiztype_id}")
    Call<ResponseListSoal> quizList(
            @Header("Accept") String accept,
            @Header("Authorization") String token,
            @Path("quiztype_id") int idQuiz
    );


    @GET("collager/version")
    Call<ResponseVersion> version(
            @Header("Accept") String accept,
            @Header("Authorization") String token
    );


}