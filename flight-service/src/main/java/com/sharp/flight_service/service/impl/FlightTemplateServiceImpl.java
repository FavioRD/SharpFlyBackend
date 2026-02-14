package com.sharp.flight_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.flight_service.model.FlightTemplate;
import com.sharp.flight_service.repository.IAircraftRepository;
import com.sharp.flight_service.repository.IFlightTemplateRepository;
import com.sharp.flight_service.repository.IRouteRepository;
import com.sharp.flight_service.service.IFlightTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FlightTemplateServiceImpl implements IFlightTemplateService {

  private final IFlightTemplateRepository flightTemplateRepository;
  private final IRouteRepository routeRepository;
  private final IAircraftRepository aircraftRepository;

  @Override
  public FlightTemplate createFlightTemplate(FlightTemplate flightTemplate) {
    log.info("Creating flight template with number: {}", flightTemplate.getFlightNumber());

    if (flightTemplateRepository.existsByFlightNumber(flightTemplate.getFlightNumber())) {
      throw new IllegalArgumentException("Flight template with number " +
          flightTemplate.getFlightNumber() + " already exists");
    }

    Long routeId = flightTemplate.getRoute().getRouteId();
    Long aircraftId = flightTemplate.getAircraft().getAircraftId();

    if (!routeRepository.existsById(routeId)) {
      throw new IllegalArgumentException("Route not found with id: " + routeId);
    }

    if (!aircraftRepository.existsById(aircraftId)) {
      throw new IllegalArgumentException("Aircraft not found with id: " + aircraftId);
    }

    return flightTemplateRepository.save(flightTemplate);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<FlightTemplate> getFlightTemplateById(Long id) {
    log.info("Fetching flight template by id: {}", id);
    return flightTemplateRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<FlightTemplate> getFlightTemplateByNumber(String flightNumber) {
    log.info("Fetching flight template by number: {}", flightNumber);
    return flightTemplateRepository.findByFlightNumber(flightNumber);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightTemplate> getAllFlightTemplates() {
    log.info("Fetching all flight templates");
    return flightTemplateRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightTemplate> getActiveFlightTemplates() {
    log.info("Fetching active flight templates");
    return flightTemplateRepository.findAllActive();
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightTemplate> getFlightTemplatesByRoute(Long routeId) {
    log.info("Fetching flight templates by route: {}", routeId);
    return flightTemplateRepository.findByRouteId(routeId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightTemplate> getFlightTemplatesByAircraft(Long aircraftId) {
    log.info("Fetching flight templates by aircraft: {}", aircraftId);
    return flightTemplateRepository.findByAircraftId(aircraftId);
  }

  @Override
  public FlightTemplate updateFlightTemplate(Long id, FlightTemplate flightTemplate) {
    log.info("Updating flight template with id: {}", id);
    FlightTemplate existingTemplate = flightTemplateRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Flight template not found with id: " + id));

    String newFlightNumber = flightTemplate.getFlightNumber();
    if (!existingTemplate.getFlightNumber().equals(newFlightNumber) &&
        flightTemplateRepository.existsByFlightNumber(newFlightNumber)) {
      throw new IllegalArgumentException("Flight template with number " + newFlightNumber + " already exists");
    }

    Long routeId = flightTemplate.getRoute().getRouteId();
    Long aircraftId = flightTemplate.getAircraft().getAircraftId();

    if (!routeRepository.existsById(routeId)) {
      throw new IllegalArgumentException("Route not found with id: " + routeId);
    }

    if (!aircraftRepository.existsById(aircraftId)) {
      throw new IllegalArgumentException("Aircraft not found with id: " + aircraftId);
    }

    existingTemplate.setFlightNumber(flightTemplate.getFlightNumber());
    existingTemplate.setRoute(flightTemplate.getRoute());
    existingTemplate.setAircraft(flightTemplate.getAircraft());
    existingTemplate.setDefaultDurationMinutes(flightTemplate.getDefaultDurationMinutes());
    existingTemplate.setIsActive(flightTemplate.getIsActive());

    return flightTemplateRepository.save(existingTemplate);
  }

  @Override
  public boolean deleteFlightTemplate(Long id) {
    log.info("Deleting flight template with id: {}", id);
    if (!flightTemplateRepository.existsById(id)) {
      return false;
    }
    flightTemplateRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByFlightNumber(String flightNumber) {
    return flightTemplateRepository.existsByFlightNumber(flightNumber);
  }
}
