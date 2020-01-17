package id.technow.solve.Api;

import id.technow.solve.Model.ResponseBanner;
import id.technow.solve.Model.ResponseCategory;
import id.technow.solve.Model.ResponseDetails;
import id.technow.solve.Model.ResponseForgotPassword;
import id.technow.solve.Model.ResponseHistory;
import id.technow.solve.Model.ResponseHistoryDetail;
import id.technow.solve.Model.ResponseLeaderboard;
import id.technow.solve.Model.ResponseLogin;
import id.technow.solve.Model.ResponsePassword;
import id.technow.solve.Model.ResponsePostAnswer;
import id.technow.solve.Model.ResponseProfile;
import id.technow.solve.Model.ResponseQuestion;
import id.technow.solve.Model.ResponseListSoal;
import id.technow.solve.Model.ResponseMenuHome;
import id.technow.solve.Model.ResponseSchools;
import id.technow.solve.Model.ResponseTypeList;
import id.technow.solve.Model.ResponseSignUp;
import id.technow.solve.Model.ResponseVersion;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @FormUrlEncoded
    @POST("collager/register")
    Call<ResponseSignUp> registerUser(
            @Header("Accept") String accept,
            @Field("name") String name,
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password,
            @Field("school_id") String school_id,
            @Field("phone_number") String phone_number

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

    //@FormUrlEncoded
    //@Multipart
    @POST("collager/question/{id}")
    Call<ResponsePostAnswer> postQuestion(
            @Header("Accept") String accept,
            @Header("Authorization") String token,
            @Path("id") int idSoal,
            @Body ResponseQuestion file

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
            @Field("username") String username,
            @Field("school_id") String idSchool,
            @Field("phone_number") String phone_number

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
    );

    @GET("collager/history")
    Call<ResponseHistory> history(
            @Header("Accept") String accept,
            @Header("Authorization") String token
    );

    @GET("collager/history/{history_id}")
    Call<ResponseHistoryDetail> historyDetail(
            @Header("Accept") String accept,
            @Header("Authorization") String token,
            @Path("history_id") int id
    );

    @GET("collager/school")
    Call<ResponseSchools> schools(
            @Header("Accept") String accept,
            @Header("Authorization") String token
    );

    @GET("collager/school")
    Call<ResponseSchools> schools2(
            @Header("Accept") String accept,
            @Header("Authorization") String token,
            @Query("term") String term
    );


}
