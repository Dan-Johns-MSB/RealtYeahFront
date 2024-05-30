package com.example.prethesispractice.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Employee;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeesDetailedActivity extends ToolbarMenuActivity {
    private ImageView employeePhoto;
    private TextView employeeName;
    private TextView employeeJob;
    private TextView employeePhoneNumber;
    private TextView employeeEmail;
    private TextView employeeAddress;
    private TextView employeeBirthdate;
    private TextView employeeHiredate;
    private TextView employeePromotedate;
    private TextView employeeFiredate;
    private TextView employeeStatus;
    private Button watchEmployeeOperationsButton;
    private Button watchEmployeeClientsButton;
    private Button watchEmployeeObjectsButton;
    private Button employeeEditButton;
    private Button employeeEditUserButton;
    private Button employeeFireSavedButton;
    private Button employeeFireCompletelyButton;
    private Employee employee;
    private String token;
    private final ActivityResultLauncher<Intent> editEmployeeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String editedEmployee = result.getData().getStringExtra("result_employee");

                    Gson jsonConverter = new Gson();
                    employee = jsonConverter.fromJson(editedEmployee, Employee.class);
                    displayEmployeeInfo();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_detailed);
        inflateToolbar(null);

        employeePhoto = findViewById(R.id.employeeImage);
        employeeName = findViewById(R.id.employeeName);
        employeeJob = findViewById(R.id.employeeJob);
        employeePhoneNumber = findViewById(R.id.employeePhoneNumber);
        employeeEmail = findViewById(R.id.employeeEmail);
        employeeAddress = findViewById(R.id.employeeAddress);
        employeeBirthdate = findViewById(R.id.employeeBirthdate);
        employeeHiredate = findViewById(R.id.employeeHiredate);
        employeePromotedate = findViewById(R.id.employeePromotedate);
        employeeFiredate = findViewById(R.id.employeeFiredate);
        employeeStatus = findViewById(R.id.employeeStatus);
        watchEmployeeOperationsButton = findViewById(R.id.watchEmployeeOperationsButton);
        watchEmployeeClientsButton = findViewById(R.id.watchEmployeeClientsButton);
        watchEmployeeObjectsButton = findViewById(R.id.watchEmployeeObjectsButton);
        employeeEditButton = findViewById(R.id.employeeEditButton);
        employeeEditUserButton = findViewById(R.id.employeeEditUserButton);
        employeeFireSavedButton = findViewById(R.id.employeeFireSavedButton);
        employeeFireCompletelyButton = findViewById(R.id.employeeFireCompletelyButton);

        String role = getIntent().getStringExtra("role");
        int userEmployeeId = getIntent().getIntExtra("employee_id", 0);

        if (role == null || (!role.equals("admin") && !role.equals("mainadmin") && !(role.equals("agent") && employee.getEmployeeId() == userEmployeeId))) {
            employeeAddress.setVisibility(TextView.GONE);
            employeeBirthdate.setVisibility(TextView.GONE);
            employeeHiredate.setVisibility(TextView.GONE);
            employeePromotedate.setVisibility(TextView.GONE);
            employeeFiredate.setVisibility(TextView.GONE);
        } else if (!role.equals("admin") && !role.equals("mainadmin")) {
            employeeEditButton.setEnabled(false);
            employeeEditButton.setVisibility(Button.GONE);
            employeeEditUserButton.setEnabled(false);
            employeeEditUserButton.setVisibility(Button.GONE);
        } else if (!role.equals("mainadmin")) {
            employeeFireSavedButton.setEnabled(false);
            employeeFireSavedButton.setVisibility(Button.GONE);
            employeeFireCompletelyButton.setEnabled(false);
            employeeFireCompletelyButton.setVisibility(Button.GONE);
        }

        String chosenEmployee = getIntent().getStringExtra("chosen_employee");
        if (chosenEmployee == null || chosenEmployee.isBlank()) {
            Toast.makeText(getApplicationContext(), "No employee data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        Gson jsonConverter = new Gson();
        employee = jsonConverter.fromJson(chosenEmployee, Employee.class);

        token = getIntent().getStringExtra("token");

        displayEmployeeInfo();

        watchEmployeeOperationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromEmployeesDetailed(OperationsListActivity.class, employee.getEmployeeId());
            }
        });
        watchEmployeeClientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromEmployeesDetailed(ClientsSearchActivity.class, employee.getEmployeeId());
            }
        });
        watchEmployeeObjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromEmployeesDetailed(ObjectsSearchActivity.class, employee.getEmployeeId());
            }
        });

        employeeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToEmployeeEdit = new Intent(getApplicationContext(), CreateEmployeeActivity.class);
                moveToEmployeeEdit.putExtra("login", getIntent().getStringExtra("login"));
                moveToEmployeeEdit.putExtra("role", getIntent().getStringExtra("role"));
                moveToEmployeeEdit.putExtra("token", getIntent().getStringExtra("token"));
                moveToEmployeeEdit.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

                Gson jsonConverter = new Gson();
                String employeeJson = jsonConverter.toJson(employee);
                moveToEmployeeEdit.putExtra("FROM_EMPLOYEES_DETAILED", employeeJson);

                String employeeNameExtra = getIntent().getStringExtra("employeeName");

                if (employeeNameExtra != null) {
                    moveToEmployeeEdit.putExtra("employeeName", employeeNameExtra);
                }

                editEmployeeLauncher.launch(moveToEmployeeEdit);
            }
        });

        employeeEditUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromEmployeesDetailed(CreateAccountActivity.class, employee.getEmployeeId());
            }
        });

        employeeFireSavedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder forFireSaved = new AlertDialog.Builder(EmployeesDetailedActivity.this);
                forFireSaved.setTitle(R.string.employee_fire_saved_button);
                forFireSaved.setMessage(R.string.employee_fire_saved_message);
                forFireSaved.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        httpApi.fireEmployeeSaved("Bearer " + token, employee.getEmployeeId()).enqueue(new Callback<Employee>() {
                            @Override
                            public void onResponse(Call<Employee> call, Response<Employee> response) {
                                if (response.isSuccessful()) {
                                    employee = response.body();

                                    Toast.makeText(getApplicationContext(), "Робітника звільнено без видалення даних", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Не вдалося звільнити робітника "
                                            + "без видалення даних. Спробуйте знову", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Employee> call, Throwable throwable) {
                                Toast.makeText(getApplicationContext(), "Не вдалося звільнити робітника "
                                        + "без видалення даних(" + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                forFireSaved.setNegativeButton(R.string.alert_dialog_goBack, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = forFireSaved.create();
                alertDialog.show();
            }
        });

        employeeFireCompletelyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder forFireSaved = new AlertDialog.Builder(EmployeesDetailedActivity.this);
                forFireSaved.setTitle(R.string.employee_fire_completely_button);
                forFireSaved.setMessage(R.string.employee_fire_completely_message);
                forFireSaved.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        httpApi.fireEmployee("Bearer " + token, employee.getEmployeeId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Робітника звільнено повністю", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Не вдалося звільнити робітника "
                                            + "повністю. Спробуйте знову", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                Toast.makeText(getApplicationContext(), "Не вдалося звільнити робітника "
                                        + "повністю(" + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                forFireSaved.setNegativeButton(R.string.alert_dialog_goBack, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = forFireSaved.create();
                alertDialog.show();
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

    private void displayEmployeeInfo() {
        if (employee == null) {
            Toast.makeText(getApplicationContext(), "No employee data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        httpApi.getEmployeeEmail("Bearer " + token, employee.getEmployeeId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    employeeEmail.setText(response.body());
                } else {
                    employeeEmail.setText(R.string.unknown_value);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // employeePhoto must be specified here
        int resourceId = getResources().getIdentifier(employee.getPhoto(), "drawable",
                getPackageName());
        if (resourceId != 0) {
            employeePhoto.setImageResource(resourceId);
        }

        employeeName.setText(employee.getName());
        employeeJob.setText(employee.getJob());
        employeePhoneNumber.setText(employee.getPhoneNumber());
        employeeAddress.setText(employee.getAddress());
        employeeBirthdate.setText(employee.getBirthdate());
        employeeHiredate.setText(employee.getHiredate());
        employeePromotedate.setText(employee.getPromotedate());
        employeeFiredate.setText(employee.getFiredate());
        employeeStatus.setText(employee.getStatus());
    }

    private void cameFromEmployeesDetailed(Class<?> activityClass, int data) {
        Intent moveToActivity = new Intent(getApplicationContext(), activityClass);

        if (data < 1) {
            data = employee.getEmployeeId();
        }

        moveToActivity.putExtra("FROM_EMPLOYEES_DETAILED", data);
        passEmployeeData(moveToActivity);
    }
}