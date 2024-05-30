package com.example.prethesispractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.entities.EstateObject;
import com.example.prethesispractice.entities.Operation;
import com.example.prethesispractice.helpers.InputFieldsValidation;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOperationActivity extends ToolbarMenuActivity {
    private EditText operationName;
    private EditText operationDate;
    private EditText operationTime;
    private TextView leadCounteragentNameView;
    private Button pickFirstClientButton;
    private TextView secondaryCounteragentNameView;
    private Button pickSecondClientButton;
    private TextView objectAddressView;
    private Button pickObjectButton;
    private TextView hostNameView;
    private Button pickHostButton;
    private Button createButton;
    private Client counteragentLead;
    private Client counteragentSecondary;
    private EstateObject estateObject;
    private Employee host;
    private Operation leadOperation;
    private Operation secondaryOperation;
    private final ActivityResultLauncher<Intent> chooseLeadLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String chosenClient = result.getData().getStringExtra("result_client");

                    Gson jsonConverter = new Gson();
                    counteragentLead = jsonConverter.fromJson(chosenClient, Client.class);
                    leadCounteragentNameView.setText(counteragentLead.getName());
                }
            });
    private final ActivityResultLauncher<Intent> chooseSecondaryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String chosenClient = result.getData().getStringExtra("result_client");

                    Gson jsonConverter = new Gson();
                    counteragentSecondary = jsonConverter.fromJson(chosenClient, Client.class);
                    secondaryCounteragentNameView.setText(counteragentSecondary.getName());
                }
            });
    private final ActivityResultLauncher<Intent> chooseObjectLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String chosenObject = result.getData().getStringExtra("result_object");

                    Gson jsonConverter = new Gson();
                    estateObject = jsonConverter.fromJson(chosenObject, EstateObject.class);
                    objectAddressView.setText(estateObject.getAddress());
                }
            });
    private final ActivityResultLauncher<Intent> chooseHostLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String chosenHost = result.getData().getStringExtra("result_employee");

                    Gson jsonConverter = new Gson();
                    host = jsonConverter.fromJson(chosenHost, Employee.class);
                    hostNameView.setText(host.getName());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_operation);
        inflateToolbar(null);

        String previousLeadOperation = getIntent().getStringExtra("prev_lead");
        String previousSecondaryOperation = getIntent().getStringExtra("prev_secondary");
        final String token = getIntent().getStringExtra("token");

        if (token != null && !token.isBlank()) {
            operationName = findViewById(R.id.createOpName);
            operationDate = findViewById(R.id.createOpDate);
            operationTime = findViewById(R.id.createOpTime);
            leadCounteragentNameView = findViewById(R.id.createOpFirstClientName);
            pickFirstClientButton = findViewById(R.id.pickFirstClientButton);
            secondaryCounteragentNameView = findViewById(R.id.createOpSecondClientName);
            pickSecondClientButton = findViewById(R.id.pickSecondClientButton);
            objectAddressView = findViewById(R.id.createOpObjectAddress);
            pickObjectButton = findViewById(R.id.pickObjectButton);
            hostNameView = findViewById(R.id.createOpHostName);
            pickHostButton = findViewById(R.id.pickHostButton);
            createButton = findViewById(R.id.createOpButton);

            if (previousLeadOperation != null && !previousLeadOperation.isBlank()) {
                Gson jsonConverter = new Gson();
                leadOperation = jsonConverter.fromJson(previousLeadOperation, Operation.class);

                httpApi.getClientById("Bearer " + token, leadOperation.getCounteragentLead()).enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        if (response.isSuccessful()) {
                            counteragentLead = response.body();

                            leadCounteragentNameView.setText(counteragentLead.getName());
                            pickFirstClientButton.setEnabled(false);
                            pickFirstClientButton.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.gray));
                        } else {
                            Toast.makeText(getApplicationContext(), "Помилка під час підтягування головного " +
                                    "контрагента з ланцюга операцій", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Помилка під час підтягування головного " +
                                       "контрагента з ланцюга операцій(" + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                httpApi.getEstateObjectById("Bearer " + token, leadOperation.getEstateObjectId()).enqueue(new Callback<EstateObject>() {
                    @Override
                    public void onResponse(Call<EstateObject> call, Response<EstateObject> response) {
                        if (response.isSuccessful()) {
                            estateObject = response.body();

                            objectAddressView.setText(estateObject.getAddress());
                            pickObjectButton.setEnabled(false);
                            pickObjectButton.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.gray));
                        } else {
                            Toast.makeText(getApplicationContext(), "Помилка під час підтягування об'єкта з ланцюга операцій", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<EstateObject> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Помилка під час підтягування об'єкта з ланцюга операцій("
                                       + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            if (previousSecondaryOperation != null && !previousSecondaryOperation.isBlank()) {
                Gson jsonConverter = new Gson();
                secondaryOperation = jsonConverter.fromJson(previousSecondaryOperation, Operation.class);

                httpApi.getClientById("Bearer " + token,
                                      secondaryOperation.getCounteragentSecondary() == null
                                      ? secondaryOperation.getCounteragentLead()
                                      : secondaryOperation.getCounteragentSecondary())
                        .enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        if (response.isSuccessful()) {
                            counteragentSecondary = response.body();

                            secondaryCounteragentNameView.setText(counteragentSecondary.getName());
                            pickSecondClientButton.setEnabled(false);
                            pickSecondClientButton.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.gray));
                        } else {
                            Toast.makeText(getApplicationContext(), "Помилка під час підтягування другого " +
                                    "контрагента з ланцюга операцій", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Помилка під час підтягування другого " +
                                "контрагента з ланцюга операцій(" + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer operationId = null;
                    String name = operationName.getText().toString();
                    String date = operationDate.getText().toString();
                    String time = operationTime.getText().toString();
                    int counteragentLeadId = 0;
                    Integer counteragentSecondaryId = null;
                    Integer estateObjectId = null;
                    int hostId = 0;
                    String status = "Очікується";
                    String actType = null;
                    Integer fkOperationLead = null;
                    Integer fkOperationSecondary = null;

                    boolean checkPassFlag = true;
                    if (counteragentLead == null || host == null) {
                        Toast.makeText(getApplicationContext(), "Оберіть обов'язкові поля"
                                       + "(перший контрагент, хост)", Toast.LENGTH_LONG).show();
                        checkPassFlag = false;
                    }
                    if (name.isBlank()) {
                        operationName.setError("Назва операції не може бути порожньою");
                        checkPassFlag = false;
                    }
                    if (!InputFieldsValidation.checkDate(date)) {
                        operationDate.setError("Дата повинна відповідати формату рік(4 цифри)-місяць(2 цифри)-день(2 цифри)");
                        checkPassFlag = false;
                    }
                    if (!InputFieldsValidation.checkTime(time)) {
                        operationTime.setError("Час повинен відповідати формату година(2 цифри):хвилини(2 цифри):секунди(2 цифри)");
                        checkPassFlag = false;
                    }

                    if (counteragentLead == null) {
                        Toast.makeText(getApplicationContext(), "Оберіть головного(першого) контрагента", Toast.LENGTH_LONG).show();
                        checkPassFlag = false;
                    } else {
                        counteragentLeadId = counteragentLead.getClientId();
                    }

                    if (counteragentSecondary != null) {
                        counteragentSecondaryId = counteragentSecondary.getClientId();
                    }

                    if ((leadOperation == null && secondaryOperation != null)
                            || (leadOperation != null && secondaryOperation == null)) {
                        Toast.makeText(getApplicationContext(), "Не було підтягнуто усі минулі операції", Toast.LENGTH_LONG).show();
                        checkPassFlag = false;
                    } else if (leadOperation != null && secondaryOperation != null) {
                        fkOperationLead = leadOperation.getOperationId();
                        fkOperationSecondary = secondaryOperation.getOperationId();
                    }

                    if (estateObject != null) {
                        estateObjectId = estateObject.getEstateObjectId();
                    }

                    if (host == null) {
                        Toast.makeText(getApplicationContext(), "Оберіть хоста", Toast.LENGTH_LONG).show();
                        checkPassFlag = false;
                    } else {
                        hostId = host.getEmployeeId();
                    }

                    if (checkPassFlag) {
                        Operation newOperation = new Operation(operationId, name, date + " " + time,
                                                               counteragentLeadId, counteragentSecondaryId,
                                                               estateObjectId, hostId, null, status,
                                                               actType, fkOperationLead, fkOperationSecondary);

                        httpApi.insertOperation("Bearer " + token, newOperation).enqueue(new Callback<Operation>() {
                            @Override
                            public void onResponse(Call<Operation> call, Response<Operation> response) {
                                if (response.isSuccessful()) {
                                    Operation returnOperation = response.body();
                                    finishCreatingWithResult("Операція успішно додана!", returnOperation);
                                } else {
                                    finishCreatingWithError("Помилка при збереженні нової операції. Спробуйте знову");
                                }
                            }

                            @Override
                            public void onFailure(Call<Operation> call, Throwable throwable) {
                                finishCreatingWithError("Помилка при збереженні нової операції("
                                                        + throwable.getMessage() + "). Спробуйте знову");
                            }
                        });
                    }
                }
            });
        }

        pickFirstClientButton = findViewById(R.id.pickFirstClientButton);
        pickSecondClientButton = findViewById(R.id.pickSecondClientButton);
        pickObjectButton = findViewById(R.id.pickObjectButton);
        pickHostButton = findViewById(R.id.pickHostButton);

        pickFirstClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromCreateOperation(ClientsSearchActivity.class, "lead");
            }
        });
        pickSecondClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromCreateOperation(ClientsSearchActivity.class, "secondary");
            }
        });
        pickObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromCreateOperation(ObjectsSearchActivity.class, "objects");
            }
        });
        pickHostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromCreateOperation(EmployeeSearchActivity.class, "hosts");
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

    private void cameFromCreateOperation(Class<?> activityClass, String launcherType) {
        Intent moveToActivity = new Intent(getApplicationContext(), activityClass);
        moveToActivity.putExtra("login", getIntent().getStringExtra("login"));
        moveToActivity.putExtra("role", getIntent().getStringExtra("role"));
        moveToActivity.putExtra("token", getIntent().getStringExtra("token"));
        moveToActivity.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

        moveToActivity.putExtra("FROM_CREATE_OPERATION", true);

        String employeeNameExtra = getIntent().getStringExtra("employeeName");

        if (employeeNameExtra != null) {
            moveToActivity.putExtra("employeeName", employeeNameExtra);
        }

        if (launcherType.equals("lead")) {
            chooseLeadLauncher.launch(moveToActivity);
        } else if (launcherType.equals("secondary")) {
            chooseSecondaryLauncher.launch(moveToActivity);
        } else if (launcherType.equals("objects")) {
            chooseObjectLauncher.launch(moveToActivity);
        } else {
            chooseHostLauncher.launch(moveToActivity);
        }

    }

    private void finishCreatingWithResult(String message, Operation operation) {
        Gson jsonConverter = new Gson();
        String insertedOperation = jsonConverter.toJson(operation);

        Intent moveToOperationDetailed = new Intent(getApplicationContext(), OperationsDetailedActivity.class);
        moveToOperationDetailed.putExtra("chosen_operation", insertedOperation);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        passEmployeeData(moveToOperationDetailed);
        finish();
    }

    private void finishCreatingWithError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        finish();
    }
}