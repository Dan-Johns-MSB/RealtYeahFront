package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;

public class ClientViewHolder extends RecyclerView.ViewHolder {
    protected ImageView photoView;
    protected TextView nameView;
    protected TextView passportNumberView;
    protected TextView lastObjectAddressView;
    protected TextView phoneNumberView;

    public ClientViewHolder(@NonNull View itemView) {
        super(itemView);

        photoView = itemView.findViewById(R.id.clientFoundPhoto);
        nameView = itemView.findViewById(R.id.clientFoundName);
        passportNumberView = itemView.findViewById(R.id.clientFoundPassport);
        lastObjectAddressView = itemView.findViewById(R.id.clientFoundLatestRelatedObjectAddress);
        phoneNumberView = itemView.findViewById(R.id.clientFoundPhoneNumber);
    }
}
