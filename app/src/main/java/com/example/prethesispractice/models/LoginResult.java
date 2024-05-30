package com.example.prethesispractice.models;

import androidx.annotation.Nullable;

public class LoginResult {
    @Nullable
    private LoggedInUser success;
    @Nullable
    private String info;

    public LoginResult(@Nullable LoggedInUser success) {
        this.success = success;
    }

    public LoginResult(@Nullable String info) { this.info = info; }

    @Nullable
    public LoggedInUser getSuccess() {
        return success;
    }

    @Nullable
    public String getInfo() { return info; }
}
