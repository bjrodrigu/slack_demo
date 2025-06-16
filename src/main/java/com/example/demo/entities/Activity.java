package com.example.demo.entities;

public class Activity {
    private String activity;
    private double availability;
    private String type;
    private int participants;
    private double price;
    private String accessibility;
    private String duration;
    private boolean kidFriendly;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getAvailability() {
        return availability;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isKidFriendly() {
        return kidFriendly;
    }

    public void setKidFriendly(boolean kidFriendly) {
        this.kidFriendly = kidFriendly;
    }

    @Override
    public String toString() {
        return activity + '\n' +
            "availability: " + availability + '\n' +
            "type: '" + type + '\n' +
            "participants: " + participants + '\n' +
            "price: " + price + '\n' +
            "accessibility: " + accessibility + '\n' +
            "duration: " + duration;
    }
}
