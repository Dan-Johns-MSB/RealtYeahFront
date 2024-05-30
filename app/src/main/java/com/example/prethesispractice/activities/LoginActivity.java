package com.example.prethesispractice.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.PubKey;
import com.example.prethesispractice.entities.UserResponse;
import com.example.prethesispractice.helpers.PKCS1ToSubjectPublicKeyInfo;
import com.example.prethesispractice.httpApi.HttpApi;
import com.example.prethesispractice.httpApi.HttpRequest;
import com.example.prethesispractice.models.AuthenticationModel;
import com.example.prethesispractice.models.EncModel;
import com.example.prethesispractice.models.LoginFormState;
import com.example.prethesispractice.models.LoginResult;
import com.example.prethesispractice.models.LoginViewModel;
import com.example.prethesispractice.models.LoginViewModelFactory;
import com.google.gson.Gson;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView loginInputField;
    private TextView passwordInputField;
    private Button loginSubmitButton;
    private LoginViewModel loginViewModel;

    public static byte[] Encrypt(String pubKey, Object t) {
        Gson g = new Gson();
        String enc_log = g.toJson(t);

        try {
            PublicKey publicKey;
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            byte[] byteKey = Base64.decode(pubKey.getBytes(), Base64.DEFAULT);
            byteKey = PKCS1ToSubjectPublicKeyInfo.createSubjectPublicKeyInfoEncoding(byteKey);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            publicKey = kf.generatePublic(X509publicKey);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(enc_log.getBytes());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginInputField = findViewById(R.id.inputLogin);
        passwordInputField = findViewById(R.id.inputPassword);
        loginSubmitButton = findViewById(R.id.loginSubmitButton);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginSubmitButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    loginInputField.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordInputField.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                setResult(Activity.RESULT_OK);

                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // doing nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // doing nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(loginInputField.getText().toString(),
                        passwordInputField.getText().toString());
            }
        };
        loginInputField.addTextChangedListener(afterTextChangedListener);
        passwordInputField.addTextChangedListener(afterTextChangedListener);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSubmitButton.setEnabled(false);
                AuthenticationModel auth = new AuthenticationModel(loginInputField.getText().toString(),
                        passwordInputField.getText().toString());
                Tmo(auth);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void Tmo(final AuthenticationModel auth) {
        HttpApi api = new HttpApi();
        final HttpRequest userService = api.getRetrofit().create(HttpRequest.class);
        userService.loadPubKey().enqueue(new Callback<PubKey>() {
            @Override
            public void onResponse(Call<PubKey> call, Response<PubKey> response) {
                if (response.isSuccessful()) {
                    try {
                        final String tmp = response.body().getPubKey();
                        final String encData = Base64.encodeToString(Encrypt(tmp, auth), Base64.DEFAULT);
                        userService.login(new EncModel(encData))
                                .enqueue(new Callback<UserResponse>() {
                                    @Override
                                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                                main.putExtra("login", response.body().getLogin());
                                                main.putExtra("role", response.body().getRole());
                                                main.putExtra("token", response.body().getToken());
                                                main.putExtra("employee_id", response.body().getEmployeeId());

                                                startActivity(main);
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            try {
                                                if (response.code() == 404) {
                                                    Failure(new Exception("User doesn't exist or incorrect login"));
                                                } else {
                                                    Failure(new Exception("Incorrect password (response code:" + response.code() + ")"));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserResponse> call, Throwable t) {
                                        Failure(t);
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Failure(new Exception(response.errorBody() != null ? response.errorBody().toString() : ""));
                }
            }

            @Override
            public void onFailure(Call<PubKey> call, Throwable t) {
                Failure(t);
            }
        });
    }

    private void Failure(Throwable t) {
        LoginResult error = new LoginResult(t.getMessage());
        Log.d("retro_error", t.getMessage());
        loginViewModel.login(error);
    }
}