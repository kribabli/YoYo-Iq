package com.example.yoyoiq.PlayerPOJO;

public class UpdatedTeamResponse {
    boolean status;
    String response;

    public UpdatedTeamResponse(boolean status, String response) {
        this.status = status;
        this.response = response;
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
}
