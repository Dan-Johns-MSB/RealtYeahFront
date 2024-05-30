package com.example.prethesispractice.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.entities.EstateObject;
import com.example.prethesispractice.entities.Operation;
import com.example.prethesispractice.models.constants.ActTypesConst;
import com.example.prethesispractice.models.constants.Role;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationsDetailedActivity extends ToolbarMenuActivity {
    private TextView operationName;
    private TextView operationDate;
    private TextView operationType;
    private TextView firstClientView;
    private TextView secondClientView;
    private TextView objectAddress;
    private TextView hostName;
    private TextView operationStatus;
    private Button createNextButton;
    private Button watchDocsButton;
    private Operation operation;
    private Client firstClient;
    private Client secondClient;
    private EstateObject estateObject;
    private Employee host;
    private ArrayList<Boolean> availableActs;
    private ArrayList<Button> actButtons;
    private final ActivityResultLauncher<Intent> assignActLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String resultOperation = result.getData().getStringExtra("result_operation");

                    final String token = getIntent().getStringExtra("token");
                    Gson jsonConverter = new Gson();
                    operation = jsonConverter.fromJson(resultOperation, Operation.class);

                    getAvailableActs(token);
                    displayOperationInfo(token);
                    getNextAvailability(token);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_detailed);
        inflateToolbar(null);

        operationName = findViewById(R.id.detailedOpName);
        operationDate = findViewById(R.id.detailedOpDate);
        operationType = findViewById(R.id.detailedOpType);
        firstClientView = findViewById(R.id.detailedOpClient1);
        secondClientView = findViewById(R.id.detailedOpClient2);
        objectAddress = findViewById(R.id.detailedOpObject);
        hostName = findViewById(R.id.detailedOpRealtor);
        operationStatus = findViewById(R.id.detailedOpStatus);

        String chosenOperation = getIntent().getStringExtra("chosen_operation");
        if (chosenOperation == null || chosenOperation.isBlank()) {
            Toast.makeText(getApplicationContext(), "No operation data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        Gson jsonConverter = new Gson();
        operation = jsonConverter.fromJson(chosenOperation, Operation.class);

        final String token = getIntent().getStringExtra("token");
        displayOperationInfo(token);

        createNextButton = findViewById(R.id.createNextOperationButton);
        getNextAvailability(token);

        actButtons = new ArrayList<Button>();
        actButtons.add(findViewById(R.id.beginBuyOpButton));
        actButtons.add(findViewById(R.id.beginSellOpButton));
        actButtons.add(findViewById(R.id.beginRentOpButton));
        actButtons.add(findViewById(R.id.beginForRentOpButton));
        actButtons.add(findViewById(R.id.objectCheckOpButton));
        actButtons.add(findViewById(R.id.pledgeOpButton));
        actButtons.add(findViewById(R.id.finalOpButton));
        actButtons.add(findViewById(R.id.cancelOpButton));
        actButtons.add(findViewById(R.id.deleteOpButton));

        getAvailableActs(token);

        firstClientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientClick(firstClient);
            }
        });
        secondClientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientClick(secondClient);
            }
        });
        objectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objectClick();
            }
        });
        hostName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hostClick();
            }
        });

        actButtons.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.BUY_AGENT_DEAL);
            }
        });
        actButtons.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.SELL_AGENT_DEAL);
            }
        });
        actButtons.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.RENT_AGENT_DEAL);
            }
        });
        actButtons.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.FOR_RENT_AGENT_DEAL);
            }
        });
        actButtons.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.OBJECT_CHECK);
            }
        });
        actButtons.get(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.PLEDGE);
            }
        });
        actButtons.get(6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAssignAct(ActTypesConst.FINAL_DEAL);
            }
        });

        String role = getIntent().getStringExtra("role");
        int employeeId = getIntent().getIntExtra("employee_id", 0);

        if (Role.MAIN_ADMIN.equals(role) || Role.ADMIN.equals(role) || operation.getHostId() == employeeId) {
            actButtons.get(7).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder forCancel = new AlertDialog.Builder(OperationsDetailedActivity.this);
                    forCancel.setTitle(R.string.operations_cancel_op_button);
                    forCancel.setMessage(R.string.cancel_op_message);
                    forCancel.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            httpApi.cancelOperation("Bearer " + token, operation.getOperationId()).enqueue(new Callback<Operation>() {
                                @Override
                                public void onResponse(Call<Operation> call, Response<Operation> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Операцію успішно відмінено/зірвано!", Toast.LENGTH_LONG).show();
                                        operation = response.body();

                                        getAvailableActs(token);
                                        displayOperationInfo(token);
                                        getNextAvailability(token);
                                    } else {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час відміни операції("
                                                    + response.errorBody().string() + ")", Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час відміни операції", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Operation> call, Throwable throwable) {
                                    Toast.makeText(getApplicationContext(), "Сталася помилка під час відміни операції("
                                            + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    forCancel.setNegativeButton(R.string.alert_dialog_goBack, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = forCancel.create();
                    alertDialog.show();
                }
            });
        }

        if (Role.MAIN_ADMIN.equals(role) || Role.ADMIN.equals(role)) {
            actButtons.get(8).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder forDelete = new AlertDialog.Builder(OperationsDetailedActivity.this);
                    forDelete.setTitle(R.string.operations_delete_op_button);
                    forDelete.setMessage(R.string.delete_op_message);
                    forDelete.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            httpApi.deleteOperation("Bearer " + token, operation.getOperationId()).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Операцію успішно видалено!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення операції("
                                                    + response.errorBody().string() + ")", Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення операції", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                    Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення операції("
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

        watchDocsButton = findViewById(R.id.watchDocsOpButton);
        watchDocsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(WatchDocumentsActivity.class);
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

    private void getAvailableActs(String token) {
        httpApi.getAvailableActs("Bearer " + token, operation.getOperationId()).enqueue(new Callback<List<Boolean>>() {
            @Override
            public void onResponse(Call<List<Boolean>> call, Response<List<Boolean>> response) {
                if (response.isSuccessful()) {
                    availableActs = new ArrayList<Boolean>(response.body());

                    for (Button actButton : actButtons) {
                        actButton.setEnabled(availableActs.get(actButtons.indexOf(actButton)));

                        if (!availableActs.get(actButtons.indexOf(actButton))) {
                            actButton.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.gray));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Boolean>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clientClick(Client client) {
        Intent moveToClient = new Intent(getApplicationContext(), ClientsDetailedActivity.class);

        Gson jsonConverter = new Gson();
        String chosenClient = jsonConverter.toJson(client);
        moveToClient.putExtra("chosen_client", chosenClient);

        passEmployeeData(moveToClient);
    }

    private void objectClick() {
        if (estateObject != null) {
            Intent moveToObject = new Intent(getApplicationContext(), ObjectsDetailedActivity.class);

            Gson jsonConverter = new Gson();
            String chosenObject = jsonConverter.toJson(estateObject);
            moveToObject.putExtra("chosen_object", chosenObject);

            passEmployeeData(moveToObject);
        }
    }

    private void hostClick() {
        Intent moveToHost = new Intent(getApplicationContext(), EmployeesDetailedActivity.class);

        Gson jsonConverter = new Gson();
        String chosenHost = jsonConverter.toJson(host);
        moveToHost.putExtra("chosen_employee", chosenHost);

        passEmployeeData(moveToHost);
    }

    private void displayOperationInfo(String token) {
        if (operation == null) {
            Toast.makeText(getApplicationContext(), "No operation data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        operationName.setText(operation.getName());
        operationDate.setText(operation.getDateFormatted());
        operationType.setText(operation.getActType());
        operationStatus.setText(operation.getStatus());

        httpApi.getClientById("Bearer " + token, operation.getCounteragentLead())
            .enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if (response.isSuccessful()) {
                        firstClient = response.body();
                        firstClientView.setText(firstClient.getName());
                    }
                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        if (operation.getCounteragentSecondary() == null) {
            secondClientView.setText(R.string.unknown_value);
        } else {
            httpApi.getClientById("Bearer " + token, operation.getCounteragentSecondary())
                .enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        if (response.isSuccessful()) {
                            secondClient = response.body();
                            secondClientView.setText(secondClient.getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }

        if (operation.getEstateObjectId() == null) {
            objectAddress.setText(R.string.unknown_value);
        } else {
            httpApi.getEstateObjectById("Bearer " + token, operation.getEstateObjectId())
                .enqueue(new Callback<EstateObject>() {
                    @Override
                    public void onResponse(Call<EstateObject> call, Response<EstateObject> response) {
                        if (response.isSuccessful()) {
                            estateObject = response.body();
                            objectAddress.setText(estateObject.getAddress());
                        }
                    }

                    @Override
                    public void onFailure(Call<EstateObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }

        httpApi.getHostByOperation("Bearer " + token, operation.getOperationId()).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    host = response.body();
                    hostName.setText(host.getName());
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void moveToAssignAct(String actType) {
        Gson jsonConverter = new Gson();
        String operationToAssign = jsonConverter.toJson(operation);

        Intent moveToApplyDocuments = new Intent(getApplicationContext(), AssignActActivity.class);
        moveToApplyDocuments.putExtra("act_type", actType);
        moveToApplyDocuments.putExtra("operation", operationToAssign);

        passEmployeeData(moveToApplyDocuments);
    }

    private void getNextAvailability(String token) {
        httpApi.getNextAvailability("Bearer " + token, operation.getOperationId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body()) {
                    createNextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Gson jsonConverter = new Gson();

                            if (operation.getCounteragentSecondary() != null) {
                                Intent toCreateChainOperation = new Intent(getApplicationContext(), CreateOperationActivity.class);
                                toCreateChainOperation.putExtra("prev_lead", jsonConverter.toJson(operation));
                                toCreateChainOperation.putExtra("prev_secondary", jsonConverter.toJson(operation));

                                passEmployeeData(toCreateChainOperation);
                            } else {
                                Intent toPickSecondOperation = new Intent(getApplicationContext(), OperationsListActivity.class);
                                toPickSecondOperation.putExtra("prev_lead", jsonConverter.toJson(operation));

                                passEmployeeData(toPickSecondOperation);
                            }
                        }
                    });

                    createNextButton.setEnabled(true);
                    createNextButton.setVisibility(Button.VISIBLE);
                } else {
                    createNextButton.setEnabled(false);
                    createNextButton.setVisibility(Button.GONE);
                    Toast.makeText(getApplicationContext(), "Не вдалося визначити можливість використання операції у ланцюгу", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Не вдалося визначити можливість використання операції у ланцюгу("
                        + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
            }
        });
    }
}