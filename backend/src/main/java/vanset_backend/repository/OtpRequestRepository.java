package vanset_backend.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.OtpRequest;

public interface OtpRequestRepository
        extends JpaRepository<OtpRequest, Long> {

    Optional<OtpRequest> findTopByIdentifierOrderByCreatedAtDesc(
            String identifier
    );

    long countByIdentifierAndCreatedAtAfter(
            String identifier,
            LocalDateTime createdAt
    );
}