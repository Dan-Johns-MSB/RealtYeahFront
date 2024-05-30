package com.example.prethesispractice.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prethesispractice.R;
import com.example.prethesispractice.activities.EmployeesDetailedActivity;
import com.example.prethesispractice.activities.ToolbarMenuActivity;
import com.example.prethesispractice.entities.Employee;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {
    private Context context;
    private ToolbarMenuActivity currentActivity;
    private List<Employee> employees;

    public EmployeeAdapter(Context context, ToolbarMenuActivity currentActivity, List<Employee> employees) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (currentActivity == null) {
            throw new IllegalArgumentException("Current activity can't be null");
        } else if (employees == null) {
            throw new IllegalArgumentException("Hosts list is null");
        }

        this.context = context;
        this.currentActivity = currentActivity;
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        // consider how we're gonna store files and photos on server, then fix it
        int resourceId = context.getResources().getIdentifier(employees.get(position).getPhoto(), "drawable",
                context.getPackageName());
        if (resourceId != 0) {
            holder.photoView.setImageResource(resourceId);
        }

        holder.nameView.setText(employees.get(position).getName());
        holder.jobView.setText(employees.get(position).getJob());
        holder.phoneNumberView.setText(employees.get(position).getPhoneNumber());

        currentActivity.getHttpApi().getEmployeeEmail("Bearer " + currentActivity.getIntent().getStringExtra("token"),
                employees.get(position).getEmployeeId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    holder.emailView.setText(response.body());
                } else {
                    holder.emailView.setText(R.string.unknown_value);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToEmployeeDetails = new Intent(context, EmployeesDetailedActivity.class);

                Gson jsonConverter = new Gson();
                String chosenEmployee = jsonConverter.toJson(employees.get(holder.getAdapterPosition()));
                moveToEmployeeDetails.putExtra("chosen_employee", chosenEmployee);
                currentActivity.passEmployeeData(moveToEmployeeDetails);
            }
        });

        if (currentActivity.getIntent().getBooleanExtra("FROM_CREATE_OPERATION", false)) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    finishChoiceWithResult("Хост успішно обраний!", employees.get(holder.getAdapterPosition()));

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        if (employees == null) {
            throw new IllegalArgumentException("Clients list is null");
        }

        this.employees = employees;
    }

    private void finishChoiceWithResult(String message, Employee employee) {
        Gson jsonConverter = new Gson();
        String resultEmployee = jsonConverter.toJson(employee);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_employee", resultEmployee);
        currentActivity.setResult(Activity.RESULT_OK, resultReturn);

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        currentActivity.finish();
    }
}
