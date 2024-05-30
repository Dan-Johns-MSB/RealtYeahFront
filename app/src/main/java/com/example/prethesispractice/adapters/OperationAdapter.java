package com.example.prethesispractice.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.activities.CreateOperationActivity;
import com.example.prethesispractice.activities.OperationsDetailedActivity;
import com.example.prethesispractice.activities.ToolbarMenuActivity;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.entities.Operation;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> {
    private Context context;
    private final ToolbarMenuActivity currentActivity;
    private List<Operation> operations;

    public OperationAdapter(Context context, ToolbarMenuActivity currentActivity, List<Operation> operations) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (currentActivity == null) {
            throw new IllegalArgumentException("Current activity can't be null");
        } else if (operations == null) {
            throw new IllegalArgumentException("Operations list is null");
        }

        this.context = context;
        this.currentActivity = currentActivity;
        this.operations = operations;
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OperationViewHolder(LayoutInflater.from(context).inflate(R.layout.operation_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        holder.nameView.setText(operations.get(position).getName());
        holder.dateView.setText(operations.get(position).getDateFormatted());
        holder.statusView.setText(operations.get(position).getStatus());

        currentActivity.getHttpApi().getHostByOperation("Bearer " + currentActivity.getIntent().getStringExtra("token"),
                        operations.get(position).getOperationId())
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        if (response.isSuccessful()) {
                            operations.get(holder.getAdapterPosition()).setHost(response.body());
                            holder.hostView.setText(operations.get(holder.getAdapterPosition()).getHost().getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        currentActivity.getHttpApi().getOperationObjectAddress("Bearer " + currentActivity.getIntent().getStringExtra("token"),
                        operations.get(position).getOperationId())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            holder.relatedObjectAddressView.setText(response.body());
                        } else {
                            holder.relatedObjectAddressView.setText(R.string.unknown_value);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToOperationDetails = new Intent(context, OperationsDetailedActivity.class);

                Gson jsonConverter = new Gson();
                String chosenOperation = jsonConverter.toJson(operations.get(holder.getAdapterPosition()));
                moveToOperationDetails.putExtra("chosen_operation", chosenOperation);
                currentActivity.passEmployeeData(moveToOperationDetails);
            }
        });

        String previousLeadOperation = currentActivity.getIntent().getStringExtra("prev_lead");
        if (previousLeadOperation != null && !previousLeadOperation.isBlank()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent moveToCreateOperation = new Intent(context, CreateOperationActivity.class);
                    moveToCreateOperation.putExtra("prev_lead", previousLeadOperation);

                    Gson jsonConverter = new Gson();
                    String chosenOperation = jsonConverter.toJson(operations.get(holder.getAdapterPosition()));
                    moveToCreateOperation.putExtra("prev_secondary", chosenOperation);
                    currentActivity.passEmployeeData(moveToCreateOperation);

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return operations.size();
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

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        if (operations == null) {
            throw new IllegalArgumentException("Operations list is null");
        }

        this.operations = operations;
    }
}
