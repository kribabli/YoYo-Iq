package com.example.yoyoiq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MyCoupons extends AppCompatActivity {
    TextView backPress, applyCode;

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
                Intent intent = new Intent(MyCoupons.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}