package com.sharp.flight_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.flight_service.model.FlightInstance;
import com.sharp.flight_service.model.FlightStatus;
import com.sharp.flight_service.repository.IFlightInstanceRepository;
import com.sharp.flight_service.repository.IFlightTemplateRepository;
import com.sharp.flight_service.service.IFlightInstanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FlightInstanceServiceImpl implements IFlightInstanceService {

  private final IFlightInstanceRepository flightInstanceRepository;
  private final IFlightTemplateRepository flightTemplateRepository;

  @Override
  public FlightInstance createFlightInstance(FlightInstance flightInstance) {
    log.info("Creating flight instance for template id: {} at {}",
        flightInstance.getFlightTemplate().getFlightTemplateId(),
        flightInstance.getDepartureAt());

    Long templateId = flightInstance.getFlightTemplate().getFlightTemplateId();
    LocalDateTime departureAt = flightInstance.getDepartureAt();

    if (!flightTemplateRepository.existsById(templateId)) {
      throw new IllegalArgumentException("Flight template not found with id: " + templateId);
    }

    if (flightInstanceRepository.existsByFlightTemplateFlightTemplateIdAndDepartureAt(templateId, departureAt)) {
      throw new IllegalArgumentException("Flight instance already exists for this template and departure time");
    }

    if (flightInstance.getArrivalAt().isBefore(flightInstance.getDepartureAt())) {
      throw new IllegalArgumentException("Arrival time cannot be before departure time");
    }

    return flightInstanceRepository.save(flightInstance);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<FlightInstance> getFlightInstanceById(Long id) {
    log.info("Fetching flight instance by id: {}", id);
    return flightInstanceRepository.findByIdWithDetails(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getAllFlightInstances() {
    log.info("Fetching all flight instances");
    return flightInstanceRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getFlightInstancesByStatus(FlightStatus status) {
    log.info("Fetching flight instances by status: {}", status);
    return flightInstanceRepository.findByStatus(status);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getFlightInstancesByDateRange(LocalDateTime start, LocalDateTime end) {
    log.info("Fetching flight instances between {} and {}", start, end);
    return flightInstanceRepository.findByDepartureBetween(start, end);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getFlightInstancesByTemplate(Long flightTemplateId) {
    log.info("Fetching flight instances by template: {}", flightTemplateId);
    return flightInstanceRepository.findByFlightTemplateId(flightTemplateId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getFlightInstancesByOriginAndDateRange(Long originAirportId,
      LocalDateTime start, LocalDateTime end) {
    log.info("Fetching flight instances from origin {} between {} and {}",
        originAirportId, start, end);
    return flightInstanceRepository.findByOriginAndDateRange(originAirportId, start, end);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getFlightInstancesByCitiesAndDateRange(String originCity,
      String destCity, LocalDateTime start, LocalDateTime end) {
    log.info("Fetching flight instances from {} to {} between {} and {}",
        originCity, destCity, start, end);
    return flightInstanceRepository.findByCitiesAndDateRange(originCity, destCity, start, end);
  }

  @Override
  @Transactional(readOnly = true)
  public List<FlightInstance> getUpcomingFlights() {
    log.info("Fetching upcoming flights");
    return flightInstanceRepository.findUpcomingFlights(LocalDateTime.now());
  }

  @Override
  public FlightInstance updateFlightInstance(Long id, FlightInstance flightInstance) {
    log.info("Updating flight instance with id: {}", id);
    FlightInstance existingInstance = flightInstanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Flight instance not found with id: " + id));

    Long templateId = flightInstance.getFlightTemplate().getFlightTemplateId();
    LocalDateTime departureAt = flightInstance.getDepartureAt();

    if (!flightTemplateRepository.existsById(templateId)) {
      throw new IllegalArgumentException("Flight template not found with id: " + templateId);
    }

    if (!existingInstance.getFlightTemplate().getFlightTemplateId().equals(templateId) ||
        !existingInstance.getDepartureAt().equals(departureAt)) {
      if (flightInstanceRepository.existsByFlightTemplateFlightTemplateIdAndDepartureAt(templateId, departureAt)) {
        throw new IllegalArgumentException("Flight instance already exists for this template and departure time");
      }
    }

    if (flightInstance.getArrivalAt().isBefore(flightInstance.getDepartureAt())) {
      throw new IllegalArgumentException("Arrival time cannot be before departure time");
    }

    existingInstance.setFlightTemplate(flightInstance.getFlightTemplate());
    existingInstance.setDepartureAt(flightInstance.getDepartureAt());
    existingInstance.setArrivalAt(flightInstance.getArrivalAt());
    existingInstance.setStatus(flightInstance.getStatus());
    existingInstance.setCapacity(flightInstance.getCapacity());

    return flightInstanceRepository.save(existingInstance);
  }

  @Override
  public FlightInstance updateStatus(Long id, FlightStatus status) {
    log.info("Updating flight instance {} status to: {}", id, status);
    FlightInstance existingInstance = flightInstanceRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Flight instance not found with id: " + id));

    existingInstance.setStatus(status);
    return flightInstanceRepository.save(existingInstance);
  }

  @Override
  public boolean deleteFlightInstance(Long id) {
    log.info("Deleting flight instance with id: {}", id);
    if (!flightInstanceRepository.existsById(id)) {
      return false;
    }
    flightInstanceRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsFlightInstance(Long flightTemplateId, LocalDateTime departureAt) {
    return flightInstanceRepository.existsByFlightTemplateFlightTemplateIdAndDepartureAt(flightTemplateId, departureAt);
  }
}
