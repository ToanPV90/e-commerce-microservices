package com.mikhailkarpov.auth.repository;

import com.mikhailkarpov.auth.entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends CrudRepository<AppUser, UUID> {

    boolean existsByUsername(String username);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<AppUser> findByUsername(@Param("username") String username);
}
