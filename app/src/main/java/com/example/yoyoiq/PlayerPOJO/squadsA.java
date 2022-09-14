package com.example.yoyoiq.PlayerPOJO;

public class squadsA {
    String player_id;
    String role;
    String substitute;
    String role_str;
    String playing11;
    String name;
    int avg_points;

    public squadsA(String player_id, String role, String substitute, String role_str, String playing11, String name,int avg_points) {
        this.player_id = player_id;
        this.role = role;
        this.substitute = substitute;
        this.role_str = role_str;
        this.playing11 = playing11;
        this.name = name;
        this.avg_points = avg_points;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSubstitute() {
        return substitute;
    }

    public void setSubstitute(String substitute) {
        this.substitute = substitute;
    }

    public String getRole_str() {
        return role_str;
    }

    public void setRole_str(String role_str) {
        this.role_str = role_str;
    }

    public String getPlaying11() {
        return playing11;
    }

    public void setPlaying11(String playing11) {
        this.playing11 = playing11;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvg_points() {
        return avg_points;
    }

    public void setAvg_points(int avg_points) {
        this.avg_points = avg_points;
    }
}
