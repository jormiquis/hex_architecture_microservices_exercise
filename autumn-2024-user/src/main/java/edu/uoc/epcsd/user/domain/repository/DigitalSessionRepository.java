package edu.uoc.epcsd.user.domain.repository;

import edu.uoc.epcsd.user.domain.DigitalSession;

import java.util.List;
import java.util.Optional;

public interface DigitalSessionRepository {

    List<DigitalSession> findDigitalSession(Long userId);

    Long addDigitalSession(DigitalSession digitalSession);
    
    Long updateDigitalSession(DigitalSession digitalSession);
	
    void dropDigitalSession(DigitalSession digitalSession);

	List<DigitalSession> findAllDigitalSession();

	Optional<DigitalSession> findDigitalSessionById(Long id);
	
}
