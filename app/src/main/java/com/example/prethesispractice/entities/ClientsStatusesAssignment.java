package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientsStatusesAssignment {
    @SerializedName("clientId")
    @Expose
    private int clientId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("operationId")
    @Expose
    private int operationId;
    @SerializedName("requirements")
    @Expose
    private String requirements;

    public ClientsStatusesAssignment() {

    }

    public ClientsStatusesAssignment(int clientId, String status, int operationId,
                                     String requirements) {
        this.clientId = clientId;
        this.status = status;
        this.operationId = operationId;
        this.requirements = requirements;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
}
