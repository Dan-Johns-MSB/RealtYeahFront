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
import com.example.prethesispractice.adapters.ObjectAdapter;
import com.example.prethesispractice.entities.EstateObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObjectsSearchActivity extends ToolbarMenuActivity {
    private Button addObjectButton;
    private ConstraintLayout searchMenuLayout;
    private SearchView searchView;
    private Switch searchInTypesSwitch;
    private Switch searchInAddressSwitch;
    private Switch searchInOwnersSwitch;
    private List<EstateObject> objectsFound;
    private List<String> owners;
    private ObjectAdapter adapter;
    private final ActivityResultLauncher<Intent> createObjectLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String createdObject = result.getData().getStringExtra("result_object");

                    Gson jsonConverter = new Gson();
                    objectsFound.add(0, jsonConverter.fromJson(createdObject, EstateObject.class));

                    httpApi.getEstateObjectOwnerName("Bearer " + getIntent().getStringExtra("token"),
                            objectsFound.get(0).getEstateObjectId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                owners.add(0, response.body() == null ? "Unknown" : response.body());
                                adapter.notifyDataSetChanged();
                            } else {
                                owners.add(0, "Unknown");
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
        setContentView(R.layout.activity_objects_search);
        inflateToolbar(null);

        addObjectButton = findViewById(R.id.objectsSearchAddObjectButton);
        if (!getIntent().getBooleanExtra("FROM_CREATE_OPERATION", false)) {
            addObjectButton.setVisibility(ConstraintLayout.GONE);
        } else {
            addObjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent moveToObjectCreate = new Intent(getApplicationContext(), CreateObjectActivity.class);
                    moveToObjectCreate.putExtra("login", getIntent().getStringExtra("login"));
                    moveToObjectCreate.putExtra("role", getIntent().getStringExtra("role"));
                    moveToObjectCreate.putExtra("token", getIntent().getStringExtra("token"));
                    moveToObjectCreate.putExtra("employee_id", getIntent().getIntExtra("employee_id", 0));

                    String employeeNameExtra = getIntent().getStringExtra("employeeName");

                    if (employeeNameExtra != null) {
                        moveToObjectCreate.putExtra("employeeName", employeeNameExtra);
                    }

                    createObjectLauncher.launch(moveToObjectCreate);
                }
            });
        }

        final String token = getIntent().getStringExtra("token");
        int fromClient = getIntent().getIntExtra("FROM_CLIENTS_DETAILED", 0);
        int fromEmployee = getIntent().getIntExtra("FROM_EMPLOYEES_DETAILED", 0);

        if (fromClient > 0) {
            searchMenuLayout = findViewById(R.id.objectSearchMenuLayout);
            searchMenuLayout.setVisibility(ConstraintLayout.GONE);

            getClientRelatedObjects(token, fromClient);
        } else if (fromEmployee > 0) {
            startAdapter();
            setupFilters();

            getEmployeeRelatedObjects(token, fromEmployee);
        } else {
            startAdapter();
            setupFilters();

            String objectsType = getIntent().getStringExtra("objectsType");
            getObjects(token, objectsType);
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

    private void getObjects(String token, String status) {
        if (!token.isBlank()) {
            if (status == null || status.isBlank()) {
                httpApi.getEstateObjects("Bearer " + token).enqueue(new Callback<List<EstateObject>>() {
                    @Override
                    public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                        if (response.isSuccessful()) {
                            objectsFound = response.body();
                            owners = new ArrayList<>();

                            for (int i = 0; i < objectsFound.size(); ++i) {
                                httpApi.getEstateObjectOwnerName("Bearer " + token, objectsFound.get(i).getEstateObjectId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            owners.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            owners.add("Unknown");
                                        }

                                        if (owners.size() == objectsFound.size()) {
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
                    public void onFailure(Call<List<EstateObject>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else if (status.equals("OTHERS")) {
                httpApi.getEstateObjectsByStatus("Bearer " + token, "Внесений у базу").enqueue(new Callback<List<EstateObject>>() {
                    @Override
                    public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                        if (response.isSuccessful()) {
                            objectsFound = response.body();

                            httpApi.getEstateObjectsByStatus("Bearer " + token, "Зарезервований").enqueue(new Callback<List<EstateObject>>() {
                                @Override
                                public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        objectsFound.addAll(response.body());

                                        httpApi.getEstateObjectsByStatus("Bearer " + token, "Архівований").enqueue(new Callback<List<EstateObject>>() {
                                            @Override
                                            public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    objectsFound.addAll(response.body());
                                                    owners = new ArrayList<>();

                                                    for (int i = 0; i < objectsFound.size(); ++i) {
                                                        httpApi.getEstateObjectOwnerName("Bearer " + token, objectsFound.get(i).getEstateObjectId()).enqueue(new Callback<String>() {
                                                            @Override
                                                            public void onResponse(Call<String> call, Response<String> response) {
                                                                if (response.isSuccessful()) {
                                                                    owners.add(response.body() == null ? "Unknown" : response.body());
                                                                } else {
                                                                    owners.add("Unknown");
                                                                }

                                                                if (owners.size() == objectsFound.size()) {
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
                                            public void onFailure(Call<List<EstateObject>> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<EstateObject>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EstateObject>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                httpApi.getEstateObjectsByStatus("Bearer " + token, status).enqueue(new Callback<List<EstateObject>>() {
                    @Override
                    public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                        if (response.isSuccessful()) {
                            objectsFound = response.body();
                            owners = new ArrayList<>();

                            for (int i = 0; i < objectsFound.size(); ++i) {
                                httpApi.getEstateObjectOwnerName("Bearer " + token, objectsFound.get(i).getEstateObjectId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            owners.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            owners.add("Unknown");
                                        }

                                        if (owners.size() == objectsFound.size()) {
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
                    public void onFailure(Call<List<EstateObject>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error receiving data. Please restart app and try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getClientRelatedObjects(String token, int fromClient) {
        if (!token.isBlank()) {
            if (fromClient > 0) {
                httpApi.getClientRelatedObjects("Bearer " + token, fromClient).enqueue(new Callback<List<EstateObject>>() {
                    @Override
                    public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                        if (response.isSuccessful()) {
                            objectsFound = response.body();
                            owners = new ArrayList<>();

                            for (int i = 0; i < objectsFound.size(); ++i) {
                                httpApi.getEstateObjectOwnerName("Bearer " + token, objectsFound.get(i).getEstateObjectId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            owners.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            owners.add("Unknown");
                                        }

                                        if (owners.size() == objectsFound.size()) {
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
                    public void onFailure(Call<List<EstateObject>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Can't load related objects as client hasn't been provided",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error receiving data. Please restart app and try again",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getEmployeeRelatedObjects(String token, int fromEmployee) {
        if (!token.isBlank()) {
            if (fromEmployee > 0) {
                httpApi.getEmployeeRelatedObjects("Bearer " + token, fromEmployee).enqueue(new Callback<List<EstateObject>>() {
                    @Override
                    public void onResponse(Call<List<EstateObject>> call, Response<List<EstateObject>> response) {
                        if (response.isSuccessful()) {
                            objectsFound = response.body();
                            owners = new ArrayList<>();

                            for (int i = 0; i < objectsFound.size(); ++i) {
                                httpApi.getEstateObjectOwnerName("Bearer " + token, objectsFound.get(i).getEstateObjectId()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            owners.add(response.body() == null ? "Unknown" : response.body());
                                        } else {
                                            owners.add("Unknown");
                                        }

                                        if (owners.size() == objectsFound.size()) {
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
                    public void onFailure(Call<List<EstateObject>> call, Throwable t) {
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
        searchView = findViewById(R.id.objectSearchView);
        searchInTypesSwitch = findViewById(R.id.searchInTypesOption);
        searchInAddressSwitch = findViewById(R.id.searchInAddressesOption);
        searchInOwnersSwitch = findViewById(R.id.searchInOwnersOption);

        searchInTypesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInAddressSwitch.setChecked(false);
                    searchInOwnersSwitch.setChecked(false);

                    List<EstateObject> filteredObjects = new ArrayList<EstateObject>();
                    List<String> filteredOwners = new ArrayList<String>();
                    for (EstateObject estateObject : objectsFound) {
                        if (estateObject.getType().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredObjects.add(estateObject);
                            filteredOwners.add(owners.get(objectsFound.indexOf(estateObject)));
                        }
                    }

                    changeAdapterDataset(filteredObjects, filteredOwners);
                } else if (!searchInAddressSwitch.isChecked()
                        && !searchInOwnersSwitch.isChecked()) {
                    changeAdapterDataset(objectsFound, owners);
                }
            }
        });
        searchInAddressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInTypesSwitch.setChecked(false);
                    searchInOwnersSwitch.setChecked(false);

                    List<EstateObject> filteredObjects = new ArrayList<EstateObject>();
                    List<String> filteredOwners = new ArrayList<String>();
                    for (EstateObject estateObject : objectsFound) {
                        if (estateObject.getAddress().toLowerCase()
                                .contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredObjects.add(estateObject);
                            filteredOwners.add(owners.get(objectsFound.indexOf(estateObject)));
                        }
                    }

                    changeAdapterDataset(filteredObjects, filteredOwners);
                } else if (!searchInTypesSwitch.isChecked()
                        && !searchInOwnersSwitch.isChecked()) {
                    changeAdapterDataset(objectsFound, owners);
                }
            }
        });
        searchInOwnersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchView.clearFocus();

                if (b) {
                    searchInTypesSwitch.setChecked(false);
                    searchInAddressSwitch.setChecked(false);

                    List<EstateObject> filteredObjects = new ArrayList<EstateObject>();
                    List<String> filteredOwners = new ArrayList<String>();
                    for (String owner : owners) {
                        if (owner.toLowerCase().contains(searchView.getQuery().toString().toLowerCase())) {
                            filteredObjects.add(objectsFound.get(owners.indexOf(owner)));
                            filteredOwners.add(owner);
                        }
                    }

                    changeAdapterDataset(filteredObjects, filteredOwners);
                } else if (!searchInTypesSwitch.isChecked()
                        && !searchInAddressSwitch.isChecked()) {
                    changeAdapterDataset(objectsFound, owners);
                }
            }
        });
    }

    private void startAdapter() {
        RecyclerView objectsSearchResult = findViewById(R.id.objectsSearchResult);
        objectsSearchResult.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (objectsFound == null || owners == null) {
            objectsFound = new ArrayList<EstateObject>();
            owners = new ArrayList<String>();
        }
        adapter = new ObjectAdapter(getApplicationContext(), objectsFound, owners, this);
        objectsSearchResult.setAdapter(adapter);
    }

    private void changeAdapterDataset(List<EstateObject> changedObjects, List<String> changedOwners) {
        if (adapter == null) {
            startAdapter();
        }

        adapter.setEstateObjects(changedObjects);
        adapter.setOwners(changedOwners);
        adapter.notifyDataSetChanged();
    }
}