package com.sharp.flight_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharp.flight_service.model.Airport;
import com.sharp.flight_service.service.IAirportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
public class AirportController {

  private final IAirportService airportService;

  @PostMapping
  public ResponseEntity<Airport> createAirport(@RequestBody Airport airport) {
    log.info("POST /api/v1/airports - Creating airport: {}", airport.getIata());
    Airport created = airportService.createAirport(airport);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Airport> getAirportById(@PathVariable Long id) {
    log.info("GET /api/v1/airports/{} - Fetching airport", id);
    return airportService.getAirportById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/iata/{iata}")
  public ResponseEntity<Airport> getAirportByIata(@PathVariable String iata) {
    log.info("GET /api/v1/airports/iata/{} - Fetching airport by IATA", iata);
    return airportService.getAirportByIata(iata)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Airport>> getAllAirports(
      @RequestParam(required = false) Boolean activeOnly) {
    log.info("GET /api/v1/airports - Fetching all airports, activeOnly: {}", activeOnly);
    List<Airport> airports = Boolean.TRUE.equals(activeOnly)
        ? airportService.getActiveAirports()
        : airportService.getAllAirports();
    return ResponseEntity.ok(airports);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Airport> updateAirport(@PathVariable Long id, @RequestBody Airport airport) {
    log.info("PUT /api/v1/airports/{} - Updating airport", id);
    try {
      Airport updated = airportService.updateAirport(id, airport);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating airport: {}", e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
    log.info("DELETE /api/v1/airports/{} - Deleting airport", id);
    boolean deleted = airportService.deleteAirport(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/exists/iata/{iata}")
  public ResponseEntity<Boolean> existsByIata(@PathVariable String iata) {
    log.info("GET /api/v1/airports/exists/iata/{} - Checking existence", iata);
    return ResponseEntity.ok(airportService.existsByIata(iata));
  }
}
