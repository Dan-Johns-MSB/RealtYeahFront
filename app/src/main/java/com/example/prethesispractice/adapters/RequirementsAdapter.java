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
import com.example.prethesispractice.activities.OperationsDetailedActivity;
import com.example.prethesispractice.activities.ToolbarMenuActivity;
import com.example.prethesispractice.entities.ClientsStatusesAssignment;
import com.example.prethesispractice.entities.Operation;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsViewHolder> {
    private final String token;
    private Context context;
    private ToolbarMenuActivity currentActivity;
    private List<ClientsStatusesAssignment> statusesAssignments;

    public RequirementsAdapter(Context context, ToolbarMenuActivity currentActivity,
                               List<ClientsStatusesAssignment> statusesAssignments, String token) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (currentActivity == null) {
            throw new IllegalArgumentException("Current activity can't be null");
        } else if (statusesAssignments == null) {
            throw new IllegalArgumentException("Clients list is null");
        } else if (token == null) {
            throw new IllegalArgumentException("Token can't be null");
        }

        this.context = context;
        this.currentActivity = currentActivity;
        this.statusesAssignments = statusesAssignments;
        this.token = token;
    }

    @NonNull
    @Override
    public RequirementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequirementsViewHolder(LayoutInflater.from(context).inflate(R.layout.requirement_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequirementsViewHolder holder, int position) {
        String operationText = "Операція №" + statusesAssignments.get(position).getOperationId();
        holder.operationTextView.setText(operationText);
        holder.operationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentActivity.getHttpApi().getOperationById("Bearer " + token,
                                statusesAssignments.get(holder.getAdapterPosition()).getOperationId())
                        .enqueue(new Callback<Operation>() {
                            @Override
                            public void onResponse(Call<Operation> call, Response<Operation> response) {
                                if (response.isSuccessful()) {
                                    Gson jsonConverter = new Gson();

                                    Intent moveToOperation = new Intent(context, OperationsDetailedActivity.class);
                                    moveToOperation.putExtra("chosen_operation", jsonConverter.toJson(response.body()));

                                    currentActivity.passEmployeeData(moveToOperation);
                                } else {
                                    Toast.makeText(context, "Неможливо перейти до вказаної операції", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Operation> call, Throwable throwable) {
                                Toast.makeText(context, "Неможливо перейти до вказаної операції("
                                        + throwable.getMessage() + ")", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        String requirements = statusesAssignments.get(position).getRequirements();
        holder.requirementView.setText((requirements == null || requirements.isBlank()) ? "" : requirements);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.requirementView.setEnabled(!holder.requirementView.isEnabled());

                ClientsStatusesAssignment currentAssignment = statusesAssignments.get(holder.getAdapterPosition());
                String actualText = holder.requirementView.getText().toString();
                if (!holder.requirementView.isEnabled() && !actualText.equals(currentAssignment.getRequirements())) {
                    currentAssignment.setRequirements(actualText);

                    currentActivity.getHttpApi().updateClientsStatusesAssignment("Bearer " + token,
                            currentAssignment.getClientId(), currentAssignment.getStatus(),
                            currentAssignment.getOperationId(), currentAssignment).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                statusesAssignments.set(holder.getAdapterPosition(), currentAssignment);
                                notifyItemChanged(holder.getAdapterPosition());
                                Toast.makeText(context, "Збережено", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Помилка при збереженні. Спробуйте знову", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return statusesAssignments.size();
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

    public ToolbarMenuActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(ToolbarMenuActivity currentActivity) {
        if (currentActivity == null) {
            throw new IllegalArgumentException("Current activity can't be null");
        }

        this.currentActivity = currentActivity;
    }

    public List<ClientsStatusesAssignment> getStatusesAssignments() {
        return statusesAssignments;
    }

    public void setStatusesAssignments(List<ClientsStatusesAssignment> statusesAssignments) {
        if (statusesAssignments == null) {
            throw new IllegalArgumentException("Clients list is null");
        }

        this.statusesAssignments = statusesAssignments;
    }
}
