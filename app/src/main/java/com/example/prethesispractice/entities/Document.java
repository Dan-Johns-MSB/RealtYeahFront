package com.example.prethesispractice.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("documentId")
    @Expose
    private Integer documentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("operationId")
    @Expose
    private int operationId;

    public Document() {

    }

    public Document(Integer documentId, String name, String file, int operationId) {
        if (documentId != null && documentId < 1) {
            throw new IllegalArgumentException("Document ID can't be less than 1");
        } else if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Title string is either empty or null");
        } else if (file == null || file.isBlank()) {
            throw new IllegalArgumentException("File path string is either empty or null");
        } else if (operationId < 1) {
            throw new IllegalArgumentException("Operation ID can't be less than 1");
        }

        this.documentId = documentId;
        this.name = name;
        this.file = file;
        this.operationId = operationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Title string is either empty or null");
        }

        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        if (file == null || file.isBlank()) {
            throw new IllegalArgumentException("Path string is either empty or null");
        }

        this.file = file;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        if (documentId != null && documentId < 1) {
            throw new IllegalArgumentException("Document ID can't be less than 1");
        }

        this.documentId = documentId;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        if (operationId < 1) {
            throw new IllegalArgumentException("Operation ID can't be less than 1");
        }

        this.operationId = operationId;
    }
}
