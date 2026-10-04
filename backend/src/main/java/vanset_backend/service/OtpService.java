package vanset_backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vanset_backend.entity.OtpRequest;
import vanset_backend.repository.OtpRequestRepository;

@Service
public class OtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;

    private final OtpRequestRepository otpRequestRepository;

    private final SecureRandom secureRandom =
            new SecureRandom();

    public OtpService(
            OtpRequestRepository otpRequestRepository) {

        this.otpRequestRepository =
                otpRequestRepository;
    }

    @Transactional
    public String generateOtp(String identifier) {

        String normalizedIdentifier =
                normalizeIdentifier(identifier);

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        OtpRequest otpRequest = new OtpRequest();

        otpRequest.setIdentifier(
                normalizedIdentifier
        );

        otpRequest.setOtpHash(
                hashOtp(otp)
        );

        otpRequest.setExpiresAt(
                LocalDateTime.now()
                        .plusMinutes(OTP_EXPIRY_MINUTES)
        );

        otpRequest.setAttempts(0);
        otpRequest.setVerified(false);
        otpRequest.setCreatedAt(
                LocalDateTime.now()
        );

        otpRequestRepository.save(otpRequest);

        /*
         * Temporary development return.
         *
         * This will NOT be exposed in the production
         * authentication API. A real SMS/email provider
         * will deliver the OTP to the user.
         */
        return otp;
    }

    @Transactional
    public boolean verifyOtp(
            String identifier,
            String otp) {

        String normalizedIdentifier =
                normalizeIdentifier(identifier);

        OtpRequest otpRequest =
                otpRequestRepository
                        .findTopByIdentifierOrderByCreatedAtDesc(
                                normalizedIdentifier
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "OTP request not found"
                                )
                        );

        if (otpRequest.isVerified()) {
            throw new IllegalStateException(
                    "OTP has already been used"
            );
        }

        if (LocalDateTime.now()
                .isAfter(otpRequest.getExpiresAt())) {

            throw new IllegalStateException(
                    "OTP has expired"
            );
        }

        if (otpRequest.getAttempts()
                >= MAX_ATTEMPTS) {

            throw new IllegalStateException(
                    "Maximum OTP attempts exceeded"
            );
        }

        otpRequest.setAttempts(
                otpRequest.getAttempts() + 1
        );

        boolean valid = MessageDigest.isEqual(
                hashOtp(otp).getBytes(StandardCharsets.UTF_8),
                otpRequest.getOtpHash()
                        .getBytes(StandardCharsets.UTF_8)
        );

        if (!valid) {

            otpRequestRepository.save(otpRequest);

            return false;
        }

        otpRequest.setVerified(true);

        otpRequestRepository.save(otpRequest);

        return true;
    }

    private String normalizeIdentifier(
            String identifier) {

        if (identifier == null ||
                identifier.isBlank()) {

            throw new IllegalArgumentException(
                    "Phone or email is required"
            );
        }

        return identifier
                .trim()
                .toLowerCase();
    }

    private String hashOtp(String otp) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            otp.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte b : hash) {

                result.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return result.toString();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to hash OTP",
                    e
            );
        }
    }
}