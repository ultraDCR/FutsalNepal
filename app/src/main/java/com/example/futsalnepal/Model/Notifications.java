package com.example.futsalnepal.Model;



import java.util.Date;

public class Notifications extends NotificationId{
    public String from;
    public String message;
    public String type;
    public Date timestamp;
    public String status;

    public Notifications(){}

    public Notifications(String from, String message, String type, Date timestamp, String status){
        this.from = from;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
