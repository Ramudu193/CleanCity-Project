package com.cleancity.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRewardEmail(String toEmail, String volunteerName) {

        LocalDateTime receiveDateTime = LocalDateTime.now().plusDays(7);

        String subject = "Congratulations! CleanCity Reward Achieved";

        String body = "Dear " + volunteerName + ",\n\n"
                + "Congratulations! You have successfully completed 50 approved cleanup complaints.\n\n"
                + "As a reward, you will receive:\n"
                + "- Your favourite Indian sports player jersey with signature\n"
                + "- One free IPL match ticket\n\n"
                + "Reward receiving date and time:\n"
                + receiveDateTime + "\n\n"
                + "Thank you for helping build a cleaner city.\n\n"
                + "Regards,\n"
                + "CleanCity Team";

        sendSimpleEmail(toEmail, subject, body);
    }

    public void sendProofApprovedEmail(String toEmail, String volunteerName, int completedCount, String adminRemark) {

        String subject = "Cleanup Proof Approved - Great Work!";

        String body = "Dear " + volunteerName + ",\n\n"
                + "Your cleanup proof has been approved.\n\n"
                + "Admin Remark: " + adminRemark + "\n\n"
                + "Your completed cleanup count is now: " + completedCount + ".\n\n"
                + "Great work! Keep contributing to make the city cleaner.\n"
                + "Complete 50 approved cleanups to unlock your reward.\n\n"
                + "Regards,\n"
                + "CleanCity Team";

        sendSimpleEmail(toEmail, subject, body);
    }

    public void sendProofRejectedEmail(String toEmail, String volunteerName, String reason) {

        String subject = "Cleanup Proof Rejected - Please Try Again";

        String body = "Dear " + volunteerName + ",\n\n"
                + "Your cleanup proof was rejected.\n\n"
                + "Reason: " + reason + "\n\n"
                + "Please don’t give up. Upload a clearer video and try again.\n"
                + "Your effort still matters for a cleaner city.\n\n"
                + "Regards,\n"
                + "CleanCity Team";

        sendSimpleEmail(toEmail, subject, body);
    }

    private void sendSimpleEmail(String toEmail, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}