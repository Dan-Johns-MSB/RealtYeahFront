package com.example.prethesispractice.adapters;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prethesispractice.R;

public class RequirementsViewHolder extends RecyclerView.ViewHolder {
    protected ImageButton editButton;
    protected TextView operationTextView;
    protected EditText requirementView;

    public RequirementsViewHolder(@NonNull View itemView) {
        super(itemView);

        editButton = itemView.findViewById(R.id.reqEditButton);
        operationTextView = itemView.findViewById(R.id.reqOperationId);
        requirementView = itemView.findViewById(R.id.reqTextField);
    }

    public ImageButton getEditButton() {
        return editButton;
    }

    public TextView getOperationTextView() {
        return operationTextView;
    }

    public EditText getRequirementView() {
        return requirementView;
    }
}
