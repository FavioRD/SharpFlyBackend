package com.sharp.flight_service.service;

import java.util.List;
import java.util.Optional;

import com.sharp.flight_service.model.Airport;

public interface IAirportService {

  Airport createAirport(Airport airport);

  Optional<Airport> getAirportById(Long id);

  Optional<Airport> getAirportByIata(String iata);

  List<Airport> getAllAirports();

  List<Airport> getActiveAirports();

  Airport updateAirport(Long id, Airport airport);

  boolean deleteAirport(Long id);

  boolean existsByIata(String iata);
}
