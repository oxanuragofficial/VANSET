package vanset_backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vanset_backend.dto.AuthResponse;
import vanset_backend.service.AuthService;
import vanset_backend.service.OtpService;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpController {

    private final OtpService otpService;
    private final AuthService authService;

    public OtpController(
            OtpService otpService,
            AuthService authService) {

        this.otpService = otpService;
        this.authService = authService;
    }

    @PostMapping("/request")
    public Map<String, String> requestOtp(
            @RequestBody Map<String, String> request) {

        String identifier =
                request.get("identifier");

        otpService.generateOtp(identifier);

        return Map.of(
                "message",
                "OTP sent successfully"
        );
    }

    @PostMapping("/verify")
    public AuthResponse verifyOtp(
            @RequestBody Map<String, String> request) {

        String identifier =
                request.get("identifier");

        String otp =
                request.get("otp");

        return authService.verifyOtpAndLogin(
                identifier,
                otp
        );
    }
}