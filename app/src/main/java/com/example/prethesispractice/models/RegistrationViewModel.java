package com.example.prethesispractice.models;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prethesispractice.R;
import com.example.prethesispractice.helpers.InputFieldsValidation;

public class RegistrationViewModel extends ViewModel {

    private MutableLiveData<RegistrationFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LiveData<RegistrationFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void register(LoginResult lr) {
        this.loginResult.setValue(lr);
    }

    public void registerDataChanged(String username, String password, String repeatPassword) {
        RegistrationFormState formState = new RegistrationFormState(true);
        if (!isUserNameValid(username)) formState.setUsernameError(R.string.invalid_username);
        if (!isPasswordValid(password)) formState.setPasswordError(R.string.invalid_password);
        if (!isRepPasValid(password, repeatPassword)) formState.setRepPassError(R.string.invalid_password_repeat);

        registerFormState.setValue(formState);
    }

    private boolean isUserNameValid(String login) {
        if (login == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(login).matches();
    }

    private boolean isPasswordValid(String password) {
        return InputFieldsValidation.checkPassword(password);
    }

    private boolean isRepPasValid(String pass, String rep){
        return pass.equals(rep);
    }
}
