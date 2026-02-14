package com.sharp.flight_service.service;

import java.util.List;
import java.util.Optional;

import com.sharp.flight_service.model.Route;

public interface IRouteService {

  Route createRoute(Route route);

  Optional<Route> getRouteById(Long id);

  Optional<Route> getRouteByOriginAndDestination(Long originAirportId, Long destinationAirportId);

  List<Route> getAllRoutes();

  List<Route> getActiveRoutes();

  List<Route> getRoutesByOriginAirport(Long originAirportId);

  List<Route> getRoutesByDestinationAirport(Long destinationAirportId);

  Route updateRoute(Long id, Route route);

  boolean deleteRoute(Long id);

  boolean existsRoute(Long originAirportId, Long destinationAirportId);
}
