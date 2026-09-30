package vanset_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Collection;

public interface CollectionRepository
        extends JpaRepository<Collection, Long> {
}