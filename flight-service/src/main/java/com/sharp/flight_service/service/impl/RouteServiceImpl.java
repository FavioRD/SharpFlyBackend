package com.sharp.flight_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.flight_service.model.Route;
import com.sharp.flight_service.repository.IAirportRepository;
import com.sharp.flight_service.repository.IRouteRepository;
import com.sharp.flight_service.service.IRouteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements IRouteService {

  private final IRouteRepository routeRepository;
  private final IAirportRepository airportRepository;

  @Override
  public Route createRoute(Route route) {
    log.info("Creating route from origin {} to destination {}",
        route.getOriginAirport().getAirportId(),
        route.getDestinationAirport().getAirportId());

    Long originId = route.getOriginAirport().getAirportId();
    Long destId = route.getDestinationAirport().getAirportId();

    if (originId.equals(destId)) {
      throw new IllegalArgumentException("Origin and destination airports cannot be the same");
    }

    Optional<Route> existingRoute = routeRepository.findByOriginAndDestination(originId, destId);
    if (existingRoute.isPresent() && existingRoute.get().getIsActive()) {
      throw new IllegalArgumentException("Route already exists between these airports");
    }

    return routeRepository.save(route);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Route> getRouteById(Long id) {
    log.info("Fetching route by id: {}", id);
    return routeRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Route> getRouteByOriginAndDestination(Long originAirportId, Long destinationAirportId) {
    log.info("Fetching route from origin {} to destination {}", originAirportId, destinationAirportId);
    return routeRepository.findByOriginAndDestination(originAirportId, destinationAirportId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Route> getAllRoutes() {
    log.info("Fetching all routes");
    return routeRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Route> getActiveRoutes() {
    log.info("Fetching active routes");
    return routeRepository.findAllActiveWithAirports();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Route> getRoutesByOriginAirport(Long originAirportId) {
    log.info("Fetching routes by origin airport: {}", originAirportId);
    return routeRepository.findByOriginAirportId(originAirportId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Route> getRoutesByDestinationAirport(Long destinationAirportId) {
    log.info("Fetching routes by destination airport: {}", destinationAirportId);
    return routeRepository.findByDestinationAirportId(destinationAirportId);
  }

  @Override
  public Route updateRoute(Long id, Route route) {
    log.info("Updating route with id: {}", id);
    Route existingRoute = routeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Route not found with id: " + id));

    Long originId = route.getOriginAirport().getAirportId();
    Long destId = route.getDestinationAirport().getAirportId();

    if (originId.equals(destId)) {
      throw new IllegalArgumentException("Origin and destination airports cannot be the same");
    }

    existingRoute.setOriginAirport(route.getOriginAirport());
    existingRoute.setDestinationAirport(route.getDestinationAirport());
    existingRoute.setIsActive(route.getIsActive());

    return routeRepository.save(existingRoute);
  }

  @Override
  public boolean deleteRoute(Long id) {
    log.info("Deleting route with id: {}", id);
    if (!routeRepository.existsById(id)) {
      return false;
    }
    routeRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsRoute(Long originAirportId, Long destinationAirportId) {
    return routeRepository.findByOriginAndDestination(originAirportId, destinationAirportId).isPresent();
  }
}
