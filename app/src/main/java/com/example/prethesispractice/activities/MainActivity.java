package com.example.prethesispractice.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Employee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ToolbarMenuActivity {
    private ImageView employeePhotoView;
    private TextView employeeNameTextView;
    private TextView jobTextView;
    private TextView birthdateTextView;
    private TextView hiredateTextView;
    private TextView promotedateTextView;
    private Button infoDropdownButton;
    private TableLayout infoDropdown;
    private ConstraintLayout clientsButton;
    private ConstraintLayout objectsButton;
    private ConstraintLayout operationsButton;
    private ConstraintLayout employeesButton;
    private Button logoutButton;
    private int employeeId;
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String token = getIntent().getStringExtra("token");
        employeeId = getIntent().getIntExtra("employee_id", 0);

        employeePhotoView = findViewById(R.id.mainPageUserAvatarImageView);
        employeeNameTextView = findViewById(R.id.mainPageEmployeeNameTextView);
        jobTextView = findViewById(R.id.mainPageJobInfoText);
        birthdateTextView = findViewById(R.id.mainPageBirthdate);
        hiredateTextView = findViewById(R.id.mainPageHiredate);
        promotedateTextView = findViewById(R.id.mainPagePromotionDate);

        infoDropdownButton = findViewById(R.id.userInfoDropdownButton);
        infoDropdown = findViewById(R.id.tableLayout);

        infoDropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoDropdown.getVisibility() == ConstraintLayout.GONE) {
                    infoDropdown.setVisibility(ConstraintLayout.VISIBLE);
                    infoDropdownButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_keyboard_arrow_up_24, 0);
                } else {
                    infoDropdown.setVisibility(ConstraintLayout.GONE);
                    infoDropdownButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_keyboard_arrow_down_24, 0);
                }
            }
        });

        getEmployeeData(token);

        clientsButton = findViewById(R.id.mainOtherPageContainer1);
        clientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(ClientsActivity.class);
            }
        });

        objectsButton = findViewById(R.id.mainOtherPageContainer2);
        objectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(ObjectsActivity.class);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(LoginActivity.class);
            }
        });

        operationsButton = findViewById(R.id.mainOtherPageContainer3);
        operationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(OperationsActivity.class);
            }
        });

        employeesButton = findViewById(R.id.mainOtherPageContainer4);
        employeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(EmployeeSearchActivity.class);
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

    private void getEmployeeData(String token) {
        if ((employeeId > 0) && !token.isBlank())
            httpApi.getEmployeeById("Bearer " + token, employeeId).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    if (response.isSuccessful()) {
                        employee = response.body();
                        inflateToolbar(employee.getName());
                        getIntent().putExtra("employeeName", employee.getName());

                        int resourceId = getResources().getIdentifier(employee.getPhoto(), "drawable",
                                getPackageName());
                        if (resourceId != 0) {
                            employeePhotoView.setImageResource(resourceId);
                        }

                        employeeNameTextView.setText(employee.getName());
                        jobTextView.setText(employee.getJob());
                        birthdateTextView.setText(employee.getBirthdateFormatted());
                        hiredateTextView.setText(employee.getHiredateFormatted());
                        promotedateTextView.setText(employee.getPromotedateFormatted());
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        else {
            Toast.makeText(getApplicationContext(), "Error receiving data. Please restart app and try again",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }
}