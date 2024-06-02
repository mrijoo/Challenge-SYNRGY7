package com.ch.binarfud.service;

public interface MailService {
    void sendEmail(String to, String subject, String message);

    void sendVerificationEmail(String to, String username, String otp);

    void sendResetPasswordEmail(String to, String username, String resetPasswordUrl);
}
