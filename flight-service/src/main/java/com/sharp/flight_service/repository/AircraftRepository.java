package com.sharp.flight_service.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.soapaircraft.Aircraft;

@Repository
public class AircraftRepository {
	@Autowired
	private JdbcTemplate template;

	private RowMapper<Aircraft> rowMapper = (rs, rowNum) -> {
	    try {
	        Aircraft aircraft = new Aircraft();
	        aircraft.setAircraftId(rs.getLong("aircraft_id"));
	        aircraft.setCode(rs.getString("code"));
	        aircraft.setModel(rs.getString("model"));
	        aircraft.setSeatCapacity(rs.getInt("seat_capacity"));
	        aircraft.setIsActive(rs.getBoolean("is_active"));
	        
	        if (rs.getTimestamp("created_at") != null) {
	            java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
	            java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
	            cal.setTime(timestamp);
	            
	            javax.xml.datatype.DatatypeFactory df = javax.xml.datatype.DatatypeFactory.newInstance();
	            aircraft.setCreatedAt(df.newXMLGregorianCalendar(cal));
	        }
	        
	        return aircraft;
	    } catch (javax.xml.datatype.DatatypeConfigurationException e) {
	        throw new RuntimeException("Error al convertir la fecha: " + e.getMessage(), e);
	    }
	};

	/**
	 * Lista todos los aircrafts
	 */
	public List<Aircraft> listAircrafts() {
	    String sql = "SELECT * FROM aircraft";
	    return template.query(sql, rowMapper);
	}

	/**
	 * Busca un aircraft por su ID
	 */
	public Aircraft findById(long aircraftId) {
	    String sql = "SELECT * FROM aircraft WHERE aircraft_id = ?";
	    List<Aircraft> lista = template.query(sql, rowMapper, aircraftId);
	    return lista.isEmpty() ? null : lista.get(0);
	}

	/**
	 * Busca un aircraft por su código
	 */
	public Aircraft findByCode(String code) {
	    String sql = "SELECT * FROM aircraft WHERE code = ?";
	    List<Aircraft> lista = template.query(sql, rowMapper, code);
	    return lista.isEmpty() ? null : lista.get(0);
	}

	/**
	 * Guarda un nuevo aircraft
	 */
	public int saveAircraft(Aircraft aircraft) {
	    String sql = "INSERT INTO aircraft (code, model, seat_capacity, is_active, created_at) VALUES (?, ?, ?, ?, ?)";
	    
	    java.sql.Timestamp timestamp = null;
	    if (aircraft.getCreatedAt() != null) {
	        timestamp = new java.sql.Timestamp(
	            aircraft.getCreatedAt().toGregorianCalendar().getTimeInMillis()
	        );
	    }
	    
	    return template.update(sql,
	        aircraft.getCode(),
	        aircraft.getModel(),
	        aircraft.getSeatCapacity(),
	        aircraft.isIsActive(),
	        timestamp
	    );
	}

	/**
	 * Actualiza un aircraft existente
	 */
	public int updateAircraft(Aircraft aircraft) {
	    String sql = "UPDATE aircraft SET code=?, model=?, seat_capacity=?, is_active=? WHERE aircraft_id=?";
	    
	    return template.update(sql,
	        aircraft.getCode(),
	        aircraft.getModel(),
	        aircraft.getSeatCapacity(),
	        aircraft.isIsActive(),
	        aircraft.getAircraftId()
	    );
	}

	/**
	 * Elimina un aircraft por su ID
	 */
	public int deleteAircraft(long aircraftId) {
	    String sql = "DELETE FROM aircraft WHERE aircraft_id=?";
	    return template.update(sql, aircraftId);
	}

	/**
	 * Lista aircrafts activos
	 */
	public List<Aircraft> listActiveAircrafts() {
	    String sql = "SELECT * FROM aircraft WHERE is_active = true";
	    return template.query(sql, rowMapper);
	}

	/**
	 * Lista aircrafts por capacidad de asientos
	 */
	public List<Aircraft> findByMinSeatCapacity(int minCapacity) {
	    String sql = "SELECT * FROM aircraft WHERE seat_capacity >= ?";
	    return template.query(sql, rowMapper, minCapacity);
	}
}
