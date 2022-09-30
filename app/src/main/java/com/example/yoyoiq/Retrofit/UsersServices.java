package com.example.yoyoiq.Retrofit;

import com.example.yoyoiq.BannerPOJO.Banner;
import com.example.yoyoiq.ContestPOJO.Contests;
import com.example.yoyoiq.CreatedTeamPOJO.CreatedTeamResponse;
import com.example.yoyoiq.JoinContest.JoinContestsResponse;
import com.example.yoyoiq.KYC.KycAddedPostResponse;
import com.example.yoyoiq.KYC.ViewKycResponse;
import com.example.yoyoiq.LoginPojo.LoginResponse;
import com.example.yoyoiq.LoginPojo.RegistrationResponse;
import com.example.yoyoiq.Model.ReferCode;
import com.example.yoyoiq.OnlyMyContestPOJO.MyContest1;
import com.example.yoyoiq.PlayerPOJO.ResponsePlayer;
import com.example.yoyoiq.PlayerPOJO.UpdatedTeamResponse;
import com.example.yoyoiq.UpcommingReq.UpcommingResponse;
import com.example.yoyoiq.UpdatePassword.UpdatePassword;
import com.example.yoyoiq.WalletPackage.ContestJoinResponse;
import com.example.yoyoiq.WalletPackage.PostBalanceResponse;
import com.example.yoyoiq.WalletPackage.ViewBalanceResponse;
import com.example.yoyoiq.WalletPackage.ViewTransactionHistoryResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UsersServices {

    @GET("upcomingMatchesList")
    Call<UpcommingResponse> getMatch(
    );

    @GET("banner")
    Call<Banner> getBanner(
    );

    @FormUrlEncoded
    @POST("matchPlaying11detail")
    Call<ResponsePlayer> getMatchPlaying11(
            @Field("match_id") String match_id
    );

    @FormUrlEncoded
    @POST("contestList")
    Call<Contests> getContestsList(
            @Field("match_id") String match_id
    );

    @FormUrlEncoded
    @POST("userTeam")
    Call<JSONObject> Send_myteam_list_Server(
            @Field("user_id") String User_id,
            @Field("match_id") String match_id,
            @Field("squads") String squads,
            @Field("short_squads") String short_squads
//            @Body SendCreatedTeamServer sendCreatedTeamServer
    );

    @FormUrlEncoded
    @POST("updateuserTeam")
    Call<UpdatedTeamResponse> updateTeamOnServer(
            @Field("id") String id,
            @Field("user_id") String User_id,
            @Field("match_id") String match_id,
            @Field("squads") String squads,
            @Field("short_squads") String short_squads
//            @Body SendCreatedTeamServer sendCreatedTeamServer
    );

    @FormUrlEncoded
    @POST("userTeamList")
    Call<CreatedTeamResponse> getUserTeamCreated(
            @Field("user_id") String user_id,
            @Field("match_id") String match_id
    );

    @FormUrlEncoded
    @POST("registration")
    Call<RegistrationResponse> SendUserDetails_server(
            @Field("mobile") String mobile_no,
            @Field("name") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> getUserLoginData(
            @Field("email_or_mobile") String mobile_no,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("reffer")
    Call<ReferCode> getReferCode(
            @Field("referral_code") String referral_code
    );

    @FormUrlEncoded
    @POST("changepasword")
    Call<UpdatePassword> upDatePassword(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("viewbalance")
    Call<ViewBalanceResponse> getBalanceDetails(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("viewkycdetail")
    Call<ViewKycResponse> getkycDetails(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("viewtransection")
    Call<ViewTransactionHistoryResponse> getTransactionDetails(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("myContestMatchesList")
    Call<UpcommingResponse> getContestMatchesList(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("liveContestMatchesList")
    Call<UpcommingResponse> getLiveContestMatchesList(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("myjoincontest")
    Call<MyContest1> getJoinContestList(
            @Field("user_id") String user_id,
            @Field("match_id") String match_id
    );

    @FormUrlEncoded
    @POST("addbalance")
    Call<PostBalanceResponse> sendBalanceData(
            @Field("user_id") String user_id,
            @Field("balance") String balance,
            @Field("transection_id") String transaction_id
    );

    @FormUrlEncoded
    @POST("joinContest")
    Call<JoinContestsResponse> getJoinContestResponse(
            @Field("user_id") String user_id,
            @Field("match_id") String match_id,
            @Field("contest_id") String contest_id,
            @Field("team_id") String team_id
    );

    @Multipart
    @POST("addkycdetail")
    Call<KycAddedPostResponse> sendKycDetailsOnServer(
            @Part("user_id") RequestBody userid,
            @Part("fullname") RequestBody fullName,
            @Part("account_no") RequestBody accountNo,
            @Part("ifsc_code") RequestBody ifsc,
            @Part("bank_name") RequestBody bankName,
            @Part("dob") RequestBody dob,
            @Part("address") RequestBody address,
            @Part("adhar_no") RequestBody adhar_no,
            @Part("pancard_no") RequestBody pan,
            @Part MultipartBody.Part pancard,
            @Part MultipartBody.Part adhar
    );

    @FormUrlEncoded
    @POST("contestJoinFee")
    Call<ContestJoinResponse> requestToJoinFee(
            @Field("user_id") String user_id,
            @Field("contest_id") String match_id,
            @Field("amount") String contest_id
    );
}