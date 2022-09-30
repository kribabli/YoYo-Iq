package com.example.yoyoiq.KYC;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yoyoiq.R;
import com.example.yoyoiq.common.DatabaseConnectivity;
import com.example.yoyoiq.common.SessionManager;
import com.example.yoyoiq.common.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ShowKYCDetails extends AppCompatActivity {
    TextView mobileNo, emailId, panCardNo, bankAccountNo, backPress, mobileStatus, emailStatus, panStatus, accountStatus;
    SharedPrefManager sharedPrefManager;
    DatabaseConnectivity common = DatabaseConnectivity.getInstance();
    String loggedInUserNumber, loggedInUserEmail;
    String status;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String Pancard = "";
    String BankAccount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kycdetails);
        sharedPreferences = getSharedPreferences("path", MODE_PRIVATE);
        sessionManager = new SessionManager(getApplicationContext());
        common.setProgressDialog("", "Loading..", ShowKYCDetails.this, ShowKYCDetails.this);
        initMethod();
        setAction();
        showKYCDetails();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        mobileNo = findViewById(R.id.mobileNo);
        emailId = findViewById(R.id.email);
        panCardNo = findViewById(R.id.panCardNumber);
        bankAccountNo = findViewById(R.id.bankAccountNo);
        mobileStatus = findViewById(R.id.mobileStatus);
        emailStatus = findViewById(R.id.emailStatus);
        panStatus = findViewById(R.id.panStatus);
        accountStatus = findViewById(R.id.accountStatus);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        loggedInUserNumber = sharedPrefManager.getUserData().getMobileNo();
        loggedInUserEmail = sharedPrefManager.getUserData().getEmailId();
        Pancard = getIntent().getStringExtra("Pancard");
        BankAccount = getIntent().getStringExtra("BankAccount");
        status = getIntent().getStringExtra("status");
    }

    private void setAction() {
        backPress.setOnClickListener(view -> onBackPressed());
    }

    private void showKYCDetails() {
        int validation = Integer.parseInt(status);
        if (validation == 1) {
            mobileStatus.setTextColor(Color.parseColor("#ECBD15"));
            mobileStatus.setText("Pending");
            mobileNo.setText("" + sessionManager.getUserData().getMobileNo());
            emailId.setText("" + sessionManager.getUserData().getEmailId());
            panCardNo.setText("" + Pancard);
            bankAccountNo.setText("" + BankAccount);
            emailStatus.setTextColor(Color.parseColor("#ECBD15"));
            emailStatus.setText("Pending");
            panStatus.setTextColor(Color.parseColor("#ECBD15"));
            panStatus.setText("Pending");
            accountStatus.setTextColor(Color.parseColor("#ECBD15"));
            accountStatus.setText("Pending");
        } else if (validation == 2) {
            mobileStatus.setTextColor(Color.parseColor("#109E38"));
            mobileNo.setText("" + sessionManager.getUserData().getMobileNo());
            emailId.setText("" + sessionManager.getUserData().getEmailId());
            panCardNo.setText("" + Pancard);
            bankAccountNo.setText("" + BankAccount);
            mobileStatus.setText("Verified");
            emailStatus.setTextColor(Color.parseColor("#109E38"));
            emailStatus.setText("Verified");
            panStatus.setTextColor(Color.parseColor("#109E38"));
            panStatus.setText("Verified");
            accountStatus.setTextColor(Color.parseColor("#109E38"));
            accountStatus.setText("Verified");
        } else {
            common.showAlertDialog("Alert!", "Please Upload Your Kyc Data Again", false, ShowKYCDetails.this);
        }

        common.closeDialog(ShowKYCDetails.this);
    }
}