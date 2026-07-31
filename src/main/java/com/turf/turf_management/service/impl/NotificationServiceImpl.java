package com.turf.turf_management.service.impl;

import com.turf.turf_management.model.Player;
import com.turf.turf_management.service.EmailService;
import com.turf.turf_management.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;


    // 🏏 1. Player Created
    @Override
    public void playerCreated(String email, String playerName) {

        Map<String, Object> data = new HashMap<>();
        data.put("name", playerName);

        try {
            emailService.sendHtmlEmail(
                    email,
                    "🏏 Welcome to Mavericks Cricket Club",
                    "emails/player-welcome",
                    data
            );
        } catch (Exception e) {
            log.error("Failed to send player welcome email", e);
        }
    }

    @Override
    public void bookingCreated(String email, String playerName, String turfName, String date, String time, String location, String notes) {

        Map<String, Object> data = new HashMap<>();
        data.put("name", playerName);
        data.put("turfName", turfName);
        data.put("date", date);
        data.put("time", time);
        data.put("location", location);
        data.put("notes", notes);

        emailService.sendHtmlEmail(
                email,
                "🏏 Match Booking Confirmed - Mavericks Cricket Club",
                "emails/booking-confirmation",
                data
        );
    }

    // 🏏 3. Contribution Generated
    @Override
    public void contributionGenerated(String email, String playerName, double amount, String turfName, String date, String time) {

        Map<String, Object> data = new HashMap<>();
        data.put("name", playerName);
        data.put("amount", amount);
        data.put("turfName", turfName);
        data.put("date", date);
        data.put("time", time);

        emailService.sendHtmlEmail(
                email,
                "💰 Contribution Details - Mavericks Cricket Club",
                "emails/contribution",
                data
        );
    }

    @Override
    public void paymentReceived(String email, String playerName, double amount) {

        Map<String, Object> data = new HashMap<>();
        data.put("name", playerName);
        data.put("amount", amount);

        emailService.sendHtmlEmail(
                email,
                "✅ Payment Received - Mavericks Cricket Club",
                "emails/payment",
                data
        );
    }

    @Async
    public void sendBirthdayWish(String email, String name) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("name", name);

        emailService.sendHtmlEmail(
                email,
                "🎉 Happy Birthday from Mavericks Cricket Club 🏏",
                "emails/birthday-email",
                data
        );
    }
}
