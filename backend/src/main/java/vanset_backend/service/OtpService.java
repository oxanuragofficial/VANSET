
package vanset_backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import vanset_backend.entity.OtpRequest;
import vanset_backend.exception.InvalidIdentifierException;
import vanset_backend.exception.OtpAttemptsExceededException;
import vanset_backend.exception.OtpExpiredException;
import vanset_backend.exception.OtpRateLimitException;
import vanset_backend.repository.OtpRequestRepository;

@Service
public class OtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    private static final int MAX_REQUESTS_PER_HOUR = 5;
    private static final int REQUEST_WINDOW_HOURS = 1;

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

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime requestWindowStart =
                now.minusHours(
                        REQUEST_WINDOW_HOURS
                );

        long requestCount =
                otpRequestRepository
                        .countByIdentifierAndCreatedAtAfter(
                                normalizedIdentifier,
                                requestWindowStart
                        );

        if (requestCount >= MAX_REQUESTS_PER_HOUR) {

            throw new OtpRateLimitException(
                    "Maximum OTP requests exceeded. Please try again later"
            );
        }

        OtpRequest latestRequest =
                otpRequestRepository
                        .findTopByIdentifierOrderByCreatedAtDesc(
                                normalizedIdentifier
                        )
                        .orElse(null);

        if (latestRequest != null) {

            LocalDateTime cooldownEndsAt =
                    latestRequest.getCreatedAt()
                            .plusSeconds(
                                    RESEND_COOLDOWN_SECONDS
                            );

            if (now.isBefore(cooldownEndsAt)) {

                throw new OtpRateLimitException(
                        "Please wait before requesting another OTP"
                );
            }
        }

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        OtpRequest otpRequest =
                new OtpRequest();

        otpRequest.setIdentifier(
                normalizedIdentifier
        );

        otpRequest.setOtpHash(
                hashOtp(otp)
        );

        otpRequest.setExpiresAt(
                now.plusMinutes(
                        OTP_EXPIRY_MINUTES
                )
        );

        otpRequest.setAttempts(0);
        otpRequest.setVerified(false);
        otpRequest.setCreatedAt(now);

        otpRequestRepository.save(
                otpRequest
        );

        /*
         * Development-only OTP logging.
         *
         * Production will use a real SMS/email provider.
         */
        System.out.println(
                "DEV OTP for "
                        + normalizedIdentifier
                        + ": "
                        + otp
        );

        return otp;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

            throw new OtpExpiredException(
                    "OTP has expired"
            );
        }

        if (otpRequest.getAttempts()
                >= MAX_ATTEMPTS) {

            throw new OtpAttemptsExceededException(
                    "Maximum OTP attempts exceeded"
            );
        }

        otpRequest.setAttempts(
                otpRequest.getAttempts() + 1
        );

        boolean valid = MessageDigest.isEqual(
                hashOtp(otp).getBytes(
                        StandardCharsets.UTF_8
                ),
                otpRequest.getOtpHash()
                        .getBytes(
                                StandardCharsets.UTF_8
                        )
        );

        if (!valid) {

            otpRequestRepository.save(
                    otpRequest
            );

            return false;
        }

        otpRequest.setVerified(true);

        otpRequestRepository.save(
                otpRequest
        );

        return true;
    }

    private String normalizeIdentifier(
            String identifier) {

        if (identifier == null ||
                identifier.isBlank()) {

            throw new InvalidIdentifierException(
                    "Phone or email is required"
            );
        }

        String normalizedIdentifier =
                identifier.trim().toLowerCase();

        boolean validPhone =
                normalizedIdentifier.matches(
                        "^[0-9]{10}$"
                );

        boolean validEmail =
                normalizedIdentifier.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                );

        if (!validPhone && !validEmail) {

            throw new InvalidIdentifierException(
                    "Enter a valid 10-digit phone number or email address"
            );
        }

        return normalizedIdentifier;
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
