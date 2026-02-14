package com.sharp.flight_service.service;

import java.util.List;
import java.util.Optional;

import com.sharp.flight_service.model.FlightTemplate;

public interface IFlightTemplateService {

  FlightTemplate createFlightTemplate(FlightTemplate flightTemplate);

  Optional<FlightTemplate> getFlightTemplateById(Long id);

  Optional<FlightTemplate> getFlightTemplateByNumber(String flightNumber);

  List<FlightTemplate> getAllFlightTemplates();

  List<FlightTemplate> getActiveFlightTemplates();

  List<FlightTemplate> getFlightTemplatesByRoute(Long routeId);

  List<FlightTemplate> getFlightTemplatesByAircraft(Long aircraftId);

  FlightTemplate updateFlightTemplate(Long id, FlightTemplate flightTemplate);

  boolean deleteFlightTemplate(Long id);

  boolean existsByFlightNumber(String flightNumber);
}
