package com.aziz.taskapi.repository;

import com.aziz.taskapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for querying and persisting {@link AppUser} entities.
 */
public interface UserRepository extends JpaRepository<AppUser, Long> {
    /**
     * Finds a user by username.
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Checks whether a username is already in use.
     */
    boolean existsByUsername(String username);
}
