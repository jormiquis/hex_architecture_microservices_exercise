package edu.uoc.epcsd.user.domain.service;


import edu.uoc.epcsd.user.application.rest.request.CreateDigitalSessionRequest;
import edu.uoc.epcsd.user.domain.DigitalSession;

import java.util.List;
import java.util.Optional;

public interface DigitalSessionService {

    List<DigitalSession> findAllDigitalSession();

    List<DigitalSession> findDigitalSession(Long userId);

	Optional<DigitalSession> findDigitalSessionById(Long id);

	Long addDigitalSession(DigitalSession digitalsession);

    Long updateDigitalSession(Long digitalSessionId, CreateDigitalSessionRequest updateDigitalSessionRequest);

    void dropDigitalSession(Long id);

}
