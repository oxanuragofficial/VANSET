package vanset_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vanset_backend.dto.AuthResponse;
import vanset_backend.entity.User;
import vanset_backend.entity.UserRole;
import vanset_backend.repository.UserRepository;

@Service
public class AuthService {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
            OtpService otpService,
            UserRepository userRepository,
            JwtService jwtService) {

        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse verifyOtpAndLogin(
            String identifier,
            String otp) {

        boolean verified =
                otpService.verifyOtp(
                        identifier,
                        otp
                );

        if (!verified) {
            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }

        String normalizedIdentifier =
                identifier.trim().toLowerCase();

        User user =
                userRepository
                        .findByPhoneOrEmail(
                                normalizedIdentifier,
                                normalizedIdentifier
                        )
                        .orElseGet(() -> {

                            User newUser = new User();

                            if (normalizedIdentifier.contains("@")) {
                                newUser.setEmail(
                                        normalizedIdentifier
                                );
                            } else {
                                newUser.setPhone(
                                        normalizedIdentifier
                                );
                            }

                            newUser.setName("Vanset Customer");
                            newUser.setRole(UserRole.CUSTOMER);

                            return userRepository.save(
                                    newUser
                            );
                        });

        String token =
                jwtService.generateToken(user);

        AuthResponse response =
                new AuthResponse();

        response.setToken(token);
        response.setUserId(user.getId());
        response.setRole(user.getRole().name());

        return response;
    }
}