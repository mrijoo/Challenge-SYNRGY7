package com.binar.notifier.stream.data.firebase;

import java.util.List;

import lombok.Data;

@Data
public class SendNotificationDto {
    private List<String> tokens;
    private String title;
    private String body;
}
