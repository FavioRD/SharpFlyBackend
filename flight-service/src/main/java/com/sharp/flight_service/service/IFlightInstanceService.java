package com.sharp.flight_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sharp.flight_service.model.FlightInstance;
import com.sharp.flight_service.model.FlightStatus;

public interface IFlightInstanceService {

  FlightInstance createFlightInstance(FlightInstance flightInstance);

  Optional<FlightInstance> getFlightInstanceById(Long id);

  List<FlightInstance> getAllFlightInstances();

  List<FlightInstance> getFlightInstancesByStatus(FlightStatus status);

  List<FlightInstance> getFlightInstancesByDateRange(LocalDateTime start, LocalDateTime end);

  List<FlightInstance> getFlightInstancesByTemplate(Long flightTemplateId);

  List<FlightInstance> getFlightInstancesByOriginAndDateRange(Long originAirportId, LocalDateTime start, LocalDateTime end);

  List<FlightInstance> getFlightInstancesByCitiesAndDateRange(String originCity, String destCity, LocalDateTime start, LocalDateTime end);

  List<FlightInstance> getUpcomingFlights();

  FlightInstance updateFlightInstance(Long id, FlightInstance flightInstance);

  FlightInstance updateStatus(Long id, FlightStatus status);

  boolean deleteFlightInstance(Long id);

  boolean existsFlightInstance(Long flightTemplateId, LocalDateTime departureAt);
}
