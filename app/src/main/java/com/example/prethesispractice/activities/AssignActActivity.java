package com.example.prethesispractice.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.adapters.AssignActDocumentAdapter;
import com.example.prethesispractice.entities.Document;
import com.example.prethesispractice.entities.Operation;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignActActivity extends ToolbarMenuActivity {
    private static final String[] SUPPORTED_FILE_TYPES = {"image/*",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
    private static final int REQUEST_CODE_FOR_OPENING = 2000;
    private ImageButton documentsAddButton;
    private Button assignActButton;
    private AssignActDocumentAdapter adapter;
    private Operation operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_act);
        inflateToolbar(null);

        documentsAddButton = findViewById(R.id.documentsAddButton);
        assignActButton = findViewById(R.id.assignActButton);

        RecyclerView clientsSearchResult = findViewById(R.id.assignActPickedFiles);
        clientsSearchResult.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AssignActDocumentAdapter(this, new ArrayList<Document>());
        clientsSearchResult.setAdapter(adapter);

        Gson jsonConverter = new Gson();
        String actType = getIntent().getStringExtra("act_type");
        operation = jsonConverter.fromJson(getIntent().getStringExtra("operation"), Operation.class);

        documentsAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseFileIntent.setType("*/*");
                chooseFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, SUPPORTED_FILE_TYPES);
                chooseFileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
                startActivityForResult(chooseFileIntent, REQUEST_CODE_FOR_OPENING);
            }
        });

        assignActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String token = getIntent().getStringExtra("token");

                httpApi.assignAct("Bearer " + token, operation.getOperationId(), actType).enqueue(new Callback<Operation>() {
                    @Override
                    public void onResponse(Call<Operation> call, Response<Operation> response) {
                        if (response.isSuccessful()) {
                            operation.setActType(actType);
                            operation.setStatus("Успішно");

                            finishEditingWithResult("Акт успішно прив'язано");
                        } else {
                            try {
                                finishEditingWithError("Акт неможливо прив'язати(" + response.errorBody().string() + ")");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Operation> call, Throwable t) {
                        finishEditingWithError("Помилка під час прив'язування акту(" + t.getMessage() + ")");
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_OPENING && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ArrayList<Uri> selectedFiles = new ArrayList<>();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        selectedFiles.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else if (data.getData() != null) {
                    selectedFiles.add(data.getData());
                }

                List<Document> documents = new ArrayList<>();

                String[] names = new String[]{"Техпаспорт", "Договір", "Додаток"};
                String[] paths = new String[]{"DCIM/01.jpg", "DCIM/02.jpg", "DCIM/03.jpg"};

                int counter = 0;
                for (Uri uri : selectedFiles) {
                    String filePath = uri.getPath();
                    documents.add(new Document(null, paths[counter], paths[counter], 1));
                    counter = counter + 1;
                }

                adapter.setDocuments(documents);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void finishEditingWithResult(String message) {
        Gson jsonConverter = new Gson();
        String resultOperation = jsonConverter.toJson(operation);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_operation", resultOperation);
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