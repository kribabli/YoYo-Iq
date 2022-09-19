package com.example.yoyoiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoyoiq.LoginPojo.LoginResponse;
import com.example.yoyoiq.Model.ReferCode;
import com.example.yoyoiq.Retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoupons extends AppCompatActivity {
    TextView backPress, applyCode;
    EditText couponCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons);
        initMethod();
        setAction();
    }

    private void initMethod() {
        backPress = findViewById(R.id.backPress);
        applyCode = findViewById(R.id.applyCode);
        couponCode = findViewById(R.id.couponCode);
    }

    private void setAction() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    private boolean validation() {
        boolean isValid = true;
        try {
            if (couponCode.getText().toString().trim().length() == 0) {
                couponCode.setError("Please enter valid code");
                couponCode.requestFocus();
                isValid = false;
            } else {
                checkData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private void checkData() {
        String referCode = couponCode.getText().toString().trim();
        Call<ReferCode> call = ApiClient.getInstance().getApi().getReferCode(referCode);
        call.enqueue(new Callback<ReferCode>() {
            @Override
            public void onResponse(Call<ReferCode> call, Response<ReferCode> response) {
                ReferCode referCode = response.body();
                if (response.isSuccessful()) {
                    if (referCode.getResponse().equalsIgnoreCase("Reffer amount Successfully")) {
                        Intent intent = new Intent(MyCoupons.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MyCoupons.this, "Enter Valid Code...", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReferCode> call, Throwable t) {

            }
        });
    }
}