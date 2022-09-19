package com.example.yoyoiq.Model;

public class ReferCode {
    boolean status;
    String response;
    String data;

    public ReferCode(boolean status, String response, String data) {
        this.status = status;
        this.response = response;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
