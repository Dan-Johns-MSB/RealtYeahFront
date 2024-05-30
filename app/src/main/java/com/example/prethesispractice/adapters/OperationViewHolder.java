package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;

public class OperationViewHolder extends RecyclerView.ViewHolder {
    protected TextView nameView;
    protected TextView dateView;
    protected TextView hostView;
    protected TextView relatedObjectAddressView;
    protected TextView statusView;

    public OperationViewHolder(@NonNull View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.operationFoundName);
        dateView = itemView.findViewById(R.id.operationFoundDate);
        hostView = itemView.findViewById(R.id.operationFoundHost);
        relatedObjectAddressView = itemView.findViewById(R.id.operationFoundRelatedObjectAddress);
        statusView = itemView.findViewById(R.id.operationFoundStatus);
    }
}
