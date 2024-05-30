package com.example.prethesispractice.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.helpers.InputFieldsValidation;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEmployeeActivity extends ToolbarMenuActivity {
    private ImageView employeePhotoView;
    private EditText employeeNameView;
    private EditText employeePhoneNumberView;
    private EditText employeeJobView;
    private EditText employeeBirthdateView;
    private EditText employeeAddressView;
    private EditText employeeHiredateView;
    private EditText employeePromotedateView;
    private EditText employeeFiredateView;
    private Spinner employeeStatusView;
    private Button createEmployeeButton;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);
        inflateToolbar(null);

        employeePhotoView = findViewById(R.id.uploadedPhoto);
        employeeNameView = findViewById(R.id.createEmployeeName);
        employeePhoneNumberView = findViewById(R.id.createEmployeePhoneNumber);
        employeeJobView = findViewById(R.id.createEmployeeJob);
        employeeBirthdateView = findViewById(R.id.createEmployeeBirthdate);
        employeeAddressView = findViewById(R.id.createEmployeeAddress);
        employeeHiredateView = findViewById(R.id.createEmployeeHiredate);
        employeePromotedateView = findViewById(R.id.createEmployeePromotedate);
        employeeFiredateView = findViewById(R.id.createEmployeeFiredate);
        employeeStatusView = findViewById(R.id.createEmployeeStatus);
        createEmployeeButton = findViewById(R.id.createObjectButton);

        ArrayList<String> statusList = new ArrayList<String>(
                Arrays.asList(getResources().getStringArray(R.array.employee_statuses))
        );
        statusList.remove("Звільнений");

        final String token = getIntent().getStringExtra("token");
        String fromEmployee = getIntent().getStringExtra("FROM_EMPLOYEES_DETAILED");
        if (fromEmployee != null && !fromEmployee.isBlank()) {
            Gson jsonConverter = new Gson();
            employee = jsonConverter.fromJson(fromEmployee, Employee.class);

            if (employee.getStatus().equals("Звільнений")) {
                statusList.add(0, employee.getStatus());
            }

            if (statusList.indexOf(employee.getStatus()) != 0) {
                statusList.set(statusList.indexOf(employee.getStatus()), statusList.get(0));
                statusList.set(0, employee.getStatus());
            }

            int resourceId = getResources().getIdentifier(employee.getPhoto(), "drawable",
                    getPackageName());
            if (resourceId != 0) {
                employeePhotoView.setImageResource(resourceId);
            }

            employeeNameView.setText(employee.getName());
            employeePhoneNumberView.setText(employee.getPhoneNumber());
            employeeJobView.setText(employee.getJob());
            employeeBirthdateView.setText(employee.getBirthdateDateOnly());
            employeeAddressView.setText(employee.getAddress());
            employeeHiredateView.setText(employee.getHiredateDateOnly());
            employeePromotedateView.setText(employee.getPromotedateDateOnly());
            employeeFiredateView.setText(employee.getFiredateDateOnly());
            createEmployeeButton.setText(R.string.employee_edit_button);
        }

        employeeHiredateView.setEnabled(false);
        employeeHiredateView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        employeePromotedateView.setEnabled(false);
        employeePromotedateView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        employeeFiredateView.setEnabled(false);
        employeeFiredateView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        ArrayAdapter<String> forStatusSpinner = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinners,
                statusList);
        forStatusSpinner.setDropDownViewResource(R.layout.spinners);
        employeeStatusView.setAdapter(forStatusSpinner);
        employeeStatusView.setEnabled(employee != null);

        createEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employeeName = employeeNameView.getText().toString();
                String employeePhoneNumber = employeePhoneNumberView.getText().toString();
                String employeeJob = employeeJobView.getText().toString();
                String employeeBirthdate = employeeBirthdateView.getText().toString();
                String employeeAddress = employeeAddressView.getText().toString();
                String employeeHiredate = employeeHiredateView.getText().toString();
                String employeePromotedate = employeePromotedateView.getText().toString();
                String employeeFiredate = employeeFiredateView.getText().toString();
                String employeeStatus = employeeStatusView.getSelectedItem().toString();

                if (employee != null && !employee.getJob().equals(employeeJob)) {
                    employeePromotedate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                }
                if (employee == null || employee.getStatus().equals("Звільнений") && !employee.getStatus().equals(employeeStatus)) {
                    employeeHiredate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                    employeePromotedate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
                    employeeFiredate = null;
                }

                boolean checkPassFlag = true;
                if (employeeName.isBlank()) {
                    employeeNameView.setError("Ім'я не може бути порожнім");
                    checkPassFlag = false;
                }
                if (employeePhoneNumber.isBlank() || employeePhoneNumber.length() > 13) {
                    employeePhoneNumberView.setError("Номер телефону повинен містити лише цифри без знаків,"
                            + "пробілів, та мати не більше 13 цифр");
                    checkPassFlag = false;
                }
                if (employeePhoneNumber.isBlank()) {
                    employeeJobView.setError("Посада робітника не може бути порожньою");
                    checkPassFlag = false;
                }
                if (!InputFieldsValidation.checkDate(employeeBirthdate)) {
                    employeeBirthdateView.setError("Дата повинна відповідати формату рік(4 цифри)-місяць(2 цифри)-день(2 цифри)");
                    checkPassFlag = false;
                }
                if (employeeAddress.isBlank()) {
                    employeeAddressView.setError("Адреса робітника не може бути порожньою");
                    checkPassFlag = false;
                }

                if (checkPassFlag) {
                    if (employee != null) {
                        employee = new Employee(employee.getEmployeeId(), "Unknown"/* this is photo if what */,
                                                employeeName, employeeJob, employeePhoneNumber,
                                                employeeBirthdate, employeeAddress, employeeHiredate,
                                                employeePromotedate, employeeFiredate, employeeStatus);

                        /*Log.d("bobrkurwa", Integer.toString(employee.getEmployeeId()));
                        Log.d("bobrkurwa", "Unknown");
                        Log.d("bobrkurwa", employeeName);
                        Log.d("bobrkurwa", employeeJob);
                        Log.d("bobrkurwa", employeePhoneNumber);
                        Log.d("bobrkurwa", employeeBirthdate);
                        Log.d("bobrkurwa", employeeAddress);
                        Log.d("bobrkurwa", employeeHiredate);
                        Log.d("bobrkurwa", employeePromotedate);
                        Log.d("bobrkurwa", employeeFiredate);
                        Log.d("bobrkurwa", employeeStatus);*/

//                        employee = new Employee(employee.getEmployeeId(), "Unknown"/* this is photo if what */,
//                                employeeName, employeeJob, employeePhoneNumber,
//                                employeeBirthdate + " 00:00:00", employeeAddress,
//                                employeeHiredate + " 00:00:00", employeePromotedate + " 00:00:00",
//                                employeeFiredate == null || employeeFiredate.isBlank() ? employeeFiredate : employeeFiredate + " 00:00:00", employeeStatus);

                        httpApi.updateEmployee("Bearer " + token, employee.getEmployeeId(), employee).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    finishEditingWithResult("Збережено");
                                } else {
                                    finishEditingWithError("Помилка при збереженні. Спробуйте знову");
                                    try {
                                        Log.d("bobrkurwa", Integer.toString(response.code()));
                                        Log.d("bobrkurwa", response.errorBody().string());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                finishEditingWithError("Помилка при збереженні відредагованого робітника("
                                        + throwable.getMessage() + "). Спробуйте знову");
                            }
                        });
                    } else {
                        employee = new Employee(null, "Unknown"/* this is photo if what */,
                                                employeeName, employeeJob, employeePhoneNumber, employeeBirthdate,
                                                employeeAddress, employeeHiredate, employeePromotedate,
                                                employeeFiredate, employeeStatus);

                        httpApi.insertEmployee("Bearer " + token, employee).enqueue(new Callback<Employee>() {
                            @Override
                            public void onResponse(Call<Employee> call, Response<Employee> response) {
                                if (response.isSuccessful()) {
                                    employee = response.body();
                                    finishEditingWithResult("Робітник успішно доданий!");
                                } else {
                                    finishEditingWithError("Помилка при збереженні нового робітника. Спробуйте знову");
                                }
                            }

                            @Override
                            public void onFailure(Call<Employee> call, Throwable throwable) {
                                finishEditingWithError("Помилка при збереженні нового робітника("
                                        + throwable.getMessage() + "). Спробуйте знову");
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
        String resultEmployee = jsonConverter.toJson(employee);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_employee", resultEmployee);
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