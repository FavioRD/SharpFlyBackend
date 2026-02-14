package com.sharp.flight_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sharp.flight_service.model.Route;

@Repository
public interface IRouteRepository extends JpaRepository<Route, Long> {

  @Query("SELECT r FROM Route r WHERE r.originAirport.airportId = :originId AND r.destinationAirport.airportId = :destId AND r.isActive = true")
  Optional<Route> findByOriginAndDestination(@Param("originId") Long originAirportId, @Param("destId") Long destinationAirportId);

  @Query("SELECT r FROM Route r JOIN FETCH r.originAirport JOIN FETCH r.destinationAirport WHERE r.originAirport.airportId = :originId AND r.isActive = true")
  List<Route> findByOriginAirportId(@Param("originId") Long originAirportId);

  @Query("SELECT r FROM Route r JOIN FETCH r.originAirport JOIN FETCH r.destinationAirport WHERE r.destinationAirport.airportId = :destId AND r.isActive = true")
  List<Route> findByDestinationAirportId(@Param("destId") Long destinationAirportId);

  @Query("SELECT r FROM Route r JOIN FETCH r.originAirport JOIN FETCH r.destinationAirport WHERE r.isActive = true")
  List<Route> findAllActiveWithAirports();
}
