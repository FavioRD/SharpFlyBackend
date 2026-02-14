package com.sharp.flight_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharp.flight_service.model.Aircraft;
import com.sharp.flight_service.service.IAircraftService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/aircraft")
@RequiredArgsConstructor
public class AircraftController {

  private final IAircraftService aircraftService;

  @PostMapping
  public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
    log.info("POST /api/v1/aircraft - Creating aircraft: {}", aircraft.getCode());
    Aircraft created = aircraftService.createAircraft(aircraft);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
    log.info("GET /api/v1/aircraft/{} - Fetching aircraft", id);
    return aircraftService.getAircraftById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/code/{code}")
  public ResponseEntity<Aircraft> getAircraftByCode(@PathVariable String code) {
    log.info("GET /api/v1/aircraft/code/{} - Fetching aircraft by code", code);
    return aircraftService.getAircraftByCode(code)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Aircraft>> getAllAircraft(
      @RequestParam(required = false) Boolean activeOnly) {
    log.info("GET /api/v1/aircraft - Fetching all aircraft, activeOnly: {}", activeOnly);
    List<Aircraft> aircraft = Boolean.TRUE.equals(activeOnly)
        ? aircraftService.getActiveAircraft()
        : aircraftService.getAllAircraft();
    return ResponseEntity.ok(aircraft);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraft) {
    log.info("PUT /api/v1/aircraft/{} - Updating aircraft", id);
    try {
      Aircraft updated = aircraftService.updateAircraft(id, aircraft);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating aircraft: {}", e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
    log.info("DELETE /api/v1/aircraft/{} - Deleting aircraft", id);
    boolean deleted = aircraftService.deleteAircraft(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/exists/code/{code}")
  public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
    log.info("GET /api/v1/aircraft/exists/code/{} - Checking existence", code);
    return ResponseEntity.ok(aircraftService.existsByCode(code));
  }
}
