package com.example.yoyoiq.KYC;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yoyoiq.R;
import com.example.yoyoiq.common.DatabaseConnectivity;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.example.yoyoiq.common.SharedPrefManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.util.FileUriUtils;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class KYCActivity extends AppCompatActivity {

    TextView backPress, camera1, date_of_birth,adharImageCamera;
    EditText fullName, accountNo, retypeAccount, bankName, ifscCode, panCard, aadharNo, address_ed;
    Button submit;
    DatabaseConnectivity common = DatabaseConnectivity.getInstance();
    SharedPrefManager sharedPrefManager;
    String loggedInUserNumber;
    ImageView imageViewPan,aadhar_card_Image;
    String code = "";
    SessionManager sessionManager;
    private DatePickerDialog datePickerDialog;
    private Boolean clickable = true;
    String date = "", year = "", month = "";
    int PICK_IMAGE_MULTIPLE = 100;
    String pan_img_path = "", aadhar_img_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycactivity);
        initMethod();
        setAction();
        loggedInUserNumber = sharedPrefManager.getUserData().getMobileNo();
        sessionManager = new SessionManager(getApplicationContext());
        Log.d("Amit","Value "+sessionManager.getUserData().getUser_id());

    }

    private void initMethod() {
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        backPress = findViewById(R.id.backPress);
        camera1 = findViewById(R.id.camera1);
        imageViewPan = findViewById(R.id.pancard_img);
        fullName = findViewById(R.id.fullNameEt);
        accountNo = findViewById(R.id.accountNoEt);
        retypeAccount = findViewById(R.id.accountNoEt1);
        bankName = findViewById(R.id.bankName);
        ifscCode = findViewById(R.id.ifscCode);
        panCard = findViewById(R.id.panCardNo);
        aadharNo = findViewById(R.id.aadharNo);
        submit = findViewById(R.id.submitBtn);
        address_ed = findViewById(R.id.address_ed);
        date_of_birth = findViewById(R.id.date_of_birth);
        adharImageCamera = findViewById(R.id.adharImageCamera);
        aadhar_card_Image = findViewById(R.id.aadhar_card_Image);
    }

    private void setAction() {
        backPress.setOnClickListener(view -> onBackPressed());

        camera1.setOnClickListener(view -> {
            callCamera();
        });

        submit.setOnClickListener(view -> buttonValidation());

        date_of_birth.setOnClickListener(view -> {
            if (clickable) {
                clickable = false;
                final Calendar calender = Calendar.getInstance();
                int years = calender.get(Calendar.YEAR);
                int monthInt = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(KYCActivity.this, (view1, years1, monthsOfYear, dayOfMonths) -> {
                    year = "" + years1;
                    date = (dayOfMonths + "-" + (monthsOfYear + 1) + "-" + year);
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd-MM-yyyy").parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        month = new SimpleDateFormat("MMMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    date_of_birth.setText(date);
                    clickable = true;
                }, years, monthInt, day);
                datePickerDialog.show();
            }
        });

        adharImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallCameraForAdharImage();
            }
        });
    }


    private void callCamera() {
        code = "pan";
        ImagePicker.with(KYCActivity.this)
                .crop(16f, 9f)
                .start();

    }

    private void CallCameraForAdharImage() {
        code = "adhar";
        ImagePicker.with(KYCActivity.this)
                .crop(16f, 9f)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (code.equalsIgnoreCase("pan")) {
                pan_img_path = FileUriUtils.INSTANCE.getRealPath(this, data.getData());
                Uri selectedImage = data.getData();
                imageViewPan.setImageURI(selectedImage);
            } else if (code.equalsIgnoreCase("adhar")) {
                aadhar_img_path = FileUriUtils.INSTANCE.getRealPath(this, data.getData());
                Uri selectedImage1 = data.getData();
                aadhar_card_Image.setImageURI(selectedImage1);
            }
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Image Selecting Error...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean buttonValidation() {
        boolean isValid = true;
        try {
            if (fullName.getText().toString().trim().length() == 0) {
                fullName.setError("Please enter Full Name");
                fullName.requestFocus();
                isValid = false;
            } else if (accountNo.getText().toString().trim().length() == 0) {
                accountNo.setError("Please enter account no.");
                accountNo.requestFocus();
                isValid = false;
            } else if (accountNo.getText().toString().trim().length() <= 10) {
                accountNo.setError("Please enter correct account no.");
                accountNo.requestFocus();
                isValid = false;
            } else if (date_of_birth.getText().toString().length() == 0) {
                date_of_birth.setError("Please enter correct account no.");
                date_of_birth.requestFocus();
                isValid = false;
            } else if (retypeAccount.getText().toString().trim().length() == 0
                    || retypeAccount.getText().toString().trim() == accountNo.getText().toString().trim()) {
                retypeAccount.setError("Re-Enter account no.");
                retypeAccount.requestFocus();
                isValid = false;
            } else if (bankName.getText().toString().trim().length() == 0) {
                bankName.setError("Please enter Bank Name");
                bankName.requestFocus();
                isValid = false;
            } else if (address_ed.getText().toString().trim().length() == 0) {
                address_ed.setError("Please enter address ");
                address_ed.requestFocus();
                isValid = false;
            } else if (ifscCode.getText().toString().trim().length() == 0) {
                ifscCode.setError("Please enter IFSC Code");
                ifscCode.requestFocus();
                isValid = false;
            } else if (ifscCode.getText().toString().trim().length() <= 10) {
                ifscCode.setError("Please enter correct IFSC Code");
                ifscCode.requestFocus();
                isValid = false;
            } else if (panCard.getText().toString().trim().length() == 0) {
                panCard.setError("Please enter PANCard No.");
                panCard.requestFocus();
                isValid = false;
            } else if (panCard.getText().toString().trim().length() < 10) {
                panCard.setError("Please enter correct PANCard No.");
                panCard.requestFocus();
                isValid = false;
            } else if (imageViewPan.getDrawable() == null) {
                common.showAlertDialog("Alert", "Click Picture of PANCard", false, this);
                isValid = false;
            }
            else if (aadhar_card_Image.getDrawable() == null) {
                common.showAlertDialog("Alert", "Click Picture of Aadhar Card", false, this);
                isValid = false;
            }

            else if (aadharNo.getText().toString().trim().length() == 0) {
                aadharNo.setError("Please enter Aadhar");
                aadharNo.requestFocus();
                isValid = false;
            } else if (aadharNo.getText().toString().trim().length() > 12) {
                aadharNo.setError("Please enter correct Aadhar");
                aadharNo.requestFocus();
                isValid = false;
            } else {
                common.setProgressDialog("", "Loading..", KYCActivity.this, KYCActivity.this);
                send_kyc_Details_OnServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private void send_kyc_Details_OnServer() {

        HelperData.uploadFile(KYCActivity.this, sessionManager.getUserData().getUser_id(), fullName.getText().toString(), accountNo.getText().toString(),
                ifscCode.getText().toString(), bankName.getText().toString(), date_of_birth.getText().toString(), address_ed.getText().toString(), aadharNo.getText().toString(), panCard.getText().toString(), pan_img_path,aadhar_img_path);
        common.closeDialog(KYCActivity.this);
//        showDialog("Details Saved..", true);
        fullName.setText("");
        accountNo.setText("");
        retypeAccount.setText("");
        bankName.setText("");
        ifscCode.setText("");
        panCard.setText("");
        aadharNo.setText("");
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