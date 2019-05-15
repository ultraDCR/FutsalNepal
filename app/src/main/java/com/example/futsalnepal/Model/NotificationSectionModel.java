package com.example.futsalnepal.Model;

import java.util.List;

public class NotificationSectionModel {
    public String sectionLabel;
    public List<Notifications> notificationList;

    public NotificationSectionModel(String sectionLabel, List<Notifications> notificationList) {
        this.sectionLabel = sectionLabel;
        this.notificationList = notificationList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public List<Notifications> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notifications> notificationList) {
        this.notificationList = notificationList;
    }
}
