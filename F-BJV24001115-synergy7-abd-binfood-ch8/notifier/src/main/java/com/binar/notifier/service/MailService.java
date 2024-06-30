package com.binar.notifier.service;

import com.binar.notifier.stream.data.email.EmailSendOtpDto;

public interface MailService {
    void sendEmail(String to, String subject, String message);

    void sendVerificationEmail(EmailSendOtpDto sendOtpDto);

    void sendResetPasswordEmail(EmailSendOtpDto sendOtpDto);
}
