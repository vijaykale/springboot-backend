package com.turf.turf_management.service;

public interface NotificationService {

    void playerCreated(String email, String playerName);

    void bookingCreated(String email, String playerName, String turfName, String date, String time, String location,String notes);

    void contributionGenerated(String email, String playerName, double amount, String turfName, String date, String time);

    void paymentReceived(String email, String playerName, double amount);

    void sendBirthdayWish(String email, String name);

}
