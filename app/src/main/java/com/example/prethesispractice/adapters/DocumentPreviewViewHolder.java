package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;

public class DocumentPreviewViewHolder extends RecyclerView.ViewHolder {
    protected TextView titleView;
    protected ImageButton downloadButton;

    public DocumentPreviewViewHolder(@NonNull View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.documentName);
        downloadButton = itemView.findViewById(R.id.documentDownloadButton);
    }
}
