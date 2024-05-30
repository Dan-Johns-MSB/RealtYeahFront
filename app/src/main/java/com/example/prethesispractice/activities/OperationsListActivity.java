package com.example.prethesispractice.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.adapters.OperationAdapter;
import com.example.prethesispractice.entities.Operation;
import com.example.prethesispractice.models.constants.ActTypesConst;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationsListActivity extends ToolbarMenuActivity {
    private List<Operation> operations;
    private OperationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_list);
        inflateToolbar(null);

        final String token = getIntent().getStringExtra("token");
        int client = getIntent().getIntExtra("FROM_CLIENTS_DETAILED", 0);
        int object = getIntent().getIntExtra("FROM_OBJECTS_DETAILED", 0);
        int employee = getIntent().getIntExtra("FROM_EMPLOYEES_DETAILED", 0);
        String previousLeadOperation = getIntent().getStringExtra("prev_lead");
        String previousSecondaryOperation = getIntent().getStringExtra("prev_secondary");

        Gson jsonConverter = new Gson();

        startAdapter();
        if (client > 0) {
            httpApi.getClientOperations("Bearer " + token, client).enqueue(new Callback<List<Operation>>() {
                @Override
                public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                    if (response.isSuccessful()) {
                        operations = response.body();

                        adapter.setOperations(operations);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Operation>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else if (object > 0) {
            httpApi.getEstateObjectOperations("Bearer " + token, object).enqueue(new Callback<List<Operation>>() {
                @Override
                public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                    if (response.isSuccessful()) {
                        operations = response.body();

                        adapter.setOperations(operations);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Operation>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else if (employee > 0) {
            httpApi.getEmployeeRelatedOperations("Bearer " + token, employee).enqueue(new Callback<List<Operation>>() {
                @Override
                public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                    if (response.isSuccessful()) {
                        operations = response.body();

                        adapter.setOperations(operations);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Operation>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else if (previousLeadOperation != null && !previousLeadOperation.isBlank()) {
            Operation leadOperation = jsonConverter.fromJson(previousLeadOperation, Operation.class);

            if (leadOperation.getActType().equals(ActTypesConst.SELL_AGENT_DEAL)) {
                getOperationsByActiveAct(token, ActTypesConst.BUY_AGENT_DEAL);
            } else if (leadOperation.getActType().equals(ActTypesConst.FOR_RENT_AGENT_DEAL)) {
                getOperationsByActiveAct(token, ActTypesConst.RENT_AGENT_DEAL);
            }
        } else if (previousSecondaryOperation != null && !previousSecondaryOperation.isBlank()) {
            Operation secondaryOperation = jsonConverter.fromJson(previousSecondaryOperation, Operation.class);

            if (secondaryOperation.getActType().equals(ActTypesConst.BUY_AGENT_DEAL)) {
                getOperationsByActiveAct(token, ActTypesConst.SELL_AGENT_DEAL);
            } else if (secondaryOperation.getActType().equals(ActTypesConst.RENT_AGENT_DEAL)) {
                getOperationsByActiveAct(token, ActTypesConst.FOR_RENT_AGENT_DEAL);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Couldn't load operations because client or object hasn't been specified", Toast.LENGTH_LONG).show();
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

    private void getOperationsByActiveAct(String token, String actType) {
        httpApi.getOperationsByActiveAct("Bearer " + token, actType).enqueue(new Callback<List<Operation>>() {
            @Override
            public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                if (response.isSuccessful()) {
                    operations = response.body();

                    adapter.setOperations(operations);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Не вдалося завантажити операції", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Operation>> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Не вдалося завантажити операції(" + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startAdapter() {
        RecyclerView clientsSearchResult = findViewById(R.id.operationsListView);
        clientsSearchResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (operations == null) {
            operations = new ArrayList<Operation>();
        }
        adapter = new OperationAdapter(getApplicationContext(), this, operations);
        clientsSearchResult.setAdapter(adapter);
    }
}