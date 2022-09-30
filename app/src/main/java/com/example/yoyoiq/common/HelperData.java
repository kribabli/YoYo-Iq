package com.example.yoyoiq.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.yoyoiq.CreateTeamActivity;
import com.example.yoyoiq.KYC.KycAddedPostResponse;
import com.example.yoyoiq.Model.AllSelectedPlayer;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.UpComingMatchPOJO.ShortSquadsUploadingPojoClass;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperData {
    public static MutableLiveData<Integer> playerCounter = new MutableLiveData<>(0);
    public static String type_selected = "Cricket";
    public static MutableLiveData<Integer> conty1 = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> TeamCount = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> conty2 = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> wk = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> bat = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> ar = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> bowl = new MutableLiveData<>(0);
    public static MutableLiveData<Integer> myTeam = new MutableLiveData<>(0);
    public static MutableLiveData<String> Selectedcap = new MutableLiveData<>("");
    public static MutableLiveData<String> selectedVcap = new MutableLiveData<>("");
    public static MutableLiveData<Double> creditCounter = new MutableLiveData<>(0.0);
    public static MutableLiveData<List<AllSelectedPlayer>> allSelectedPlayer = new MutableLiveData<>();
    public static MutableLiveData<Integer> selectSingleTeamCounter = new MutableLiveData<>(0);

    public static ArrayList<AllSelectedPlayer> myTeamList = new ArrayList<>();
    public static ArrayList<ShortSquadsUploadingPojoClass> myCountyPlayer = new ArrayList<>();
    public static String team1NameShort = "";
    public static String team2NameShort = "";
    public static boolean teamEdt = false;
    public static int limit = 11;
    public static boolean vcap = false;
    public static boolean cap = false;
    public static String matchId;
    public static String contestId;
    public static String UserId = "";
    public static String UserName = "";
    public static String referral_code = "";
    public static String Usermobile = "";
    public static String UserEmail = "";
    public static String logoUrlTeamA = "";
    public static String logoUrlTeamB = "";
    public static String MatchStartTime = "";
    public static String MatchEndTime = "";

    public static void newTeamMaking() {
        myTeamList.clear();
        wk.setValue(0);
        playerCounter.setValue(0);
        bat.setValue(0);
        ar.setValue(0);
        bowl.setValue(0);
        conty1.setValue(0);
        conty2.setValue(0);
        Selectedcap.setValue("");
        selectedVcap.setValue("");
        creditCounter.setValue(100.0);
        HelperData.teamEdt = false;
        CreateTeamActivity.CreatedTeamId = "";
        vcap = false;
        cap = false;
        CreateTeamActivity.addedPlayerIds = "";
    }

    public static void uploadFile(Context context, String user_Id, String fullName, String accountNo, String ifsc, String bankName, String date_of_birth, String address_ed, String aadhar, String pan, String pan_img_path,String adhar_image_path) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setTitle("Please wait..");

        MultipartBody.Part fileToUpload1 = null;
        File myFile1 = new File(pan_img_path);
        Log.d("Amit","Value PanImage  "+pan_img_path);

        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), myFile1);
        fileToUpload1 = MultipartBody.Part.createFormData("pancard", myFile1.getName(), requestBody1);

        MultipartBody.Part fileToUpload2 = null;
        File myFile2 = new File(adhar_image_path);
        Log.d("Amit","adhar "+adhar_image_path);

        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), myFile2);
        fileToUpload2 = MultipartBody.Part.createFormData("adhar", myFile2.getName(), requestBody2);



        RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), user_Id);
        RequestBody full_name = RequestBody.create(MediaType.parse("multipart/form-data"), fullName);
        RequestBody account_no = RequestBody.create(MediaType.parse("multipart/form-data"), accountNo);
        RequestBody ifsc_code = RequestBody.create(MediaType.parse("multipart/form-data"), ifsc);
        RequestBody bank_name = RequestBody.create(MediaType.parse("multipart/form-data"), bankName);
        RequestBody aadhar_no = RequestBody.create(MediaType.parse("multipart/form-data"), aadhar);
        RequestBody pancard_no = RequestBody.create(MediaType.parse("multipart/form-data"), pan);
        RequestBody panImage = RequestBody.create(MediaType.parse("multipart/form-data"), pan_img_path);
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), address_ed);
        RequestBody DOB = RequestBody.create(MediaType.parse("multipart/form-data"), date_of_birth);

        Call<KycAddedPostResponse> call = ApiClient.getInstance().getApi().sendKycDetailsOnServer(user_id, full_name, account_no,
                ifsc_code, bank_name, DOB, address, aadhar_no, pancard_no, fileToUpload1,fileToUpload2);

        call.enqueue(new Callback<KycAddedPostResponse>() {
            @Override
            public void onResponse(Call<KycAddedPostResponse> call, Response<KycAddedPostResponse> response) {
                KycAddedPostResponse kycAddedPostResponse = response.body();
                if (response.isSuccessful()) {
                    String data = new Gson().toJson(kycAddedPostResponse.getResponse());
                    Log.d("Amit","Value11 "+data);
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<KycAddedPostResponse> call, Throwable t) {
                Log.d("Amit","Value111 "+t);
                progressDialog.dismiss();


            }
        });
    }
}
