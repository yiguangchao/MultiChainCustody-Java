package com.custody.sign.controller;

import com.custody.sign.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @PostMapping("/ecdsa")
    public ResponseEntity<String> signEcdsa(@RequestBody SignRequest request) {
        try {
            String signature = signService.signEcdsa(request.message(), request.privateKey());
            return ResponseEntity.ok("Signature: " + signature);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

record SignRequest(String message, String privateKey) {}