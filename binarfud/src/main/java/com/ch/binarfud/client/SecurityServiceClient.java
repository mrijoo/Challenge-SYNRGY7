package com.ch.binarfud.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ch.binarfud.client.data.AddIdentityDto;

@FeignClient(name = "security-service")
public interface SecurityServiceClient {
    @GetMapping("/account/detail/{userId}")
    ResponseEntity<String> getAccountDetail(@PathVariable("userId") UUID userId);

    @PostMapping("/account/identity")
    ResponseEntity<String> addIdentity(@RequestBody AddIdentityDto addIdentityDto);
}
