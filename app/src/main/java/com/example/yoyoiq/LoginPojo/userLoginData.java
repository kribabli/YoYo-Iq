package com.example.yoyoiq.LoginPojo;

public class userLoginData {
    String user_id;
    String username;
    String mobile_no;
    String email_id;
    String referral_code;

    public userLoginData(String user_id, String username, String mobile_no, String email_id,String referral_code) {
        this.user_id = user_id;
        this.username = username;
        this.mobile_no = mobile_no;
        this.email_id = email_id;
        this.referral_code = referral_code;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }
}
