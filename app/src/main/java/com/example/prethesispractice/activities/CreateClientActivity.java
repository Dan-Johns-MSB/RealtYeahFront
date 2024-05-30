package com.example.prethesispractice.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.helpers.InputFieldsValidation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClientActivity extends ToolbarMenuActivity {
    private ImageView clientPhotoView;
    private EditText clientNameView;
    private EditText clientPassportView;
    private EditText clientTaxpayerView;
    private EditText clientPhoneNumberView;
    private EditText clientEmailView;
    private EditText clientBirthdateView;
    private EditText clientBirthplaceView;
    private Spinner clientGenderView;
    private Button createClientButton;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);
        inflateToolbar(null);

        clientPhotoView = findViewById(R.id.uploadedPhoto);
        clientNameView = findViewById(R.id.createClientName);
        clientPassportView = findViewById(R.id.createClientPassport);
        clientTaxpayerView = findViewById(R.id.createClientTaxpayerCardNumber);
        clientPhoneNumberView = findViewById(R.id.createClientPhoneNumber);
        clientEmailView = findViewById(R.id.createClientEmail);
        clientBirthdateView = findViewById(R.id.createClientBirthdate);
        clientBirthplaceView = findViewById(R.id.createClientBirthplace);
        clientGenderView = findViewById(R.id.createClientGender);
        createClientButton = findViewById(R.id.createClientButton);

        final String token = getIntent().getStringExtra("token");
        String fromClient = getIntent().getStringExtra("FROM_CLIENTS_DETAILED");
        ArrayList<String> gendersList = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.genders))
        );

        if (fromClient != null && !fromClient.isBlank()) {
            Gson jsonConverter = new Gson();
            client = jsonConverter.fromJson(fromClient, Client.class);

            if (!gendersList.get(0).equals(client.getGender())) {
                Collections.reverse(gendersList);
            }

            int resourceId = getResources().getIdentifier(client.getPhoto(), "drawable",
                    getPackageName());
            if (resourceId != 0) {
                clientPhotoView.setImageResource(resourceId);
            }

            clientNameView.setText(client.getName());
            clientPassportView.setText(client.getPassportNumber());
            clientTaxpayerView.setText(client.getTaxpayerNumber());
            clientPhoneNumberView.setText(client.getPhoneNumber());
            clientEmailView.setText(client.getEmail());
            clientBirthdateView.setText(client.getBirthdateDateOnly());
            clientBirthplaceView.setText(client.getBirthplace());
            createClientButton.setText(R.string.client_info_edit_client_button);
        }

        ArrayAdapter<String> forGenderSpinner = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinners,
                gendersList);
        forGenderSpinner.setDropDownViewResource(R.layout.spinners);
        clientGenderView.setAdapter(forGenderSpinner);

        createClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = clientNameView.getText().toString();
                String passportNumber = clientPassportView.getText().toString();
                String taxpayerNumber = clientTaxpayerView.getText().toString();
                String phoneNumber = clientPhoneNumberView.getText().toString();
                String email = clientEmailView.getText().toString();
                String birthdate = clientBirthdateView.getText().toString();
                String birthplace = clientBirthplaceView.getText().toString();
                String gender = clientGenderView.getSelectedItem().toString();

                boolean checkPassFlag = true;
                if (name.isBlank()) {
                    clientNameView.setError("Ім'я не може бути порожнім");
                    checkPassFlag = false;
                }
                if (!InputFieldsValidation.checkPassportNumber(passportNumber)) {
                    clientPassportView.setError("Номер паспорта повинен відповідати формату 9-х цифр або двох літер та 6 цифр");
                    checkPassFlag = false;
                }
                if (taxpayerNumber.isBlank() || taxpayerNumber.length() != 10) {
                    clientTaxpayerView.setError("РНОКПП має мати 9 цифр");
                    checkPassFlag = false;
                }
                if (phoneNumber.isBlank() || phoneNumber.length() > 13) {
                    clientPhoneNumberView.setError("Номер телефону повинен містити лише цифри без знаків,"
                            + "пробілів, та мати не більше 13 цифр");
                    checkPassFlag = false;
                }
                if (email.isBlank()) {
                    clientEmailView.setError("Електронна пошта не може бути порожньою");
                    checkPassFlag = false;
                }
                if (!InputFieldsValidation.checkDate(birthdate)) {
                    clientBirthdateView.setError("Дата повинна відповідати формату рік(4 цифри)-місяць(2 цифри)-день(2 цифри)");
                    checkPassFlag = false;
                }
                if (birthplace.isBlank()) {
                    clientBirthplaceView.setError("Місце народження не може бути порожнім");
                    checkPassFlag = false;
                }

                if (checkPassFlag) {
                    if (fromClient != null && !fromClient.isBlank()) {
                        client = new Client(client.getClientId(), passportNumber, taxpayerNumber, name, phoneNumber, email,
                                birthdate + " 00:00:00", birthplace, gender, "Unknown"/*this is photo if what*/);

                        httpApi.updateClient("Bearer " + token, client.getClientId(), client).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    finishEditingWithResult("Збережено");
                                } else {
                                    finishEditingWithError("Помилка при збереженні. Спробуйте знову");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                finishEditingWithError("Помилка при збереженні відредагованого клієнта("
                                        + t.getMessage() + "). Спробуйте знову");
                            }
                        });
                    } else {
                        client = new Client(null, passportNumber, taxpayerNumber, name, phoneNumber, email,
                                birthdate + " 00:00:00", birthplace, gender, "Unknown"/*this is photo if what*/);

                        httpApi.insertClient("Bearer " + token, client).enqueue(new Callback<Client>() {
                            @Override
                            public void onResponse(Call<Client> call, Response<Client> response) {
                                if (response.isSuccessful()) {
                                    client = response.body();
                                    finishEditingWithResult("Клієнт успішно доданий!");
                                } else {
                                    finishEditingWithError("Помилка при збереженні нового клієнта. Спробуйте знову");
                                }
                            }

                            @Override
                            public void onFailure(Call<Client> call, Throwable t) {
                                finishEditingWithError("Помилка при збереженні нового клієнта("
                                        + t.getMessage() + "). Спробуйте знову");
                            }
                        });
                    }
                }
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

    private void finishEditingWithResult(String message) {
        Gson jsonConverter = new Gson();
        String resultClient = jsonConverter.toJson(client);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_client", resultClient);
        setResult(Activity.RESULT_OK, resultReturn);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        finish();
    }

    private void finishEditingWithError(String message) {
        Intent resultReturn = new Intent();
        setResult(Activity.RESULT_CANCELED, resultReturn);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        finish();
    }
}