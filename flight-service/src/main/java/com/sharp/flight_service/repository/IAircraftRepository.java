package com.sharp.flight_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.flight_service.model.Aircraft;

@Repository
public interface IAircraftRepository extends JpaRepository<Aircraft, Long> {

  Optional<Aircraft> findByCode(String code);

  Optional<Aircraft> findByCodeAndIsActiveTrue(String code);

  List<Aircraft> findByIsActiveTrue();

  boolean existsByCode(String code);
}
