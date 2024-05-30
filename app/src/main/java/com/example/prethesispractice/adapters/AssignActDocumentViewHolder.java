package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;

public class AssignActDocumentViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    ImageButton removeButton;

    public AssignActDocumentViewHolder (@NonNull View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.documentName);
        removeButton = itemView.findViewById(R.id.documentRemoveButton);
    }
}
