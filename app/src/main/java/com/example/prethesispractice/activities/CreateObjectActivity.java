package com.example.prethesispractice.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.EstateObject;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateObjectActivity extends ToolbarMenuActivity {
    private ImageView objectPhotoView;
    private EditText objectAddressView;
    private EditText objectTypeView;
    private EditText objectAreaView;
    private EditText objectPriceView;
    private Button createObjectButton;
    private EstateObject estateObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_object);
        inflateToolbar(null);

        objectPhotoView = findViewById(R.id.uploadedPhoto);
        objectAddressView = findViewById(R.id.createObjectAddress);
        objectTypeView = findViewById(R.id.createObjectType);
        objectAreaView = findViewById(R.id.createObjectArea);
        objectPriceView = findViewById(R.id.createObjectPrice);
        createObjectButton = findViewById(R.id.createObjectButton);

        final String token = getIntent().getStringExtra("token");
        String fromObject = getIntent().getStringExtra("FROM_OBJECTS_DETAILED");

        if (fromObject != null && !fromObject.isBlank()) {
            Gson jsonConverter = new Gson();
            estateObject = jsonConverter.fromJson(fromObject, EstateObject.class);

            int resourceId = getResources().getIdentifier(estateObject.getPhoto(), "drawable",
                    getPackageName());
            if (resourceId != 0) {
                objectPhotoView.setImageResource(resourceId);
            }

            objectAddressView.setText(estateObject.getAddress());
            objectTypeView.setText(estateObject.getType());
            objectAreaView.setText(String.format("%.2f", estateObject.getArea()));
            objectPriceView.setText(String.format("%.2f", estateObject.getPrice()));
            createObjectButton.setText(R.string.object_info_edit_object_button);
        }

        createObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = objectAddressView.getText().toString();
                String type = objectTypeView.getText().toString();
                double area = 0;
                double price = 0;

                boolean checkPassFlag = true;
                if (address.isBlank()) {
                    objectAddressView.setError("Адреса об'єкту не може бути порожньою");
                    checkPassFlag = false;
                }
                if (type.isBlank()) {
                    objectTypeView.setError("Тип об'єкта не може бути порожнім");
                    checkPassFlag = false;
                }

                try {
                    area = Double.parseDouble(objectAreaView.getText().toString());

                    if (area <= 0) {
                        throw new NumberFormatException("Negative or zero area");
                    }
                } catch (NumberFormatException ex) {
                    objectAreaView.setError("Площа об'єкта не може бути меншою або дорівнювати нулю");
                    checkPassFlag = false;
                }

                try {
                    price = Double.parseDouble(objectPriceView.getText().toString());

                    if (price < 0) {
                        throw new NumberFormatException("Negative price");
                    }
                } catch (NumberFormatException ex) {
                    objectPriceView.setError("Вартість об'єкта не може бути меншою за нуль");
                    checkPassFlag = false;
                }

                if (checkPassFlag) {
                    if (fromObject != null && !fromObject.isBlank()) {
                        estateObject = new EstateObject(estateObject.getEstateObjectId(), "Unknown"/*this is photo if what*/, address, type, area, price, estateObject.getStatus());

                        httpApi.updateEstateObject("Bearer " + token, estateObject.getEstateObjectId(), estateObject).enqueue(new Callback<ResponseBody>() {
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
                                finishEditingWithError("Помилка при збереженні відредагованого об'єкта("
                                        + t.getMessage() + "). Спробуйте знову");
                            }
                        });
                    } else {
                        estateObject = new EstateObject(null, "Unknown"/*this is photo if what*/, address, type, area, price, "Внесений у базу");

                        httpApi.insertEstateObject("Bearer " + token, estateObject).enqueue(new Callback<EstateObject>() {
                            @Override
                            public void onResponse(Call<EstateObject> call, Response<EstateObject> response) {
                                if (response.isSuccessful()) {
                                    estateObject = response.body();
                                    finishEditingWithResult("Об'єкт успішно доданий!");
                                } else {
                                    finishEditingWithError("Помилка при збереженні нового об'єкта. Спробуйте знову");
                                }
                            }

                            @Override
                            public void onFailure(Call<EstateObject> call, Throwable t) {
                                finishEditingWithError("Помилка при збереженні нового об'єкта("
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
        String resultObject = jsonConverter.toJson(estateObject);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_object", resultObject);
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