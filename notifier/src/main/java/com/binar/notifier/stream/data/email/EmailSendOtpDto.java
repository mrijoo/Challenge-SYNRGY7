package com.binar.notifier.stream.data.email;

import lombok.Data;

@Data
public class EmailSendOtpDto {
    private String to;
    private String username;
    private String otp;
}
