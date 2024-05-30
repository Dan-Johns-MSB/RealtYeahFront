package com.example.prethesispractice.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.adapters.OperationAdapter;
import com.example.prethesispractice.entities.Operation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationsActivity extends ToolbarMenuActivity {

    private CalendarView calendarView;
    private String selectedDate;
    private Button searchButton;
    private CheckBox onlyMyCheckBox;
    private Button addOpButton;
    private List<Operation> operations;
    private OperationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        inflateToolbar(null);

        calendarView = findViewById(R.id.calendarView);
        searchButton = findViewById(R.id.operationsSearchButton);
        onlyMyCheckBox = findViewById(R.id.checkOnlyMyOperations);
        addOpButton = findViewById(R.id.operationsAddOpButton);

        final String token = getIntent().getStringExtra("token");

        LocalDate curDate = Instant.ofEpochMilli(calendarView.getDate()).atZone(ZoneId.systemDefault()).toLocalDate();
        selectedDate = curDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                LocalDate date = LocalDate.of(year, month + 1, day);
                selectedDate = date.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            }
        });

        addOpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicActivitySwitch(CreateOperationActivity.class);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onlyMyCheckBox.isChecked()) {
                    httpApi.getOperationsByDateAndHost("Bearer " + token, selectedDate, selectedDate,
                                    getIntent().getIntExtra("employee_id", 0))
                            .enqueue(new Callback<List<Operation>>() {
                                @Override
                                public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                                    if (response.isSuccessful()) {
                                        operations = response.body();

                                        startAdapter();
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Operation>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    httpApi.getOperationsByDate("Bearer " + token, selectedDate, selectedDate)
                            .enqueue(new Callback<List<Operation>>() {
                                @Override
                                public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                                    if (response.isSuccessful()) {
                                        operations = response.body();

                                        startAdapter();
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Operation>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
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

    private void startAdapter() {
        RecyclerView operationsResult = findViewById(R.id.operationsPageOpRecyclerView);
        operationsResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (operations == null) {
            operations = new ArrayList<Operation>();
        }
        adapter = new OperationAdapter(getApplicationContext(), this, operations);
        operationsResult.setAdapter(adapter);
    }
}