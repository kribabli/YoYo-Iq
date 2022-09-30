package com.example.yoyoiq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.yoyoiq.KYC.KYCActivity;
import com.example.yoyoiq.KYC.ViewKycResponse;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.example.yoyoiq.common.SharedPrefManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.util.FileUriUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    TextView backPress, skillScore, share;
   SessionManager sessionManager;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CircleImageView userProfile;
    ImageView profileChange;
    String code="",profileImagePath=" ";
    boolean status = false;
    String dob, address;
    TextView userName,userEmail,phone,userAddress,userDate_of_Birth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initMethod();
        setAction();
        setProfileDetails();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        userProfile = findViewById(R.id.profilePic);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        phone = findViewById(R.id.phone);
        userAddress = findViewById(R.id.userAddress);
        userDate_of_Birth = findViewById(R.id.userDate_of_Birth);
        profileChange = findViewById(R.id.profileChange);
        share = findViewById(R.id.share);
        sessionManager = new SessionManager(getApplicationContext());

        Log.d("Amit","Value2 "+sessionManager.getUserData().getMobileNo());
        Log.d("Amit","Value3 "+sessionManager.getUserData().getUserName());
        Log.d("Amit","Value4 "+sessionManager.getUserData().getEmailId());
        Log.d("Amit","Value5 "+sessionManager.getUserData().getUser_id());


        userName.setText(""+sessionManager.getUserData().getUserName());
        userEmail.setText(""+sessionManager.getUserData().getEmailId());
        phone.setText(""+sessionManager.getUserData().getMobileNo());

        if(sessionManager.getUserProfileImage()!=null){
            userProfile.setImageURI(sessionManager.getUserProfileImage());
        }
        getUserDetailFromAPI();
    }

    private void setAction() {
        backPress.setOnClickListener(view -> onBackPressed());
        //Profile Share-----------------------------------------
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "YoYoIQ");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = "profileImage";
                ImagePicker.with(ProfileActivity.this)
                        .crop(16f, 9f)
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (code.equalsIgnoreCase("profileImage")) {
                profileImagePath = FileUriUtils.INSTANCE.getRealPath(this, data.getData());
                Uri selectedImage = data.getData();
                userProfile.setImageURI(selectedImage);
                sessionManager.saveProfileImage(selectedImage);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
//                String image = getStringImage(lastBitmap);
//                SendImage(image);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myInfo:
                return true;
            case R.id.searchProfile:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setProfileDetails() {


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            String UserName = googleSignInAccount.getDisplayName();
            userName.setText(UserName);
            String userEmail = googleSignInAccount.getEmail();
            Uri photoUrl = googleSignInAccount.getPhotoUrl();
            String id = googleSignInAccount.getId();
            Glide.with(this)
                    .load(photoUrl)
                    .into(userProfile);
        }
    }


    private void getUserDetailFromAPI() {
        Call<ViewKycResponse> call = ApiClient.getInstance().getApi().getkycDetails(sessionManager.getUserData().getUser_id());
        call.enqueue(new Callback<ViewKycResponse>() {
            @Override
            public void onResponse(Call<ViewKycResponse> call, Response<ViewKycResponse> response) {
                ViewKycResponse viewKycResponse = response.body();
                if (response.isSuccessful()) {
                    String data = new Gson().toJson(viewKycResponse.isStatus());
                    status = Boolean.parseBoolean(data);
                    JSONArray jsonArray1 = null;
                    if (status != false) {
                        String data2 = new Gson().toJson(viewKycResponse.getKycDetails());
                        try {
                            jsonArray1 = new JSONArray(data2);
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                dob = jsonObject.getString("dob");
                                address = jsonObject.getString("address");
                                String status = jsonObject.getString("status");
                            }
                            userAddress.setText(address);
                            userDate_of_Birth.setText(dob);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewKycResponse> call, Throwable t) {
            }
        });
    }
}