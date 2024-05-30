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
import com.example.prethesispractice.activities.ClientsDetailedActivity;
import com.example.prethesispractice.activities.ToolbarMenuActivity;
import com.example.prethesispractice.entities.Client;
import com.google.gson.Gson;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientViewHolder> {
    private Context context;
    private ToolbarMenuActivity currentActivity;
    private List<Client> clients;
    private List<String> lastObjectAddresses;

    public ClientAdapter(Context context, List<Client> clients, List<String> lastObjectAddresses, ToolbarMenuActivity currentActivity) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        } else if (currentActivity == null) {
            throw new IllegalArgumentException("Current activity can't be null");
        } else if (clients == null) {
            throw new IllegalArgumentException("Clients list is null");
        } else if (lastObjectAddresses == null) {
            throw new IllegalArgumentException("Last object addresses list can't be null");
        }

        this.context = context;
        this.clients = clients;
        this.lastObjectAddresses = lastObjectAddresses;
        this.currentActivity = currentActivity;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientViewHolder(LayoutInflater.from(context).inflate(R.layout.client_list_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        // consider how we're gonna store files and photos on server, then fix it
        int resourceId = context.getResources().getIdentifier(clients.get(position).getPhoto(), "drawable",
                context.getPackageName());
        if (resourceId != 0) {
            holder.photoView.setImageResource(resourceId);
        }

        holder.nameView.setText(clients.get(position).getName());
        holder.passportNumberView.setText(clients.get(position).getPassportNumber());
        holder.lastObjectAddressView.setText(lastObjectAddresses.get(position));
        holder.phoneNumberView.setText(clients.get(position).getPhoneNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToClientDetails = new Intent(context, ClientsDetailedActivity.class);

                Gson jsonConverter = new Gson();
                String chosenClient = jsonConverter.toJson(clients.get(holder.getAdapterPosition()));
                moveToClientDetails.putExtra("chosen_client", chosenClient);
                currentActivity.passEmployeeData(moveToClientDetails);
            }
        });

        if (currentActivity.getIntent().getBooleanExtra("FROM_CREATE_OPERATION", false)) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    finishChoiceWithResult("Клієнта успішно обрано!", clients.get(holder.getAdapterPosition()));

                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return clients.size();
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

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        if (clients == null) {
            throw new IllegalArgumentException("Clients list is null");
        }

        this.clients = clients;
    }

    public List<String> getLastObjectAddresses() {
        return lastObjectAddresses;
    }

    public void setLastObjectAddresses(List<String> lastObjectAddresses) {
        if (lastObjectAddresses == null) {
            throw new IllegalArgumentException("Last object addresses list can't be null");
        }

        this.lastObjectAddresses = lastObjectAddresses;
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

    private void finishChoiceWithResult(String message, Client client) {
        Gson jsonConverter = new Gson();
        String resultClient = jsonConverter.toJson(client);

        Intent resultReturn = new Intent();
        resultReturn.putExtra("result_client", resultClient);
        currentActivity.setResult(Activity.RESULT_OK, resultReturn);

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        currentActivity.finish();
    }
}
