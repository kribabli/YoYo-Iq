package com.example.yoyoiq.Model;

public class SquadsA {
    String player_id;
    String role;
    String substitute;
    String role_str;
    String playing11;
    String name;
    String matchAB;
    String fantasy_player_rating;

    public SquadsA(String player_id, String role, String substitute, String role_str, String playing11, String name, String matchAB, String fantasy_player_rating) {
        this.player_id = player_id;
        this.role = role;
        this.substitute = substitute;
        this.role_str = role_str;
        this.playing11 = playing11;
        this.name = name;
        this.matchAB = matchAB;
        this.fantasy_player_rating = fantasy_player_rating;
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

    public String getMatchAB() {
        return matchAB;
    }

    public void setMatchAB(String matchAB) {
        this.matchAB = matchAB;
    }

    public String getFantasy_player_rating() {
        return fantasy_player_rating;
    }

    public void setFantasy_player_rating(String fantasy_player_rating) {
        this.fantasy_player_rating = fantasy_player_rating;
    }
}
