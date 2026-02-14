package com.sharp.flight_service.service;

import java.util.List;
import java.util.Optional;

import com.sharp.flight_service.model.Aircraft;

public interface IAircraftService {

  Aircraft createAircraft(Aircraft aircraft);

  Optional<Aircraft> getAircraftById(Long id);

  Optional<Aircraft> getAircraftByCode(String code);

  List<Aircraft> getAllAircraft();

  List<Aircraft> getActiveAircraft();

  Aircraft updateAircraft(Long id, Aircraft aircraft);

  boolean deleteAircraft(Long id);

  boolean existsByCode(String code);
}
