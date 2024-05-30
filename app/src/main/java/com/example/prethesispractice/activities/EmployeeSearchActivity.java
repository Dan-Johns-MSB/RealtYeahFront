package com.example.prethesispractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.adapters.EmployeeAdapter;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.models.constants.Role;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSearchActivity extends ToolbarMenuActivity {
    private Button addEmployeeButton;
    private SearchView searchView;
    private Switch searchInNamesSwitch;
    private Switch searchInPhoneNumbersSwitch;
    private Switch searchInEmailsSwitch;
    private Switch searchInAddressSwitch;
    private List<Employee> employees;
    private EmployeeAdapter adapter;
    private final ActivityResultLauncher<Intent> createEmployeeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String createdEmployee = result.getData().getStringExtra("result_employee");

                    Gson jsonConverter = new Gson();
                    employees.add(0, jsonConverter.fromJson(createdEmployee, Employee.class));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_search);
        inflateToolbar(null);

        addEmployeeButton = findViewById(R.id.employeesSearchAddEmployeeButton);
        String currentRole = getIntent().getStringExtra("role");
        if (!Role.MAIN_ADMIN.equals(currentRole) && !Role.ADMIN.equals(currentRole)) {
            addEmployeeButton.setEnabled(false);
            addEmployeeButton.setVisibility(Button.GONE);
        }

        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToEmployeeCreate = new Intent(getApplicationContext(), CreateEmployeeActivity.class);
                moveToEmployeeCreate.putExtra("login", getIntent().getStringExtra("login"));
                moveToEmployeeCreate.putExtra("role", getIntent().getStringExtra("role"));
                moveToEmployeeCreate.putExtra("token", getIntent().getStringExtra("token"));
                moveToEmployeeCreate.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

                String employeeNameExtra = getIntent().getStringExtra("employeeName");

                if (employeeNameExtra != null) {
                    moveToEmployeeCreate.putExtra("employeeName", employeeNameExtra);
                }

                createEmployeeLauncher.launch(moveToEmployeeCreate);
            }
        });

        final String token = getIntent().getStringExtra("token");

        startAdapter();

        searchView = findViewById(R.id.employeeSearchView);
        searchInNamesSwitch = findViewById(R.id.employeesSearchInNamesOption);
        searchInPhoneNumbersSwitch = findViewById(R.id.employeesSearchInPhoneNumbersOption);
        searchInEmailsSwitch = findViewById(R.id.employeesSearchInEmailsOption);

        searchInNamesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInPhoneNumbersSwitch.setChecked(false);
                    searchInEmailsSwitch.setChecked(false);

                    List<Employee> filteredEmployees = new ArrayList<Employee>();
                    for (Employee employee : employees) {
                        if (employee.getName().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredEmployees.add(employee);
                        }
                    }

                    changeAdapterDataset(filteredEmployees);
                } else if (!searchInPhoneNumbersSwitch.isChecked()
                        && !searchInEmailsSwitch.isChecked()) {
                    changeAdapterDataset(employees);
                }
            }
        });
        searchInPhoneNumbersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInNamesSwitch.setChecked(false);
                    searchInEmailsSwitch.setChecked(false);

                    List<Employee> filteredEmployees = new ArrayList<Employee>();
                    for (Employee employee : employees) {
                        if (employee.getPhoneNumber().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredEmployees.add(employee);
                        }
                    }

                    changeAdapterDataset(filteredEmployees);
                } else if (!searchInNamesSwitch.isChecked()
                        && !searchInEmailsSwitch.isChecked()) {
                    changeAdapterDataset(employees);
                }
            }
        });
        /*searchInEmailsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInNamesSwitch.setChecked(false);
                    searchInPhoneNumbersSwitch.setChecked(false);
                    searchInAddressSwitch.setChecked(false);

                    List<Employee> filteredEmployees = new ArrayList<Employee>();
                    for (Employee employee : employees) {
                        if (employee.getEmail().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredEmployees.add(employee);
                        }
                    }

                    changeAdapterDataset(filteredEmployees);
                } else if (!searchInNamesSwitch.isChecked()
                        && !searchInPhoneNumbersSwitch.isChecked()
                        && !searchInAddressSwitch.isChecked()) {
                    changeAdapterDataset(employees);
                }
            }
        });*/

        if (getIntent().getBooleanExtra("FROM_CREATE_OPERATION", false)) {
            httpApi.getEmployeesByStatus("Bearer " + token, "Активний").enqueue(new Callback<List<Employee>>() {
                @Override
                public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                    if (response.isSuccessful()) {
                        employees = response.body();

                        changeAdapterDataset(employees);
                    }
                }

                @Override
                public void onFailure(Call<List<Employee>> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            httpApi.getEmployees("Bearer " + token).enqueue(new Callback<List<Employee>>() {
                @Override
                public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                    if (response.isSuccessful()) {
                        employees = response.body();

                        changeAdapterDataset(employees);
                    }
                }

                @Override
                public void onFailure(Call<List<Employee>> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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

    private void startAdapter() {
        RecyclerView hostsSearchResult = findViewById(R.id.employeeSearchResult);
        hostsSearchResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (employees == null) {
            employees = new ArrayList<Employee>();
        }
        adapter = new EmployeeAdapter(getApplicationContext(), this, employees);
        hostsSearchResult.setAdapter(adapter);
    }

    private void changeAdapterDataset(List<Employee> changedEmployees) {
        if (adapter == null) {
            startAdapter();
        }

        adapter.setEmployees(changedEmployees);
        adapter.notifyDataSetChanged();
    }
}