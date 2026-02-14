package com.sharp.flight_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.flight_service.model.Aircraft;
import com.sharp.flight_service.repository.IAircraftRepository;
import com.sharp.flight_service.service.IAircraftService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AircraftServiceImpl implements IAircraftService {

  private final IAircraftRepository aircraftRepository;

  @Override
  public Aircraft createAircraft(Aircraft aircraft) {
    log.info("Creating aircraft with code: {}", aircraft.getCode());
    if (aircraftRepository.existsByCode(aircraft.getCode())) {
      throw new IllegalArgumentException("Aircraft with code " + aircraft.getCode() + " already exists");
    }
    return aircraftRepository.save(aircraft);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Aircraft> getAircraftById(Long id) {
    log.info("Fetching aircraft by id: {}", id);
    return aircraftRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Aircraft> getAircraftByCode(String code) {
    log.info("Fetching aircraft by code: {}", code);
    return aircraftRepository.findByCode(code);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Aircraft> getAllAircraft() {
    log.info("Fetching all aircraft");
    return aircraftRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Aircraft> getActiveAircraft() {
    log.info("Fetching active aircraft");
    return aircraftRepository.findByIsActiveTrue();
  }

  @Override
  public Aircraft updateAircraft(Long id, Aircraft aircraft) {
    log.info("Updating aircraft with id: {}", id);
    Aircraft existingAircraft = aircraftRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with id: " + id));

    if (!existingAircraft.getCode().equals(aircraft.getCode()) &&
        aircraftRepository.existsByCode(aircraft.getCode())) {
      throw new IllegalArgumentException("Aircraft with code " + aircraft.getCode() + " already exists");
    }

    existingAircraft.setCode(aircraft.getCode());
    existingAircraft.setModel(aircraft.getModel());
    existingAircraft.setSeatCapacity(aircraft.getSeatCapacity());
    existingAircraft.setIsActive(aircraft.getIsActive());

    return aircraftRepository.save(existingAircraft);
  }

  @Override
  public boolean deleteAircraft(Long id) {
    log.info("Deleting aircraft with id: {}", id);
    if (!aircraftRepository.existsById(id)) {
      return false;
    }
    aircraftRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByCode(String code) {
    return aircraftRepository.existsByCode(code);
  }
}
