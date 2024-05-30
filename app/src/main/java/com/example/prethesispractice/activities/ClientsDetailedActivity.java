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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.adapters.RequirementsAdapter;
import com.example.prethesispractice.adapters.RequirementsViewHolder;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.ClientsStatusesAssignment;
import com.example.prethesispractice.models.constants.Role;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsDetailedActivity extends ToolbarMenuActivity {
    private Button buyReqButton;
    private ConstraintLayout buyReqDropdown;
    private Button sellReqButton;
    private ConstraintLayout sellReqDropdown;
    private Button toRentReqButton;
    private ConstraintLayout toRentReqDropdown;
    private Button forRentReqButton;
    private ConstraintLayout forRentReqDropdown;
    private ImageView clientPhoto;
    private TextView clientName;
    private TextView passportNumber;
    private TextView taxpayerCardNumber;
    private TextView phoneNumber;
    private TextView emailAddress;
    private TextView birthdate;
    private TextView birthplace;
    private TextView gender;
    private Button watchDocumentsButton;
    private Button watchObjectsButton;
    private Button watchOperationsButton;
    private Button editClientButton;
    private Button deleteClientButton;
    private Client client;
    private final ActivityResultLauncher<Intent> editClientLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String editedClient = result.getData().getStringExtra("result_client");

                    Gson jsonConverter = new Gson();
                    client = jsonConverter.fromJson(editedClient, Client.class);
                    displayClientInfo();
                }
            });
    private List<ClientsStatusesAssignment> buyStatusAssignments;
    private List<ClientsStatusesAssignment> sellStatusAssignments;
    private List<ClientsStatusesAssignment> rentStatusAssignments;
    private List<ClientsStatusesAssignment> forRentStatusAssignments;
    private RequirementsAdapter buyRequirementsAdapter;
    private RequirementsAdapter sellRequirementsAdapter;
    private RequirementsAdapter rentRequirementsAdapter;
    private RequirementsAdapter forRentRequirementsAdapter;
    private RecyclerView buyReqRecycler;
    private RecyclerView sellReqRecycler;
    private RecyclerView rentReqRecycler;
    private RecyclerView forRentReqRecycler;
    private ToolbarMenuActivity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_detailed);
        inflateToolbar(null);

        clientPhoto = findViewById(R.id.clientInfoPhoto);
        clientName = findViewById(R.id.clientInfoName);
        passportNumber = findViewById(R.id.clientInfoPassport);
        taxpayerCardNumber = findViewById(R.id.clientInfoTaxpayerCardNumber);
        phoneNumber = findViewById(R.id.clientInfoPhoneNumber);
        emailAddress = findViewById(R.id.clientInfoEmail);
        birthdate = findViewById(R.id.clientInfoBirthdate);
        birthplace = findViewById(R.id.clientInfoBirthplace);
        gender = findViewById(R.id.clientInfoGender);

        String chosenClient = getIntent().getStringExtra("chosen_client");
        if (chosenClient == null || chosenClient.isBlank()) {
            Toast.makeText(getApplicationContext(), "No client data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        Gson jsonConverter = new Gson();
        client = jsonConverter.fromJson(chosenClient, Client.class);

        displayClientInfo();

        // Associating reqButtons and dropdowns with corresponding variables
        buyReqButton = findViewById(R.id.clientInfoBuyReqButton);
        buyReqDropdown = findViewById(R.id.clientInfoBuyReqDropdown);
        sellReqButton = findViewById(R.id.clientInfoSellReqButton);
        sellReqDropdown = findViewById(R.id.clientInfoSellReqDropdown);
        toRentReqButton = findViewById(R.id.clientInfoToRentReqButton);
        toRentReqDropdown = findViewById(R.id.clientInfoToRentReqDropdown);
        forRentReqButton = findViewById(R.id.clientInfoForRentReqButton);
        forRentReqDropdown = findViewById(R.id.clientInfoForRentReqDropdown);
        buyReqRecycler = findViewById(R.id.buyReqRecyclerView);
        buyReqRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sellReqRecycler = findViewById(R.id.sellReqRecyclerView);
        sellReqRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rentReqRecycler = findViewById(R.id.rentReqRecyclerView);
        rentReqRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        forRentReqRecycler = findViewById(R.id.forRentReqRecyclerView);
        forRentReqRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final String token = getIntent().getStringExtra("token");
        currentActivity = this;

        httpApi.getBuyRequirements("Bearer " + token, client.getClientId()).enqueue(new Callback<List<ClientsStatusesAssignment>>() {
            @Override
            public void onResponse(Call<List<ClientsStatusesAssignment>> call, Response<List<ClientsStatusesAssignment>> response) {
                if (response.isSuccessful()) {
                    buyStatusAssignments = response.body();

                    if (buyStatusAssignments != null && !buyStatusAssignments.isEmpty()) {
                        buyRequirementsAdapter = new RequirementsAdapter(getApplicationContext(), currentActivity, buyStatusAssignments, token);
                        buyReqRecycler.setAdapter(buyRequirementsAdapter);
                    }
                    buyReqButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dropdownButtonsClick("buy", token);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ClientsStatusesAssignment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        httpApi.getSellRequirements("Bearer " + token, client.getClientId()).enqueue(new Callback<List<ClientsStatusesAssignment>>() {
            @Override
            public void onResponse(Call<List<ClientsStatusesAssignment>> call, Response<List<ClientsStatusesAssignment>> response) {
                if (response.isSuccessful()) {
                    sellStatusAssignments = response.body();

                    if (sellStatusAssignments != null && !sellStatusAssignments.isEmpty()) {
                        sellRequirementsAdapter = new RequirementsAdapter(getApplicationContext(), currentActivity, sellStatusAssignments, token);
                        sellReqRecycler.setAdapter(sellRequirementsAdapter);
                    }
                    sellReqButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dropdownButtonsClick("sell", token);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ClientsStatusesAssignment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        httpApi.getRentRequirements("Bearer " + token, client.getClientId()).enqueue(new Callback<List<ClientsStatusesAssignment>>() {
            @Override
            public void onResponse(Call<List<ClientsStatusesAssignment>> call, Response<List<ClientsStatusesAssignment>> response) {
                if (response.isSuccessful()) {
                    rentStatusAssignments = response.body();

                    if (rentStatusAssignments != null && !rentStatusAssignments.isEmpty()) {
                        rentRequirementsAdapter = new RequirementsAdapter(getApplicationContext(), currentActivity, rentStatusAssignments, token);
                        rentReqRecycler.setAdapter(rentRequirementsAdapter);
                    }
                    toRentReqButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dropdownButtonsClick("rent", token);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ClientsStatusesAssignment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        httpApi.getForRentRequirements("Bearer " + token, client.getClientId()).enqueue(new Callback<List<ClientsStatusesAssignment>>() {
            @Override
            public void onResponse(Call<List<ClientsStatusesAssignment>> call, Response<List<ClientsStatusesAssignment>> response) {
                if (response.isSuccessful()) {
                    forRentStatusAssignments = response.body();

                    if (forRentStatusAssignments != null && !forRentStatusAssignments.isEmpty()) {
                        forRentRequirementsAdapter = new RequirementsAdapter(getApplicationContext(), currentActivity, forRentStatusAssignments, token);
                        forRentReqRecycler.setAdapter(forRentRequirementsAdapter);
                    }
                    forRentReqButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dropdownButtonsClick("forRent", token);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ClientsStatusesAssignment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        watchDocumentsButton = findViewById(R.id.watchDocumentsButton);
        watchDocumentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromClientsDetailed(WatchDocumentsActivity.class, 0);
            }
        });

        watchObjectsButton = findViewById(R.id.watchObjectsButton);
        watchObjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromClientsDetailed(ObjectsSearchActivity.class, 0);
            }
        });

        watchOperationsButton = findViewById(R.id.watchOperationsButton);
        watchOperationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameFromClientsDetailed(OperationsListActivity.class, 0);
            }
        });

        editClientButton = findViewById(R.id.clientInfoEditClientButton);
        editClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToClientEdit = new Intent(getApplicationContext(), CreateClientActivity.class);
                moveToClientEdit.putExtra("login", getIntent().getStringExtra("login"));
                moveToClientEdit.putExtra("role", getIntent().getStringExtra("role"));
                moveToClientEdit.putExtra("token", getIntent().getStringExtra("token"));
                moveToClientEdit.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

                Gson jsonConverter = new Gson();
                String clientJson = jsonConverter.toJson(client);
                moveToClientEdit.putExtra("FROM_CLIENTS_DETAILED", clientJson);

                String employeeNameExtra = getIntent().getStringExtra("employeeName");

                if (employeeNameExtra != null) {
                    moveToClientEdit.putExtra("employeeName", employeeNameExtra);
                }

                editClientLauncher.launch(moveToClientEdit);
            }
        });

        String role = getIntent().getStringExtra("role");
        int employeeId = getIntent().getIntExtra("employee_id", 0);

        if (Role.MAIN_ADMIN.equals(role) || Role.ADMIN.equals(role)) {
            deleteClientButton = findViewById(R.id.clientInfoDeleteClientButton);
            deleteClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder forDelete = new AlertDialog.Builder(ClientsDetailedActivity.this);
                    forDelete.setTitle(R.string.delete_client_button);
                    forDelete.setMessage(R.string.delete_client_message);
                    forDelete.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            httpApi.deleteClient("Bearer " + token, client.getClientId()).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Клієнт успішно видалений!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення клієнта("
                                                    + response.errorBody().string() + ")", Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення клієнта", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                    Toast.makeText(getApplicationContext(), "Сталася помилка під час видалення клієнта("
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

    private void dropdownButtonsClick(String requirementType, String token) {
        Button pressed;
        ConstraintLayout dropdown;
        RequirementsAdapter adapter;
        RecyclerView requirementsList;
        List<ClientsStatusesAssignment> assignments;

        if (requirementType.equals("buy")) {
            pressed = buyReqButton;
            dropdown = buyReqDropdown;
            adapter = buyRequirementsAdapter;
            requirementsList = buyReqRecycler;
            assignments = buyStatusAssignments;
        } else if (requirementType.equals("sell")) {
            pressed = sellReqButton;
            dropdown = sellReqDropdown;
            adapter = sellRequirementsAdapter;
            requirementsList = sellReqRecycler;
            assignments = sellStatusAssignments;
        } else if (requirementType.equals("rent")) {
            pressed = toRentReqButton;
            dropdown = toRentReqDropdown;
            adapter = rentRequirementsAdapter;
            requirementsList = rentReqRecycler;
            assignments = rentStatusAssignments;
        } else {
            pressed = forRentReqButton;
            dropdown = forRentReqDropdown;
            adapter = forRentRequirementsAdapter;
            requirementsList = forRentReqRecycler;
            assignments = forRentStatusAssignments;
        }

        if (adapter != null && assignments != null && !assignments.isEmpty()
                && dropdown.getVisibility() == ConstraintLayout.GONE) {
            dropdown.setVisibility(ConstraintLayout.VISIBLE);
            pressed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_keyboard_arrow_up_24, 0);
        } else if (adapter != null && assignments != null && !assignments.isEmpty()) {
            dropdown.setVisibility(ConstraintLayout.GONE);
            pressed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_keyboard_arrow_down_24, 0);

            for (int i = 0; i < assignments.size(); ++i) {
                RequirementsViewHolder viewHolder = (RequirementsViewHolder) requirementsList.findViewHolderForAdapterPosition(i);

                if (viewHolder != null) {
                    String actualText = viewHolder.getRequirementView().getText().toString();
                    ClientsStatusesAssignment currentAssignment = assignments.get(i);

                    if (viewHolder.getRequirementView().isEnabled() && !actualText.equals(currentAssignment.getRequirements())) {
                        currentAssignment.setRequirements(actualText);
                        viewHolder.getRequirementView().setEnabled(false);
                        final int j = i;

                        httpApi.updateClientsStatusesAssignment("Bearer " + token,
                                currentAssignment.getClientId(), currentAssignment.getStatus(),
                                currentAssignment.getOperationId(), currentAssignment).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    assignments.set(j, currentAssignment);
                                    adapter.notifyItemChanged(j);
                                    Toast.makeText(getApplicationContext(), "Збережено", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Помилка при збереженні. Спробуйте знову", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        }
    }

    private void displayClientInfo() {
        if (client == null) {
            Toast.makeText(getApplicationContext(), "No client data to display", Toast.LENGTH_LONG).show();
            finish();
        }

        // clientPhoto must be specified here
        int resourceId = getResources().getIdentifier(client.getPhoto(), "drawable",
                getPackageName());
        if (resourceId != 0) {
            clientPhoto.setImageResource(resourceId);
        }

        clientName.setText(client.getName());
        passportNumber.setText(client.getPassportNumber());
        taxpayerCardNumber.setText(client.getTaxpayerNumber());
        phoneNumber.setText(client.getPhoneNumber());
        emailAddress.setText(client.getEmail());
        birthdate.setText(client.getBirthdateFormatted());
        birthplace.setText(client.getBirthplace());
        gender.setText(client.getGender());
    }

    private void cameFromClientsDetailed(Class<?> activityClass, int data) {
        Intent moveToActivity = new Intent(getApplicationContext(), activityClass);

        if (data < 1) {
            data = client.getClientId();
        }

        if (moveToActivity != null) {
            moveToActivity.putExtra("FROM_CLIENTS_DETAILED", data);

            passEmployeeData(moveToActivity);
        }
    }
}