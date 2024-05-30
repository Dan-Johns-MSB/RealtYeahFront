package com.example.prethesispractice.models;

import androidx.annotation.Nullable;

public class RegistrationFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repPassError;

    private boolean isDataValid;

    public RegistrationFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer repPassError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.repPassError = repPassError;
        this.isDataValid = false;
    }

    public RegistrationFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.repPassError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getRepPassError() {
        return repPassError;
    }

    public void setUsernameError(@Nullable Integer usernameError) {
        this.isDataValid = false;
        this.usernameError = usernameError;
    }

    public void setPasswordError(@Nullable Integer passwordError) {
        this.isDataValid = false;
        this.passwordError = passwordError;
    }

    public void setRepPassError(@Nullable Integer repPassError) {
        this.isDataValid = false;
        this.repPassError = repPassError;
    }

    public void setDataValid(boolean dataValid) {
        this.isDataValid = false;
        isDataValid = dataValid;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
