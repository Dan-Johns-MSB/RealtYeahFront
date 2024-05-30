package com.example.prethesispractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Document;

import java.util.List;

public class DocumentPreviewAdapter extends RecyclerView.Adapter<DocumentPreviewViewHolder> {
    private Context context;
    private List<Document> documents;

    public DocumentPreviewAdapter(Context context, List<Document> documents) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (documents == null) {
            throw new IllegalArgumentException("Document preview list is null");
        }

        this.context = context;
        this.documents = documents;
    }

    @NonNull
    @Override
    public DocumentPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentPreviewViewHolder(LayoutInflater.from(context).inflate(R.layout.document_preview_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentPreviewViewHolder holder, int position) {
        holder.titleView.setText(documents.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        }

        this.context = context;
    }

    public List<Document> getDocumentPhotos() {
        return documents;
    }

    public void setDocumentPhotos(List<Document> documents) {
        if (documents == null) {
            throw new IllegalArgumentException("Document photos list is null");
        }

        this.documents = documents;
    }
}
