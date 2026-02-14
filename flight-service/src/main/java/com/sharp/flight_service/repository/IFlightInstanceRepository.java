package com.sharp.flight_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sharp.flight_service.model.FlightInstance;
import com.sharp.flight_service.model.FlightStatus;

@Repository
public interface IFlightInstanceRepository extends JpaRepository<FlightInstance, Long> {

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport JOIN FETCH r.destinationAirport WHERE fi.flightInstanceId = :id")
	Optional<FlightInstance> findByIdWithDetails(@Param("id") Long id);

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport JOIN FETCH r.destinationAirport WHERE fi.status = :status")
	List<FlightInstance> findByStatus(@Param("status") FlightStatus status);

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport oa JOIN FETCH r.destinationAirport da WHERE fi.departureAt BETWEEN :start AND :end")
	List<FlightInstance> findByDepartureBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport oa JOIN FETCH r.destinationAirport da WHERE ft.flightTemplateId = :templateId")
	List<FlightInstance> findByFlightTemplateId(@Param("templateId") Long flightTemplateId);

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport oa JOIN FETCH r.destinationAirport da WHERE oa.airportId = :originId AND fi.departureAt BETWEEN :start AND :end")
	List<FlightInstance> findByOriginAndDateRange(@Param("originId") Long originAirportId,
			@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport oa JOIN FETCH r.destinationAirport da WHERE oa.city = :originCity AND da.city = :destCity AND fi.departureAt BETWEEN :start AND :end")
	List<FlightInstance> findByCitiesAndDateRange(@Param("originCity") String originCity,
			@Param("destCity") String destCity, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT fi FROM FlightInstance fi JOIN FETCH fi.flightTemplate ft JOIN FETCH ft.route r JOIN FETCH ft.aircraft a JOIN FETCH r.originAirport JOIN FETCH r.destinationAirport WHERE fi.departureAt > :now ORDER BY fi.departureAt")
	List<FlightInstance> findUpcomingFlights(@Param("now") LocalDateTime now);

	boolean existsByFlightTemplateFlightTemplateIdAndDepartureAt(Long flightTemplateId, LocalDateTime departureAt);

}
