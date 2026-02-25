package com.sharp.flight_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.soapaircraft.GetAircraftResponse;
import com.example.soapaircraft.GetAircraftsResponse;
import com.sharp.flight_service.repository.AircraftRepository;

@Service
public class AircraftService {
    @Autowired
    private AircraftRepository repo;
	
	public GetAircraftsResponse responseLista() {
		GetAircraftsResponse x=new GetAircraftsResponse();
		x.setAircraft(repo.listAircrafts());
		return x;
	}
	public GetAircraftResponse responseBuscar(long l) {
		GetAircraftResponse x=new GetAircraftResponse();
		x.setAircraft(repo.findById(l));
		return x;
	}


}
