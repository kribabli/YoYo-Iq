package com.example.yoyoiq.WalletPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yoyoiq.KYC.KYCActivity;
import com.example.yoyoiq.KYC.ShowKYCDetails;
import com.example.yoyoiq.KYC.ViewKycResponse;
import com.example.yoyoiq.NotificationActivity;
import com.example.yoyoiq.R;
import com.example.yoyoiq.Retrofit.ApiClient;
import com.example.yoyoiq.common.DatabaseConnectivity;
import com.example.yoyoiq.common.HelperData;
import com.example.yoyoiq.common.SessionManager;
import com.example.yoyoiq.common.SharedPrefManager;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCash extends AppCompatActivity implements PaymentStatusListener {
    TextView addCash, backPress, myRecentPay, KYCDetails, notification, amountTv, bonusTv, withdrawTv, winningsTV;
    EditText amount;
    DatabaseConnectivity common = DatabaseConnectivity.getInstance();
    String loggedInUserNumber;
    SharedPrefManager sharedPrefManager;
    SessionManager sessionManager;
    SwipeRefreshLayout swipeRefreshLayout;
    String status1 = "";
    String Pancard = "";
    String BankAccount = "";
    boolean status = false;
    List<String> checkId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash2);
        Checkout.preload(getApplicationContext());
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        swipeRefreshLayout = new SwipeRefreshLayout(AddCash.this);
        initMethod();
        loadBalanceDataFromServer();
        loggedInUserNumber = sharedPrefManager.getUserData().getMobileNo();
        setAction();
    }

    private void LoadKycData() {
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
                                Pancard = jsonObject.getString("pancard_no");
                                BankAccount = jsonObject.getString("account_no");
                                status1 = jsonObject.getString("status");
                            }
                            KYCDetails.setOnClickListener(view -> {
                                if (status != false) {
                                    int validationStatus = Integer.parseInt(status1);
                                    if (validationStatus == 3) {
                                        common.showAlertDialog("Alert!", "Please Upload Your Kyc Data Again", false, AddCash.this);
                                        Intent intent1 = new Intent(AddCash.this, KYCActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(AddCash.this, ShowKYCDetails.class);
                                        intent.putExtra("Pancard", Pancard);
                                        intent.putExtra("BankAccount", BankAccount);
                                        intent.putExtra("status", status1);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (viewKycResponse.isStatus() == false) {
                        KYCDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent1 = new Intent(AddCash.this, KYCActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ViewKycResponse> call, Throwable t) {
            }
        });
    }

    private void loadBalanceDataFromServer() {
        Call<ViewBalanceResponse> call = ApiClient.getInstance().getApi().getBalanceDetails(sessionManager.getUserData().getUser_id());

        call.enqueue(new Callback<ViewBalanceResponse>() {
            @Override
            public void onResponse(Call<ViewBalanceResponse> call, Response<ViewBalanceResponse> response) {
                ViewBalanceResponse viewBalanceResponse = response.body();
                if (response.isSuccessful()) {
                    String balanceData = new Gson().toJson(viewBalanceResponse.getBalance());
                    JSONArray jsonArray = null;
                    String balance = null;
                    String bouns_cash = null;
                    try {
                        jsonArray = new JSONArray(balanceData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            balance = jsonObject.getString("balance");
                            bouns_cash = jsonObject.getString("add_bonus");
                        }
                        amountTv.setText(" " + balance);
                        bonusTv.setText(" " + bouns_cash);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ViewBalanceResponse> call, Throwable t) {

            }
        });
    }

    private void initMethod() {
        LoadKycData();
        addCash = findViewById(R.id.addCash);
        amountTv = findViewById(R.id.amountTv);
        withdrawTv = findViewById(R.id.withdrawTv);
        winningsTV = findViewById(R.id.winningsTV);
        bonusTv = findViewById(R.id.bonusTv);
        amount = findViewById(R.id.amount);
        notification = findViewById(R.id.notification);
        backPress = findViewById(R.id.backPress);
        myRecentPay = findViewById(R.id.myRecentPay);
        KYCDetails = findViewById(R.id.KYCDetails);
    }

    private void setAction() {
        myRecentPay.setOnClickListener(view -> {
            Intent intent = new Intent(AddCash.this, RecentTransactions.class);
            startActivity(intent);
        });

        backPress.setOnClickListener(view -> onBackPressed());

        notification.setOnClickListener(view -> {
            Intent intent = new Intent(AddCash.this, NotificationActivity.class);
            startActivity(intent);
        });

        addCash.setOnClickListener(view -> addAmountValidation());
    }

    private boolean addAmountValidation() {
        String balance = amount.getText().toString();
        boolean isValid = true;
        try {
            if (amount.getText().toString().trim().length() == 0
                    || amount.getText().toString().startsWith("0")
                    || amount.getText().toString().startsWith("00")) {
                amount.setError("Please enter amount");
                amount.requestFocus();
                isValid = false;
            } else {
                paymentMethod();
//                UpiPaymentMethod();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private void UpiPaymentMethod() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        String transcId = df.format(c);
        Float balance = Float.parseFloat(amount.getText().toString());
        DecimalFormat data1 = new DecimalFormat("0.00");
        data1.setMaximumFractionDigits(2);

        balance = Float.parseFloat(data1.format(balance));

        try {
            final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                    .with(this)
                    .setPayeeVpa("Q038766212@ybl")
                    .setPayeeName("YOYOIQ")
                    .setTransactionId(transcId)
                    .setTransactionRefId(transcId)
                    .setDescription("YOYOIQ UPI")
                    .setAmount(String.valueOf(balance))
                    .build();
            easyUpiPayment.startPayment();
            easyUpiPayment.setPaymentStatusListener(this);

        } catch (Exception e) {
            e.toString();
        }
    }

    private void paymentMethod() {
        String sAmount = amount.getText().toString();
        int amount = Math.round(Float.parseFloat(sAmount) * 100);

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_jBetacTbV0YhJN");
        checkout.setImage(R.drawable.cricket_player);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "YoYoIq");
            jsonObject.put("description", "Test Payment");
            jsonObject.put("theme.color", "#0093DD");
            jsonObject.put("currency", "INR");
            jsonObject.put("amount", amount);
            jsonObject.put("contact", "xxxxxxxx32");
            jsonObject.put("meratemplate", "mtteam.suraj@gmail.com");
            checkout.open(AddCash.this, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendBalanceServer(String s) {
        Call<PostBalanceResponse> call = ApiClient.getInstance().getApi().sendBalanceData(HelperData.UserId, amount.getText().toString(), s);
        call.enqueue(new Callback<PostBalanceResponse>() {
            @Override
            public void onResponse(Call<PostBalanceResponse> call, Response<PostBalanceResponse> response) {
                PostBalanceResponse postBalanceResponse = response.body();
                if (response.isSuccessful()) {
                    if (postBalanceResponse.getResponse().toString().equalsIgnoreCase("successfully added")) {
                        Log.d("Amit","Value11 "+postBalanceResponse.getResponse());
                        loadBalanceDataFromServer();
                        amount.setText("");
                    }
                }
            }
            @Override
            public void onFailure(Call<PostBalanceResponse> call, Throwable t) {
                Log.d("Amit","Value11 "+t);

            }
        });
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (transactionDetails.getStatus().equalsIgnoreCase("SUCCESS")) {
            builder.setTitle("Payment Successfully added..");
            builder.setMessage(transactionDetails.toString());
            builder.show();
            sendBalanceServer(transactionDetails.getTransactionId());
        } else if (transactionDetails.getStatus().equalsIgnoreCase("Failed")) {
            builder.setTitle("Payment Failed..");
            builder.setMessage(transactionDetails.toString());
            builder.show();
        }
    }

    @Override
    public void onTransactionSuccess() {
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSubmitted() {
        Toast.makeText(this, "Transaction successfully submitted..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        Toast.makeText(this, "Transaction Failed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Transaction cancelled...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {
        Toast.makeText(this, "App not found..", Toast.LENGTH_SHORT).show();
    }
}