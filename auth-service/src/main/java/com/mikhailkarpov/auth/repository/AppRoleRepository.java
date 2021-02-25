package com.mikhailkarpov.auth.repository;

import com.mikhailkarpov.auth.entity.AppRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppRoleRepository extends CrudRepository<AppRole, Integer> {

    Optional<AppRole> findByName(String name);
}
