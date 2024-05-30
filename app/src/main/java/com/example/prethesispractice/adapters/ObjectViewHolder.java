package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;

public class ObjectViewHolder extends RecyclerView.ViewHolder {
    protected ImageView photoView;
    protected TextView addressView;
    protected TextView typeView;
    protected TextView areaView;
    protected TextView priceView;
    protected TextView ownerNameView;

    public ObjectViewHolder(@NonNull View itemView) {
        super(itemView);

        photoView = itemView.findViewById(R.id.objectImage);
        addressView = itemView.findViewById(R.id.objectAddress);
        typeView = itemView.findViewById(R.id.objectType);
        areaView = itemView.findViewById(R.id.objectArea);
        priceView = itemView.findViewById(R.id.objectPrice);
        ownerNameView = itemView.findViewById(R.id.objectOwner);
    }
}
