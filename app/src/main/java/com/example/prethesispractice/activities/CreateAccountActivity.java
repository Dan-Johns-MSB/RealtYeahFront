package com.example.prethesispractice.activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.entities.PubKey;
import com.example.prethesispractice.entities.User;
import com.example.prethesispractice.entities.UserResponse;
import com.example.prethesispractice.helpers.InputFieldsValidation;
import com.example.prethesispractice.helpers.PKCS1ToSubjectPublicKeyInfo;
import com.example.prethesispractice.models.EncModel;
import com.example.prethesispractice.models.RegistrationFormState;
import com.example.prethesispractice.models.RegistrationModel;
import com.example.prethesispractice.models.RegistrationViewModel;
import com.example.prethesispractice.models.RegistrationrViewModelFactory;
import com.example.prethesispractice.models.UserUpdateModel;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends ToolbarMenuActivity {
    private EditText userLoginView;
    private EditText userPasswordView;
    private EditText userRepeatPasswordView;
    private Spinner userRoleView;
    private Button createUserButton;
    private User user;
    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        inflateToolbar(null);

        userLoginView = findViewById(R.id.createUserLogin);
        userPasswordView = findViewById(R.id.createUserPassword);
        userRepeatPasswordView = findViewById(R.id.createUserPasswordRepeat);
        userRoleView = findViewById(R.id.createUserRole);
        createUserButton = findViewById(R.id.createUserButton);

        ArrayList<String> roleListFront = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.employee_roles_ukr))
        );
        ArrayList<String> roleListBack = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.employee_roles_eng))
        );

        final String token = getIntent().getStringExtra("token");
        int fromEmployee = getIntent().getIntExtra("FROM_EMPLOYEES_DETAILED", 0);
        if (fromEmployee > 0) {
            registrationViewModel = new ViewModelProvider(this, new RegistrationrViewModelFactory())
                    .get(RegistrationViewModel.class);

            registrationViewModel.getRegisterFormState().observe(this, new Observer<RegistrationFormState>() {
                @Override
                public void onChanged(@Nullable RegistrationFormState registrationFormState) {
                    if (registrationFormState == null) {
                        return;
                    }
                    createUserButton.setEnabled(registrationFormState.isDataValid());
                    if (registrationFormState.getUsernameError() != null) {
                        userLoginView.setError(getString(registrationFormState.getUsernameError()));
                    }
                    if (registrationFormState.getPasswordError() != null) {
                        userPasswordView.setError(getString(registrationFormState.getPasswordError()));
                    }
                    if (registrationFormState.getRepPassError() != null) {
                        userRepeatPasswordView.setError(getString(registrationFormState.getRepPassError()));
                    }
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
                    registrationViewModel.registerDataChanged(userLoginView.getText().toString(),
                            userPasswordView.getText().toString(), userRepeatPasswordView.getText().toString());
                }
            };
            userLoginView.addTextChangedListener(afterTextChangedListener);
            userPasswordView.addTextChangedListener(afterTextChangedListener);
            userRepeatPasswordView.addTextChangedListener(afterTextChangedListener);

            httpApi.getEmployeeUser("Bearer " + token, fromEmployee).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        user = response.body();

                        if (roleListBack.indexOf(user.getRole()) > 0) {
                            String tempRole = roleListFront.get(roleListBack.indexOf(user.getRole()));

                            roleListFront.set(roleListBack.indexOf(user.getRole()), roleListFront.get(0));
                            roleListFront.set(0, tempRole);
                            roleListBack.set(roleListBack.indexOf(user.getRole()), roleListBack.get(0));
                            roleListBack.set(0, user.getRole());
                        }

                        userLoginView.setText(user.getLogin());
                        createUserButton.setText(R.string.edit_user_button);
                    }

                    ArrayAdapter<String> forRoleSpinner = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinners,
                            roleListFront);
                    forRoleSpinner.setDropDownViewResource(R.layout.spinners);
                    userRoleView.setAdapter(forRoleSpinner);
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Не вдалося виявити акаунт("
                            + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Неможливо створити/редагувати акаунт без переданого робітника", Toast.LENGTH_LONG).show();
            finish();
        }

        if (userRoleView.getAdapter() == null) {
            ArrayAdapter<String> forRoleSpinner = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.spinners,
                    roleListFront);
            forRoleSpinner.setDropDownViewResource(R.layout.spinners);
            userRoleView.setAdapter(forRoleSpinner);
        }

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer userId = null;
                String login = userLoginView.getText().toString();
                byte[] password = userPasswordView.getText().toString().getBytes(StandardCharsets.UTF_8);
                String role = roleListBack.get(userRoleView.getSelectedItemPosition());

                if (user != null) {
                    userId = user.getUserId();
                }

                /*boolean checkPassFlag = true;
                if (login.isBlank()) {
                    userLoginView.setError("Логін не може бути порожнім");
                    checkPassFlag = false;
                }
                if (!InputFieldsValidation.checkPassword(password)) {
                    userPasswordView.setError("Пароль повинен мати від 8 до 20 символів, "
                                              + "включати як мінімум по одній великій та маленькій "
                                              + "літерах, а також цифри");
                    checkPassFlag = false;
                }*/

                //if (checkPassFlag) {
                    if (user != null) {
                        UserUpdateModel userUpdate = new UserUpdateModel(userId, login, password, role, fromEmployee);

                        Integer finalUserId = userId;
                        httpApi.loadPubKey().enqueue(new Callback<PubKey>() {
                            @Override
                            public void onResponse(Call<PubKey> call, Response<PubKey> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        final String tmp = response.body().getPubKey();
                                        final String encData = Base64.encodeToString(LoginActivity.Encrypt(tmp, userUpdate), Base64.DEFAULT);

                                        httpApi.updateUser("Bearer " + token, finalUserId, new EncModel(encData)).enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Акаунт оновлено успішно!", Toast.LENGTH_LONG).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Помилка під час оновлення акаунта", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                                Toast.makeText(getApplicationContext(), "Помилка під час оновлення акаунта("
                                                        + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.errorBody() != null ? response.errorBody().toString() : "", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<PubKey> call, Throwable throwable) {
                                Toast.makeText(getApplicationContext(), "Помилка під час витягування ключів("
                                        + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    } else {
                        createUserButton.setEnabled(false);
                        RegistrationModel regModel = new RegistrationModel(login, password, role, fromEmployee);

                        httpApi.loadPubKey().enqueue(new Callback<PubKey>() {
                            @Override
                            public void onResponse(Call<PubKey> call, Response<PubKey> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        final String tmp = response.body().getPubKey();
                                        final String encData = Base64.encodeToString(Encrypt(tmp, regModel), Base64.DEFAULT);
                                        httpApi.register(new EncModel(encData)).enqueue(new Callback<UserResponse>() {
                                            @Override
                                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Акаунт створено успішно!", Toast.LENGTH_LONG).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Помилка під час створення акаунта", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<UserResponse> call, Throwable throwable) {
                                                Toast.makeText(getApplicationContext(), "Помилка під час створення акаунта("
                                                        + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.errorBody() != null ? response.errorBody().toString() : "", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<PubKey> call, Throwable throwable) {
                                Toast.makeText(getApplicationContext(), "Помилка під час витягування ключів("
                                        + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                //}
            }
        });
    }

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
}