package com.mazeclub;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private String userEmail;
    private String userName;

    public Data(){}
    public Data(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserEmail(String email) {
        userEmail = email;
    }

    public void setUserName(String name) {
        userName = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userEmail", userEmail);
        result.put("userName", userName);
        return result;
    }
}
