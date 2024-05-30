package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;

public class EmployeeViewHolder extends RecyclerView.ViewHolder {
    protected ImageView photoView;
    protected TextView nameView;
    protected TextView jobView;
    protected TextView phoneNumberView;
    protected TextView emailView;

    public EmployeeViewHolder(@NonNull View itemView) {
        super(itemView);

        photoView = itemView.findViewById(R.id.hostImage);
        nameView = itemView.findViewById(R.id.hostName);
        jobView = itemView.findViewById(R.id.hostJob);
        phoneNumberView = itemView.findViewById(R.id.hostPhoneNumber);
        emailView = itemView.findViewById(R.id.hostEmail);
    }
}
