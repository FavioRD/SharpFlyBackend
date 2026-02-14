package com.sharp.flight_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharp.flight_service.model.Airport;
import com.sharp.flight_service.repository.IAirportRepository;
import com.sharp.flight_service.service.IAirportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AirportServiceImpl implements IAirportService {

  private final IAirportRepository airportRepository;

  @Override
  public Airport createAirport(Airport airport) {
    log.info("Creating airport with IATA code: {}", airport.getIata());
    if (airportRepository.existsByIata(airport.getIata())) {
      throw new IllegalArgumentException("Airport with IATA code " + airport.getIata() + " already exists");
    }
    return airportRepository.save(airport);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Airport> getAirportById(Long id) {
    log.info("Fetching airport by id: {}", id);
    return airportRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Airport> getAirportByIata(String iata) {
    log.info("Fetching airport by IATA code: {}", iata);
    return airportRepository.findByIata(iata);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Airport> getAllAirports() {
    log.info("Fetching all airports");
    return airportRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Airport> getActiveAirports() {
    log.info("Fetching active airports");
    return airportRepository.findAll().stream()
        .filter(Airport::getIsActive)
        .toList();
  }

  @Override
  public Airport updateAirport(Long id, Airport airport) {
    log.info("Updating airport with id: {}", id);
    Airport existingAirport = airportRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Airport not found with id: " + id));

    if (!existingAirport.getIata().equals(airport.getIata()) &&
        airportRepository.existsByIata(airport.getIata())) {
      throw new IllegalArgumentException("Airport with IATA code " + airport.getIata() + " already exists");
    }

    existingAirport.setIata(airport.getIata());
    existingAirport.setName(airport.getName());
    existingAirport.setCity(airport.getCity());
    existingAirport.setCountry(airport.getCountry());
    existingAirport.setTimezone(airport.getTimezone());
    existingAirport.setIsActive(airport.getIsActive());

    return airportRepository.save(existingAirport);
  }

  @Override
  public boolean deleteAirport(Long id) {
    log.info("Deleting airport with id: {}", id);
    if (!airportRepository.existsById(id)) {
      return false;
    }
    airportRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByIata(String iata) {
    return airportRepository.existsByIata(iata);
  }
}
