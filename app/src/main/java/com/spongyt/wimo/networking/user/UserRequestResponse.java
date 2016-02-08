package com.spongyt.wimo.networking.user;

/**
 * Created by tmichelc on 05.02.2016.
 */
public class UserRequestResponse {

    private String gcmToken;
    private String id;
    private String password;

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
