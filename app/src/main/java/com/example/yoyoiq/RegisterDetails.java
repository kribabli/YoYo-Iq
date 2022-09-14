package com.example.yoyoiq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yoyoiq.KYC.KYCActivity;
import com.example.yoyoiq.LoginPojo.LoginResponse;
import com.example.yoyoiq.LoginPojo.RegistrationResponse;
import com.example.yoyoiq.LoginPojo.userLoginData;
import com.example.yoyoiq.Model.UserData;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.common.DatabaseConnectivity;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.example.yoyoiq.common.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDetails extends AppCompatActivity {
    TextView backPress, registerUser;
    DatabaseConnectivity cmn = DatabaseConnectivity.getInstance();
    TextView userName;
    TextView mobileNo;
    TextView emailId;
    TextView password;
    ProgressDialog progressDialog;
    SharedPrefManager sharedPrefManager;
    SessionManager sessionManager;
    String check;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    List<userLoginData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        initMethod();
        setAction();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Data Uploading..");
        check = getIntent().getStringExtra("check");
        sessionManager = new SessionManager(getApplicationContext());
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        registerUser = findViewById(R.id.registerUserDetails);
        userName = findViewById(R.id.userName);
        mobileNo = findViewById(R.id.mobileNo);
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
    }

    private void setAction() {
        backPress.setOnClickListener(view -> onBackPressed());

        registerUser.setOnClickListener(view -> validation());
    }

    private boolean validation() {
        boolean isValid = true;
        try {
            if (userName.getText().toString().trim().length() == 0) {
                userName.setError("Please enter user name");
                userName.requestFocus();
                isValid = false;
            } else if (mobileNo.getText().toString().trim().length() == 0) {
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
                isValid = false;
            } else if (emailId.getText().toString().trim().length() == 0) {
                emailId.setError("Please enter email");
                emailId.requestFocus();
                isValid = false;
            } else if (password.getText().toString().trim().length() == 0) {
                password.setError("Please enter password");
                password.requestFocus();
                isValid = false;
            } else {
                send_user_Data_onServer();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private void googleDataLogin() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            String userName1 = googleSignInAccount.getDisplayName();
            String userEmail = googleSignInAccount.getEmail();
            Uri photoUrl = googleSignInAccount.getPhotoUrl();
            String id = googleSignInAccount.getId();
            Log.d("Check ", "Here " + userEmail + " " + userName1);
            Call<RegistrationResponse> call = ApiClient.getInstance().getApi().
                    SendUserDetails_server(mobileNo.getText().toString(), userName1, userEmail, password.getText().toString());
            call.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                    RegistrationResponse registrationResponse = response.body();
                    if (response.isSuccessful()) {
                        Log.d("Amit", "Check Here ");
                        if (registrationResponse.getResponse() != null) {
                            if (registrationResponse.getResponse().equalsIgnoreCase("Email already exist")) {


                                Call<LoginResponse> call1 = ApiClient.getInstance().getApi().getUserLoginData(userEmail, password.getText().toString());
                                Log.d("Amit", "Check Data" + call1);
                                call1.enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                        LoginResponse loginResponse = response.body();
                                        if (response.isSuccessful()) {
                                            if (loginResponse.getData().trim().toString().equalsIgnoreCase("Login successful")) {
                                                list = loginResponse.getUserLoginDataArrayList();
                                                String totalData = new Gson().toJson(loginResponse.getUserLoginDataArrayList());
                                                JSONArray jsonArray = null;
                                                try {
                                                    jsonArray = new JSONArray(totalData);
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        String email_id = jsonObject.getString("email_id");
                                                        String mobile_no = jsonObject.getString("mobile_no");
                                                        String user_id = jsonObject.getString("user_id");
                                                        String username = jsonObject.getString("username");
                                                        HelperData.UserId = user_id;
                                                        HelperData.UserName = username;
                                                        HelperData.Usermobile = mobile_no;
                                                        HelperData.UserEmail = email_id;
                                                        UserData userData = new UserData(username, mobile_no, email_id, user_id);
                                                        sessionManager.saveUser(userData);
                                                        Toast.makeText(RegisterDetails.this, "Login Successfully..", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(RegisterDetails.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (loginResponse.getData().trim().toString().equalsIgnoreCase("Username or password something went wrong")) {
                                                showDialog("Invalid Mobile or Password", false);
                                            } else {
                                                showDialog("Please Register YourSelf", false);
                                            }


                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                                    }
                                });

                            } else {
                                showDialog("" + registrationResponse.getResponse().toString(), false);

                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {

                }
            });
        }

    }

    private void send_user_Data_onServer() {
        cmn.setProgressDialog("", "Please wait..", RegisterDetails.this, RegisterDetails.this);

        Call<RegistrationResponse> call = ApiClient.getInstance().getApi().
                SendUserDetails_server(mobileNo.getText().toString(), userName.getText().toString(), emailId.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if (response.isSuccessful()) {
                    if (registrationResponse.getResponse().equalsIgnoreCase("Email already exist")) {
                        showDialog("" + registrationResponse.getResponse(), true);
                        cmn.closeDialog(RegisterDetails.this);
                    }
                    if (registrationResponse.getResponse().equalsIgnoreCase("Mobile no already exist")) {
                        showDialog("" + registrationResponse.getResponse(), true);
                        cmn.closeDialog(RegisterDetails.this);
                    }
                    if (registrationResponse.getResponse().equalsIgnoreCase("successfully registered")) {
                        showDialog("User Register Successfully..", true);
                        cmn.closeDialog(RegisterDetails.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
            }
        });
    }

    public void showDialog(String message, Boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialog, id) -> {
            dialog.dismiss();
            if (isFinish) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}