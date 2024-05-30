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
import com.example.prethesispractice.activities.ObjectsDetailedActivity;
import com.example.prethesispractice.activities.ToolbarMenuActivity;
import com.example.prethesispractice.entities.EstateObject;
import com.google.gson.Gson;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectViewHolder> {
    private Context context;
    private ToolbarMenuActivity currentActivity;
    private List<EstateObject> estateObjects;
    private List<String> owners;

    public ObjectAdapter(Context context, List<EstateObject> estateObjects, List<String> owners, ToolbarMenuActivity currentActivity) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (estateObjects == null) {
            throw new IllegalArgumentException("Clients list is null");
        } else if (owners == null) {
            throw new IllegalArgumentException("Owners list can't be null");
        } else if (currentActivity == null) {
            throw new IllegalArgumentException("Current activity can't be null");
        }

        this.context = context;
        this.estateObjects = estateObjects;
        this.owners = owners;
        this.currentActivity = currentActivity;
    }

    @NonNull
    @Override
    public ObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ObjectViewHolder(LayoutInflater.from(context).inflate(R.layout.object_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectViewHolder holder, int position) {
        // consider how we're gonna store files and photos on server, then fix it
        int resourceId = context.getResources().getIdentifier(estateObjects.get(position).getPhoto(), "drawable",
                context.getPackageName());
        if (resourceId != 0) {
            holder.photoView.setImageResource(resourceId);
        }

        holder.addressView.setText(estateObjects.get(position).getAddress());
        holder.typeView.setText(estateObjects.get(position).getType());
        holder.areaView.setText(String.format("%.2f", estateObjects.get(position).getArea()));
        holder.priceView.setText(String.format("%.2f", estateObjects.get(position).getPrice()));
        holder.ownerNameView.setText(owners.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToObjectDetails = new Intent(context, ObjectsDetailedActivity.class);

                Gson jsonConverter = new Gson();
                String chosenObject = jsonConverter.toJson(estateObjects.get(holder.getAdapterPosition()));
                moveToObjectDetails.putExtra("chosen_object", chosenObject);
                currentActivity.passEmployeeData(moveToObjectDetails);
            }
        });

        if (currentActivity.getIntent().getBooleanExtra("FROM_CREATE_OPERATION", false)) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    finishChoiceWithResult("Об'єкт успішно обрано!", estateObjects.get(holder.getAdapterPosition()));

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return estateObjects.size();
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

    public List<EstateObject> getEstateObjects() {
        return estateObjects;
    }

    public void setEstateObjects(List<EstateObject> estateObjects) {
        if (estateObjects == null) {
            throw new IllegalArgumentException("Operations list is null");
        }

        this.estateObjects = estateObjects;
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

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        if (owners == null) {
            throw new IllegalArgumentException("Owners list can't be null");
        }

        this.owners = owners;
    }

    private void finishChoiceWithResult(String message, EstateObject estateObject) {
        Gson jsonConverter = new Gson();
        String resultObject = jsonConverter.toJson(estateObject);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_object", resultObject);
        currentActivity.setResult(Activity.RESULT_OK, resultReturn);

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        currentActivity.finish();
    }
}
