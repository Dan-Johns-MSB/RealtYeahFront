package com.example.prethesispractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.prethesispractice.adapters.ClientAdapter;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.EstateObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsSearchActivity extends ToolbarMenuActivity {
    private Button addClientButton;
    private ConstraintLayout searchMenuLayout;
    private SearchView searchView;
    private Switch searchInNamesSwitch;
    private Switch searchInPhoneNumbersSwitch;
    private Switch searchInEmailsSwitch;
    private Switch searchInAddressSwitch;
    private List<Client> clientsFound;
    private List<String> lastObjectAddresses;
    private ClientAdapter adapter;
    private final ActivityResultLauncher<Intent> createClientLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String createdClient = result.getData().getStringExtra("result_client");

                    Gson jsonConverter = new Gson();
                    clientsFound.add(0, jsonConverter.fromJson(createdClient, Client.class));

                    httpApi.getClientLatestObjectAddress("Bearer " + getIntent().getStringExtra("token"),
                            clientsFound.get(0).getClientId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                lastObjectAddresses.add(0, response.body() == null ? "Unknown" : response.body());
                                adapter.notifyDataSetChanged();
                            } else {
                                lastObjectAddresses.add(0, "Unknown");
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_search);
        inflateToolbar(null);

        addClientButton = findViewById(R.id.clientsSearchAddClientButton);
        if (!getIntent().getBooleanExtra("FROM_CREATE_OPERATION", false)) {
            addClientButton.setVisibility(ConstraintLayout.GONE);
        } else {
            addClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent moveToClientCreate = new Intent(getApplicationContext(), CreateClientActivity.class);
                    moveToClientCreate.putExtra("login", getIntent().getStringExtra("login"));
                    moveToClientCreate.putExtra("role", getIntent().getStringExtra("role"));
                    moveToClientCreate.putExtra("token", getIntent().getStringExtra("token"));
                    moveToClientCreate.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

                    String employeeNameExtra = getIntent().getStringExtra("employeeName");

                    if (employeeNameExtra != null) {
                        moveToClientCreate.putExtra("employeeName", employeeNameExtra);
                    }

                    createClientLauncher.launch(moveToClientCreate);
                }
            });
        }

        final String token = getIntent().getStringExtra("token");
        int fromObject = getIntent().getIntExtra("FROM_OBJECTS_DETAILED", 0);
        int fromEmployee = getIntent().getIntExtra("FROM_EMPLOYEES_DETAILED", 0);

        if (fromObject > 0) {
            searchMenuLayout = findViewById(R.id.clientSearchMenuLayout);
            searchMenuLayout.setVisibility(ConstraintLayout.GONE);

            getInterestedClients(token, fromObject);
        } else if (fromEmployee > 0) {
            startAdapter();
            setupFilters();

            getEmployeeRelatedClients(token, fromEmployee);
        } else {
            startAdapter();
            setupFilters();

            String clientsType = getIntent().getStringExtra("clientsType");
            getClients(token, clientsType);
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

    private void getClients(String token, String status) {
        if (!token.isBlank()) {
            if (status == null || status.isBlank()) {
                httpApi.getClients("Bearer " + token).enqueue(new Callback<List<Client>>() {
                    @Override
                    public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                        if (response.isSuccessful()) {
                            clientsFound = response.body();
                            lastObjectAddresses = new ArrayList<>();

                            for (int i = 0; i < clientsFound.size(); ++i) {
                                httpApi.getClientLatestObjectAddress("Bearer " + token, clientsFound.get(i).getClientId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            lastObjectAddresses.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            lastObjectAddresses.add("Unknown");
                                        }

                                        if (lastObjectAddresses.size() == clientsFound.size()) {
                                            startAdapter();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Client>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else if (status.equals("OTHERS")) {
                httpApi.getClientsByStatus("Bearer " + token, "Внесений у базу").enqueue(new Callback<List<Client>>() {
                    @Override
                    public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                        if (response.isSuccessful()) {
                            clientsFound = response.body();

                            httpApi.getClientsByStatus("Bearer " + token, "Минулий клієнт").enqueue(new Callback<List<Client>>() {
                                @Override
                                public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        clientsFound.addAll(response.body());
                                        lastObjectAddresses = new ArrayList<>();

                                        for (int i = 0; i < clientsFound.size(); ++i) {
                                            httpApi.getClientLatestObjectAddress("Bearer " + token, clientsFound.get(i).getClientId()).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if (response.isSuccessful()) {
                                                        lastObjectAddresses.add(response.body() == null ? "Unknown" : response.body());
                                                    } else {
                                                        lastObjectAddresses.add("Unknown");
                                                    }

                                                    if (lastObjectAddresses.size() == clientsFound.size()) {
                                                        startAdapter();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Client>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Client>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                httpApi.getClientsByStatus("Bearer " + token, status).enqueue(new Callback<List<Client>>() {
                    @Override
                    public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                        if (response.isSuccessful()) {
                            clientsFound = response.body();
                            lastObjectAddresses = new ArrayList<>();

                            for (int i = 0; i < clientsFound.size(); ++i) {
                                httpApi.getClientLatestObjectAddress("Bearer " + token, clientsFound.get(i).getClientId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            lastObjectAddresses.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            lastObjectAddresses.add("Unknown");
                                        }

                                        if (lastObjectAddresses.size() == clientsFound.size()) {
                                            startAdapter();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Client>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error receiving data. Please restart app and try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getInterestedClients(String token, int object) {
        if (!token.isBlank() || object > 0) {
            httpApi.getEstateObjectInterestedClients("Bearer " + token, object).enqueue(new Callback<List<Client>>() {
                @Override
                public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                    if (response.isSuccessful()) {
                        clientsFound = response.body();
                        lastObjectAddresses = new ArrayList<>();

                        for (int i = 0; i < clientsFound.size(); ++i) {
                            httpApi.getClientLatestObjectAddress("Bearer " + token, clientsFound.get(i).getClientId()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.isSuccessful()) {
                                        lastObjectAddresses.add(response.body() == null ? "Unknown" : response.body());
                                    } else {
                                        lastObjectAddresses.add("Unknown");
                                    }

                                    if (lastObjectAddresses.size() == clientsFound.size()) {
                                        startAdapter();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Заінтересованих клієнтів не знайдено", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Client>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Error receiving data. Please restart app and try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getEmployeeRelatedClients(String token, int fromEmployee) {
        if (!token.isBlank()) {
            if (fromEmployee > 0) {
                httpApi.getEmployeeRelatedClients("Bearer " + token, fromEmployee).enqueue(new Callback<List<Client>>() {
                    @Override
                    public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                        if (response.isSuccessful()) {
                            clientsFound = response.body();
                            lastObjectAddresses = new ArrayList<>();

                            for (int i = 0; i < clientsFound.size(); ++i) {
                                httpApi.getClientLatestObjectAddress("Bearer " + token, clientsFound.get(i).getClientId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            lastObjectAddresses.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            lastObjectAddresses.add("Unknown");
                                        }

                                        if (lastObjectAddresses.size() == clientsFound.size()) {
                                            startAdapter();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Client>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Can't load related objects as employee hasn't been provided",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error receiving data. Please restart app and try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setupFilters() {
        searchView = findViewById(R.id.searchView);
        searchInNamesSwitch = findViewById(R.id.clientsSearchInNamesOption);
        searchInPhoneNumbersSwitch = findViewById(R.id.clientsSearchInPhoneNumbersOption);
        searchInEmailsSwitch = findViewById(R.id.clientsSearchInEmailsOption);
        searchInAddressSwitch = findViewById(R.id.clientsSearchInObjectsAddressesOption);

        searchInNamesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInPhoneNumbersSwitch.setChecked(false);
                    searchInEmailsSwitch.setChecked(false);
                    searchInAddressSwitch.setChecked(false);

                    List<Client> filteredClients = new ArrayList<Client>();
                    List<String> filteredAddresses = new ArrayList<String>();
                    for (Client client : clientsFound) {
                        if (client.getName().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredClients.add(client);
                            filteredAddresses.add(lastObjectAddresses.get(clientsFound.indexOf(client)));
                        }
                    }

                    changeAdapterDataset(filteredClients, filteredAddresses);
                } else if (!searchInPhoneNumbersSwitch.isChecked()
                        && !searchInEmailsSwitch.isChecked()
                        && !searchInAddressSwitch.isChecked()) {
                    changeAdapterDataset(clientsFound, lastObjectAddresses);
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
                    searchInAddressSwitch.setChecked(false);

                    List<Client> filteredClients = new ArrayList<Client>();
                    List<String> filteredAddresses = new ArrayList<String>();
                    for (Client client : clientsFound) {
                        if (client.getPhoneNumber().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredClients.add(client);
                            filteredAddresses.add(lastObjectAddresses.get(clientsFound.indexOf(client)));
                        }
                    }

                    changeAdapterDataset(filteredClients, filteredAddresses);
                } else if (!searchInNamesSwitch.isChecked()
                        && !searchInEmailsSwitch.isChecked()
                        && !searchInAddressSwitch.isChecked()) {
                    changeAdapterDataset(clientsFound, lastObjectAddresses);
                }
            }
        });
        searchInEmailsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInNamesSwitch.setChecked(false);
                    searchInPhoneNumbersSwitch.setChecked(false);
                    searchInAddressSwitch.setChecked(false);

                    List<Client> filteredClients = new ArrayList<Client>();
                    List<String> filteredAddresses = new ArrayList<String>();
                    for (Client client : clientsFound) {
                        if (client.getEmail().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredClients.add(client);
                            filteredAddresses.add(lastObjectAddresses.get(clientsFound.indexOf(client)));
                        }
                    }

                    changeAdapterDataset(filteredClients, filteredAddresses);
                } else if (!searchInNamesSwitch.isChecked()
                        && !searchInPhoneNumbersSwitch.isChecked()
                        && !searchInAddressSwitch.isChecked()) {
                    changeAdapterDataset(clientsFound, lastObjectAddresses);
                }
            }
        });
        searchInAddressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInNamesSwitch.setChecked(false);
                    searchInPhoneNumbersSwitch.setChecked(false);
                    searchInEmailsSwitch.setChecked(false);

                    List<Client> filteredClients = new ArrayList<Client>();
                    List<String> filteredAddresses = new ArrayList<String>();
                    for (String address : lastObjectAddresses) {
                        if (address.toLowerCase().contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredClients.add(clientsFound.get(lastObjectAddresses.indexOf(address)));
                            filteredAddresses.add(address);
                        }
                    }

                    changeAdapterDataset(filteredClients, filteredAddresses);
                } else if (!searchInNamesSwitch.isChecked()
                        && !searchInPhoneNumbersSwitch.isChecked()
                        && !searchInEmailsSwitch.isChecked()) {
                    changeAdapterDataset(clientsFound, lastObjectAddresses);
                }
            }
        });
    }

    private void startAdapter() {
        RecyclerView clientsSearchResult = findViewById(R.id.clientsSearchResult);
        clientsSearchResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (clientsFound == null || lastObjectAddresses == null) {
            clientsFound = new ArrayList<Client>();
            lastObjectAddresses = new ArrayList<String>();
        }
        adapter = new ClientAdapter(getApplicationContext(), clientsFound, lastObjectAddresses, this);
        clientsSearchResult.setAdapter(adapter);
    }

    private void changeAdapterDataset(List<Client> changedClients, List<String> changedAddresses) {
        if (adapter == null) {
            startAdapter();
        }

        adapter.setClients(changedClients);
        adapter.setLastObjectAddresses(changedAddresses);
        adapter.notifyDataSetChanged();
    }
}