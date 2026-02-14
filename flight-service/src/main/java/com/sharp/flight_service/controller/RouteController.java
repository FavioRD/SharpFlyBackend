package com.sharp.flight_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sharp.flight_service.model.Route;
import com.sharp.flight_service.service.IRouteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

  private final IRouteService routeService;

  @PostMapping
  public ResponseEntity<Route> createRoute(@RequestBody Route route) {
    log.info("POST /api/v1/routes - Creating route");
    try {
      Route created = routeService.createRoute(route);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      log.error("Error creating route: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
    log.info("GET /api/v1/routes/{} - Fetching route", id);
    return routeService.getRouteById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/by-airports")
  public ResponseEntity<Route> getRouteByOriginAndDestination(
      @RequestParam Long originId,
      @RequestParam Long destinationId) {
    log.info("GET /api/v1/routes/by-airports - origin: {}, destination: {}", originId, destinationId);
    return routeService.getRouteByOriginAndDestination(originId, destinationId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Route>> getAllRoutes(
      @RequestParam(required = false) Boolean activeOnly) {
    log.info("GET /api/v1/routes - Fetching all routes, activeOnly: {}", activeOnly);
    List<Route> routes = Boolean.TRUE.equals(activeOnly)
        ? routeService.getActiveRoutes()
        : routeService.getAllRoutes();
    return ResponseEntity.ok(routes);
  }

  @GetMapping("/by-origin/{originId}")
  public ResponseEntity<List<Route>> getRoutesByOrigin(@PathVariable Long originId) {
    log.info("GET /api/v1/routes/by-origin/{} - Fetching routes by origin", originId);
    return ResponseEntity.ok(routeService.getRoutesByOriginAirport(originId));
  }

  @GetMapping("/by-destination/{destId}")
  public ResponseEntity<List<Route>> getRoutesByDestination(@PathVariable Long destId) {
    log.info("GET /api/v1/routes/by-destination/{} - Fetching routes by destination", destId);
    return ResponseEntity.ok(routeService.getRoutesByDestinationAirport(destId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody Route route) {
    log.info("PUT /api/v1/routes/{} - Updating route", id);
    try {
      Route updated = routeService.updateRoute(id, route);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      log.error("Error updating route: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
    log.info("DELETE /api/v1/routes/{} - Deleting route", id);
    boolean deleted = routeService.deleteRoute(id);
    return deleted
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/exists")
  public ResponseEntity<Boolean> existsRoute(
      @RequestParam Long originId,
      @RequestParam Long destinationId) {
    log.info("GET /api/v1/routes/exists - Checking route existence");
    return ResponseEntity.ok(routeService.existsRoute(originId, destinationId));
  }
}
