package com.example.mytable;

public class Complaint {
    private String description, Id, clientId, cookId, title, status;

    public Complaint() {
    }

    public Complaint(String description, String clientId, String cookId, String title, String status) {
        this.description = description;
        this.clientId = clientId;
        this.cookId = cookId;
        this.title = title;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCookId() {
        return cookId;
    }

    public void setCookId(String cookId) {
        this.cookId = cookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id){
        this.Id = id;
    }
    public String getId(){
        return Id;
    }
}
