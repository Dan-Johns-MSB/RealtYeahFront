package com.example.prethesispractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.adapters.DocumentPreviewAdapter;
import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.Document;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchDocumentsActivity extends ToolbarMenuActivity {
    private ConstraintLayout bigImagesLayout;
    private ImageView bigImageView;
    private TextView bigImageTitle;
    private ImageButton bigImageCloseBtn;
    private ImageButton bigImageDownloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_documents);
        inflateToolbar(null);

        // Associating bigImagesContainer, bigImageView and title with corresponding variables
        bigImagesLayout = findViewById(R.id.bigImagesContainer);
        bigImageView = findViewById(R.id.bigImageView);
        bigImageTitle = findViewById(R.id.bigImageTitle);
        bigImageCloseBtn = findViewById(R.id.bigImageCloseButton);
        bigImageDownloadBtn = findViewById(R.id.bigImageDownloadButton);

        RecyclerView documentsList = findViewById(R.id.documentsList);
        List<Document> documents = new ArrayList<Document>();

        String[] names = new String[]{"Техпаспорт", "Договір"};
        String[] paths = new String[]{"01.jpg", "02.jpg"};
        documents.add(new Document(null, paths[0], paths[0], 1));
        documents.add(new Document(null, paths[1], paths[1], 1));

        documentsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        documentsList.setAdapter(new DocumentPreviewAdapter(getApplicationContext(), documents));

        // Setting onClick listener to close button
        bigImageCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigImagesLayout.setVisibility(ConstraintLayout.GONE);
            }
        });
    }
}