package com.sharp.flight_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sharp.flight_service.model.FlightTemplate;

@Repository
public interface IFlightTemplateRepository extends JpaRepository<FlightTemplate, Long> {

  Optional<FlightTemplate> findByFlightNumber(String flightNumber);

  Optional<FlightTemplate> findByFlightNumberAndIsActiveTrue(String flightNumber);

  @Query("SELECT ft FROM FlightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a WHERE ft.isActive = true")
  List<FlightTemplate> findAllActive();

  @Query("SELECT ft FROM FlightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a WHERE ft.route.routeId = :routeId AND ft.isActive = true")
  List<FlightTemplate> findByRouteId(@Param("routeId") Long routeId);

  @Query("SELECT ft FROM FlightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a WHERE ft.aircraft.aircraftId = :aircraftId AND ft.isActive = true")
  List<FlightTemplate> findByAircraftId(@Param("aircraftId") Long aircraftId);

  boolean existsByFlightNumber(String flightNumber);
}
