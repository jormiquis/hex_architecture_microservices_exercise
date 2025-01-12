package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DigitalSessionRepositoryImpl implements DigitalSessionRepository {

    private final SpringDataDigitalSessionRepository jpaDigitalSessionRepository;

    private final SpringDataUserRepository jpaUserRepository;

    @Override
    public List<DigitalSession> findAllDigitalSession() {
        return jpaDigitalSessionRepository.findAll().stream().map(DigitalSessionEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<DigitalSession> findDigitalSessionById(Long id) {
        return jpaDigitalSessionRepository.findDigitalSessionById(id).map(DigitalSessionEntity::toDomain);
    }

    @Override
    public List<DigitalSession> findDigitalSession(Long userId) {
        return jpaDigitalSessionRepository.findDigitalSession(userId).stream().map(DigitalSessionEntity::toDomain).toList();
    }

    @Override
    public Long addDigitalSession(DigitalSession digitalSession) {
        Optional<DigitalSessionEntity> digitalSessionEntity = jpaDigitalSessionRepository.findDigitalSessionById(digitalSession.getId());
        if (digitalSessionEntity.isEmpty()) {
            UserEntity user = this.jpaUserRepository.findById(digitalSession.getUserId()).get();

            DigitalSessionEntity toBeSaved = DigitalSessionEntity.fromDomain(digitalSession,user);
            return jpaDigitalSessionRepository.save(toBeSaved).getId();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"digitalSession " + digitalSession.getId() + " already exists");

        }
    }

	@Override
	public Long updateDigitalSession(DigitalSession digitalSession) {

		DigitalSessionEntity digitalSessionEntity = jpaDigitalSessionRepository.findDigitalSessionById(digitalSession.getId())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"digitalSession " + digitalSession.getId() + " does not exist"));

        return jpaDigitalSessionRepository.save(digitalSessionEntity).getId();
	}

	@Override
	public void dropDigitalSession(DigitalSession digitalSession) {
         jpaDigitalSessionRepository.deleteById(digitalSession.getId());
	}


}
