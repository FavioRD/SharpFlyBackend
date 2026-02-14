package com.sharp.flight_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharp.flight_service.model.FlightTemplate;
import com.sharp.flight_service.service.IFlightTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/flight-templates")
@RequiredArgsConstructor
public class FlightTemplateController {

  private final IFlightTemplateService flightTemplateService;

  @PostMapping
  public ResponseEntity<FlightTemplate> createFlightTemplate(@RequestBody FlightTemplate flightTemplate) {
    log.info("POST /api/v1/flight-templates - Creating flight template: {}", flightTemplate.getFlightNumber());
    try {
      FlightTemplate created = flightTemplateService.createFlightTemplate(flightTemplate);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      log.error("Error creating flight template: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<FlightTemplate> getFlightTemplateById(@PathVariable Long id) {
    log.info("GET /api/v1/flight-templates/{} - Fetching flight template", id);
    return flightTemplateService.getFlightTemplateById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/number/{flightNumber}")
  public ResponseEntity<FlightTemplate> getFlightTemplateByNumber(@PathVariable String flightNumber) {
    log.info("GET /api/v1/flight-templates/number/{} - Fetching flight template by number", flightNumber);
    return flightTemplateService.getFlightTemplateByNumber(flightNumber)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<FlightTemplate>> getAllFlightTemplates(
      @RequestParam(required = false) Boolean activeOnly) {
    log.info("GET /api/v1/flight-templates - Fetching all flight templates, activeOnly: {}", activeOnly);
    List<FlightTemplate> templates = Boolean.TRUE.equals(activeOnly)
        ? flightTemplateService.getActiveFlightTemplates()
        : flightTemplateService.getAllFlightTemplates();
    return ResponseEntity.ok(templates);
  }

  @GetMapping("/by-route/{routeId}")
  public ResponseEntity<List<FlightTemplate>> getFlightTemplatesByRoute(@PathVariable Long routeId) {
    log.info("GET /api/v1/flight-templates/by-route/{} - Fetching flight templates by route", routeId);
    return ResponseEntity.ok(flightTemplateService.getFlightTemplatesByRoute(routeId));
  }

  @GetMapping("/by-aircraft/{aircraftId}")
  public ResponseEntity<List<FlightTemplate>> getFlightTemplatesByAircraft(@PathVariable Long aircraftId) {
    log.info("GET /api/v1/flight-templates/by-aircraft/{} - Fetching flight templates by aircraft", aircraftId);
    return ResponseEntity.ok(flightTemplateService.getFlightTemplatesByAircraft(aircraftId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<FlightTemplate> updateFlightTemplate(
      @PathVariable Long id,
      @RequestBody FlightTemplate flightTemplate) {
    log.info("PUT /api/v1/flight-templates/{} - Updating flight template", id);
    try {
      FlightTemplate updated = flightTemplateService.updateFlightTemplate(id, flightTemplate);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating flight template: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFlightTemplate(@PathVariable Long id) {
    log.info("DELETE /api/v1/flight-templates/{} - Deleting flight template", id);
    boolean deleted = flightTemplateService.deleteFlightTemplate(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/exists/number/{flightNumber}")
  public ResponseEntity<Boolean> existsByFlightNumber(@PathVariable String flightNumber) {
    log.info("GET /api/v1/flight-templates/exists/number/{} - Checking existence", flightNumber);
    return ResponseEntity.ok(flightTemplateService.existsByFlightNumber(flightNumber));
  }
}
