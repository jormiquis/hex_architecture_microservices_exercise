package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.application.rest.request.CreateDigitalSessionRequest;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class DigitalSessionServiceImpl implements DigitalSessionService {

    private final DigitalSessionRepository digitalSessionRepository;

    public List<DigitalSession> findAllDigitalSession() {
        return digitalSessionRepository.findAllDigitalSession();
    }

    @Override
	public List<DigitalSession> findDigitalSession(Long userId) {
		return digitalSessionRepository.findDigitalSession(userId);
	}

    @Override
	public Optional<DigitalSession> findDigitalSessionById(Long id) {
        return digitalSessionRepository.findDigitalSessionById(id);
	}

	@Override
	public Long addDigitalSession(DigitalSession digitalSession) {
        return digitalSessionRepository.addDigitalSession(digitalSession);
	}

	@Override
	public Long updateDigitalSession(Long digitalSessionId, CreateDigitalSessionRequest updateDigitalSessionRequest) {

		DigitalSession toBeUpdated = this.findDigitalSessionById(digitalSessionId)
		.orElseThrow(()->new EntityNotFoundException("The digital session " + digitalSessionId + " does not exist"));

		/* As read on pract Description :
		 * "Modify the information of a digital session (updateDigitalSession()). Using digitalSessionId,
		 * you can modify its description, location, or link"
		 */
		toBeUpdated.setDescription(updateDigitalSessionRequest.getDescription());
		toBeUpdated.setLink(updateDigitalSessionRequest.getLink());
		toBeUpdated.setLocation(updateDigitalSessionRequest.getLocation());

		return this.digitalSessionRepository.updateDigitalSession(toBeUpdated);
	}

	@Override
	public void dropDigitalSession(Long id) {
       DigitalSession toBeDropped = this.findDigitalSessionById(id)
		   .orElseThrow(()->new EntityNotFoundException("The digital session " + id + " does not exist"));
		this.digitalSessionRepository.dropDigitalSession(toBeDropped);
	}

}
