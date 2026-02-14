package com.sharp.flight_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharp.flight_service.model.Airport;

@Repository
public interface IAirportRepository extends JpaRepository<Airport, Long> {

  Optional<Airport> findByIata(String iata);

  Optional<Airport> findByIataAndIsActiveTrue(String iata);

  boolean existsByIata(String iata);
}
