package com.sharp.flight_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharp.flight_service.model.FlightInstance;
import com.sharp.flight_service.model.FlightStatus;
import com.sharp.flight_service.service.IFlightInstanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/flight-instances")
@RequiredArgsConstructor
public class FlightInstanceController {

  private final IFlightInstanceService flightInstanceService;

  @PostMapping
  public ResponseEntity<FlightInstance> createFlightInstance(@RequestBody FlightInstance flightInstance) {
    log.info("POST /api/v1/flight-instances - Creating flight instance");
    try {
      FlightInstance created = flightInstanceService.createFlightInstance(flightInstance);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      log.error("Error creating flight instance: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<FlightInstance> getFlightInstanceById(@PathVariable Long id) {
    log.info("GET /api/v1/flight-instances/{} - Fetching flight instance", id);
    return flightInstanceService.getFlightInstanceById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<FlightInstance>> getAllFlightInstances() {
    log.info("GET /api/v1/flight-instances - Fetching all flight instances");
    return ResponseEntity.ok(flightInstanceService.getAllFlightInstances());
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<FlightInstance>> getFlightInstancesByStatus(@PathVariable FlightStatus status) {
    log.info("GET /api/v1/flight-instances/status/{} - Fetching flight instances by status", status);
    return ResponseEntity.ok(flightInstanceService.getFlightInstancesByStatus(status));
  }

  @GetMapping("/by-date-range")
  public ResponseEntity<List<FlightInstance>> getFlightInstancesByDateRange(
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
    log.info("GET /api/v1/flight-instances/by-date-range - Fetching flights between {} and {}", start, end);
    return ResponseEntity.ok(flightInstanceService.getFlightInstancesByDateRange(start, end));
  }

  @GetMapping("/by-template/{templateId}")
  public ResponseEntity<List<FlightInstance>> getFlightInstancesByTemplate(@PathVariable Long templateId) {
    log.info("GET /api/v1/flight-instances/by-template/{} - Fetching flight instances by template", templateId);
    return ResponseEntity.ok(flightInstanceService.getFlightInstancesByTemplate(templateId));
  }

  @GetMapping("/search")
  public ResponseEntity<List<FlightInstance>> searchFlights(
      @RequestParam(required = false) Long originAirportId,
      @RequestParam(required = false) String originCity,
      @RequestParam(required = false) String destinationCity,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime start,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
    log.info("GET /api/v1/flight-instances/search - Searching flights");

    if (originAirportId != null) {
      return ResponseEntity.ok(flightInstanceService.getFlightInstancesByOriginAndDateRange(
          originAirportId, start, end));
    }

    if (originCity != null && destinationCity != null) {
      return ResponseEntity.ok(flightInstanceService.getFlightInstancesByCitiesAndDateRange(
          originCity, destinationCity, start, end));
    }

    return ResponseEntity.badRequest().build();
  }

  @GetMapping("/upcoming")
  public ResponseEntity<List<FlightInstance>> getUpcomingFlights() {
    log.info("GET /api/v1/flight-instances/upcoming - Fetching upcoming flights");
    return ResponseEntity.ok(flightInstanceService.getUpcomingFlights());
  }

  @PutMapping("/{id}")
  public ResponseEntity<FlightInstance> updateFlightInstance(
      @PathVariable Long id,
      @RequestBody FlightInstance flightInstance) {
    log.info("PUT /api/v1/flight-instances/{} - Updating flight instance", id);
    try {
      FlightInstance updated = flightInstanceService.updateFlightInstance(id, flightInstance);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating flight instance: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<FlightInstance> updateStatus(
      @PathVariable Long id,
      @RequestParam FlightStatus status) {
    log.info("PATCH /api/v1/flight-instances/{}/status - Updating status to: {}", id, status);
    try {
      FlightInstance updated = flightInstanceService.updateStatus(id, status);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating flight instance status: {}", e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFlightInstance(@PathVariable Long id) {
    log.info("DELETE /api/v1/flight-instances/{} - Deleting flight instance", id);
    boolean deleted = flightInstanceService.deleteFlightInstance(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
