package com.example.prethesispractice.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.EstateObject;
import com.example.prethesispractice.models.constants.Role;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObjectsDetailedActivity extends ToolbarMenuActivity {
    private ImageView objectPhoto;
    private TextView objectAddress;
    private TextView objectType;
    private TextView objectArea;
    private TextView objectPrice;
    private TextView lastOwner;
    private TextView status;
    private Button watchDocumentsButton;
    private Button watchOwnerButton;
    private Button watchPotentialClientsButton;
    private Button watchOperationsButton;
    private Button editObjectButton;
    private Button deleteObjectButton;
    private EstateObject estateObject;
    private final ActivityResultLauncher<Intent> editObjectLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String editedClient = result.getData().getStringExtra("result_object");

                    Gson jsonConverter = new Gson();
                    estateObject = jsonConverter.fromJson(editedClient, EstateObject.class);
                    String token = getIntent().getStringExtra("token");
                    displayObjectInfo(token);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects_detailed);
        inflateToolbar(null);

        // Associating client photo ImageView and info TextViews with corresponding variables
        objectPhoto = findViewById(R.id.objectImage);
        objectAddress = findViewById(R.id.objectAddress);
        objectType = findViewById(R.id.objectType);
        objectArea = findViewById(R.id.objectArea);
        objectPrice = findViewById(R.id.objectPrice);
        lastOwner = findViewById(R.id.objectOwner);
        status = findViewById(R.id.objectStatus);

        String chosenObject = getIntent().getStringExtra("chosen_object");
        if (chosenObject == null || chosenObject.isBlank()) {
            Toast.makeText(getApplicationContext(), "No object data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        Gson jsonConverter = new Gson();
        estateObject = jsonConverter.fromJson(chosenObject, EstateObject.class);

        watchDocumentsButton = findViewById(R.id.watchDocumentsButton);
        watchOwnerButton = findViewById(R.id.watchOwnerButton);
        watchPotentialClientsButton = findViewById(R.id.watchPotentialClientsButton);
        watchOperationsButton = findViewById(R.id.watchOperationsButton);
        editObjectButton = findViewById(R.id.objectInfoEditObjectButton);
        deleteObjectButton = findViewById(R.id.objectInfoDeleteObjectButton);

        String token = getIntent().getStringExtra("token");
        displayObjectInfo(token);

        watchDocumentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromObjectsDetailed(WatchDocumentsActivity.class);
            }
        });
        watchPotentialClientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromObjectsDetailed(ClientsSearchActivity.class);
            }
        });
        watchOperationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromObjectsDetailed(OperationsListActivity.class);
            }
        });
        editObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToObjectEdit = new Intent(getApplicationContext(), CreateObjectActivity.class);
                moveToObjectEdit.putExtra("login", getIntent().getStringExtra("login"));
                moveToObjectEdit.putExtra("role", getIntent().getStringExtra("role"));
                moveToObjectEdit.putExtra("token", getIntent().getStringExtra("token"));
                moveToObjectEdit.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

                Gson jsonConverter = new Gson();
                String objectJson = jsonConverter.toJson(estateObject);
                moveToObjectEdit.putExtra("FROM_OBJECTS_DETAILED", objectJson);

                String employeeNameExtra = getIntent().getStringExtra("employeeName");

                if (employeeNameExtra != null) {
                    moveToObjectEdit.putExtra("employeeName", employeeNameExtra);
                }

                editObjectLauncher.launch(moveToObjectEdit);
            }
        });

        String role = getIntent().getStringExtra("role");
        int employeeId = getIntent().getIntExtra("employee_id", 0);

        if (Role.MAIN_ADMIN.equals(role) || Role.ADMIN.equals(role)) {
            deleteObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder forDelete = new AlertDialog.Builder(ObjectsDetailedActivity.this);
                    forDelete.setTitle(R.string.delete_object_button);
                    forDelete.setMessage(R.string.delete_object_message);
                    forDelete.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            httpApi.deleteEstateObject("Bearer " + token, estateObject.getEstateObjectId()).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Об'єкт успішно видалений!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення об'єкта("
                                                    + response.errorBody().string() + ")", Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення об'єкта", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                    Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення об'єкт("
                                            + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    forDelete.setNegativeButton(R.string.alert_dialog_goBack, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = forDelete.create();
                    alertDialog.show();
                }
            });
        }
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

    private void displayObjectInfo(String token) {
        if (estateObject == null) {
            Toast.makeText(getApplicationContext(), "No estate object data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        // objectPhoto must be specified here
        int resourceId = getResources().getIdentifier(estateObject.getPhoto(), "drawable",
                getPackageName());
        if (resourceId != 0) {
            objectPhoto.setImageResource(resourceId);
        }

        objectAddress.setText(estateObject.getAddress());
        objectType.setText(estateObject.getType());
        objectArea.setText(String.format("%.2f", estateObject.getArea()));
        objectPrice.setText(String.format("%.2f", estateObject.getPrice()));
        status.setText(estateObject.getStatus());

        httpApi.getEstateObjectOwner("Bearer " + token, estateObject.getEstateObjectId()).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    lastOwner.setText(response.body().getName());

                    watchOwnerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent moveToClientDetails = new Intent(getApplicationContext(), ClientsDetailedActivity.class);

                            Gson jsonConverter = new Gson();
                            String ownerJson = jsonConverter.toJson(response.body());
                            moveToClientDetails.putExtra("chosen_client", ownerJson);
                            passEmployeeData(moveToClientDetails);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cameFromObjectsDetailed(Class<?> activityClass) {
        Intent moveToActivity = new Intent(getApplicationContext(), activityClass);

        if (moveToActivity != null) {
            moveToActivity.putExtra("FROM_OBJECTS_DETAILED", estateObject.getEstateObjectId());

            passEmployeeData(moveToActivity);
        }
    }
}