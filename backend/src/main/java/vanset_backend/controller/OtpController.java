package vanset_backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vanset_backend.service.OtpService;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/request")
    public Map<String, String> requestOtp(
            @RequestBody Map<String, String> request) {

        String identifier = request.get("identifier");

        String otp = otpService.generateOtp(identifier);

        return Map.of(
                "message", "OTP generated",
                "otp", otp
        );
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyOtp(
            @RequestBody Map<String, String> request) {

        String identifier =
                request.get("identifier");

        String otp =
                request.get("otp");

        boolean verified =
                otpService.verifyOtp(
                        identifier,
                        otp
                );

        return Map.of(
                "verified",
                verified
        );
    }
}