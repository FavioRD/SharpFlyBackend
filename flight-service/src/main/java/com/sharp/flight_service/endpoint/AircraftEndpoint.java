package com.sharp.flight_service.endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.soapaircraft.GetAircraftRequest;
import com.example.soapaircraft.GetAircraftResponse;
import com.example.soapaircraft.GetAircraftsResponse;
import com.sharp.flight_service.service.AircraftService;


@Endpoint
public class AircraftEndpoint {
	
	private static final String NAMESPACE = "http://example.com/soapaircraft";
	@Autowired
	private AircraftService service;

	@PayloadRoot(namespace = NAMESPACE, localPart = "getAircraftsRequest")
	@ResponsePayload
	public GetAircraftsResponse getSalida1() {		
		return service.responseLista();
	}

	@PayloadRoot(namespace = NAMESPACE, localPart = "getAircraftRequest")
	@ResponsePayload
	public GetAircraftResponse getSalida10(@RequestPayload GetAircraftRequest request) {		
		return service.responseBuscar(request.getAircraftId());
	}
}
