package com.example.prethesispractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.entities.Document;

import java.util.List;

public class AssignActDocumentAdapter extends RecyclerView.Adapter<AssignActDocumentViewHolder> {
    private Context context;
    private List<Document> documents;

    public AssignActDocumentAdapter(Context context, List<Document> documents) {
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
    public AssignActDocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignActDocumentViewHolder(LayoutInflater.from(context).inflate(R.layout.assign_act_document_preview_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssignActDocumentViewHolder holder, int position) {
        holder.titleView.setText(documents.get(position).getName());
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.setVisibility(ConstraintLayout.GONE);
            }
        });
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

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        if (documents == null) {
            throw new IllegalArgumentException("Document photos list is null");
        }

        this.documents = documents;
    }
}
