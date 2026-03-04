package com.custody.sign.controller;

import com.custody.sign.service.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @PostMapping("/threshold-ecdsa")
    public ResponseEntity<String> thresholdSign(@RequestBody ThresholdSignRequest request) {
        try {
            signService.initDemoShares(request.privateKey());

            String signature = signService.thresholdSignEcdsa(
                    request.message(),
                    request.shareIds()
            );
            return ResponseEntity.ok("Threshold Signature: " + signature);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

record ThresholdSignRequest(String message, String privateKey, List<Integer> shareIds) {}