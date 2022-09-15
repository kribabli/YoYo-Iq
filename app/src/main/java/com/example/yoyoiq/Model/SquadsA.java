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
    String short_namePlayers;
    String pidPlayers;
    String abbr;
    private boolean isSelected = false;
    String avg_points;

    public SquadsA(String player_id, String role, String substitute, String role_str, String playing11, String name, String matchAB, String fantasy_player_rating, String short_namePlayers, String pidPlayers, String abbr, boolean isSelected,String avg_points) {
        this.player_id = player_id;
        this.role = role;
        this.substitute = substitute;
        this.role_str = role_str;
        this.playing11 = playing11;
        this.name = name;
        this.matchAB = matchAB;
        this.fantasy_player_rating = fantasy_player_rating;
        this.short_namePlayers = short_namePlayers;
        this.pidPlayers = pidPlayers;
        this.abbr = abbr;
        this.isSelected = isSelected;
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

    public boolean getPlaying11() {
        return Boolean.parseBoolean(playing11);
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

    public String getShort_namePlayers() {
        return short_namePlayers;
    }

    public void setShort_namePlayers(String short_namePlayers) {
        this.short_namePlayers = short_namePlayers;
    }

    public String getPidPlayers() {
        return pidPlayers;
    }

    public void setPidPlayers(String pidPlayers) {
        this.pidPlayers = pidPlayers;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getAvg_points() {
        return avg_points;
    }

    public void setAvg_points(String avg_points) {
        this.avg_points = avg_points;
    }
}
