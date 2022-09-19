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

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
            @Field("mobile_no") String mobile_no,
            @Field("username") String username,
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

    @FormUrlEncoded
    @POST("addkycdetail")
    Call<KycAddedPostResponse> sendKycDetailsOnServer(
            @Field("user_id") String userid,
            @Field("fullname") String fullName,
            @Field("account_no") String accountNo,
            @Field("ifsc_code") String ifsc,
            @Field("bank_name") String bankName,
            @Field("dob") String dob,
            @Field("address") String address,
            @Field("adhar_no") String adhar_no,
            @Field("pancard_no") String pan,
            @Field("pancard") String pancard
    );

    @FormUrlEncoded
    @POST("contestJoinFee")
    Call<ContestJoinResponse> requestToJoinFee(
            @Field("user_id") String user_id,
            @Field("contest_id") String match_id,
            @Field("amount") String contest_id
    );
}