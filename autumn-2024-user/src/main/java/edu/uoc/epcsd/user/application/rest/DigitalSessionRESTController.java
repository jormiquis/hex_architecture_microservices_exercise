package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.application.rest.request.CreateDigitalSessionRequest;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.service.DigitalSessionService;
import edu.uoc.epcsd.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/digital")
public class DigitalSessionRESTController {

    private final DigitalSessionService digitalSessionService;
    private final UserService userService;

    @GetMapping("/allDigital")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalSession> getAllDigitalSession() {
        log.trace("getAllDigitalSession");

        return digitalSessionService.findAllDigitalSession();
    }

    @GetMapping("/digitalByUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalSession> getDigitalSession(@PathVariable @NotNull Long userId) {
        log.trace("getDigitalSessionsByUser");

        return digitalSessionService.findDigitalSession(userId);
    }

    @PostMapping("/addDigital")
    public ResponseEntity<Long> addDigitalSession(@RequestBody @Valid CreateDigitalSessionRequest createDigitalSessionRequest) {
        log.trace("addDigitalSession");

		userService.findUserById(createDigitalSessionRequest.getUserId())
            .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified user " + createDigitalSessionRequest.getUserId() + " does not exist."));

        Long id = digitalSessionService.addDigitalSession(DigitalSession.builder()
            .id(createDigitalSessionRequest.getId())
            .description(createDigitalSessionRequest.getDescription())
            .location(createDigitalSessionRequest.getLocation())
            .link(createDigitalSessionRequest.getLink())
            .userId(createDigitalSessionRequest.getUserId())
            .build()
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toUri();

        return  ResponseEntity.created(uri).body(id);

    }

    @PostMapping("/updateDigital/{digitalSessionId}")
    public ResponseEntity<Boolean> updateDigitalSession(@PathVariable @NotNull Long digitalSessionId, @RequestBody @Valid CreateDigitalSessionRequest updateDigitalSessionRequest) {
        log.trace("updateDigitalSession");
        try {
            DigitalSession digitalSession = this.digitalSessionService.findDigitalSessionById(digitalSessionId)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified id " + digitalSessionId + " does not exist."));

            digitalSessionService.updateDigitalSession(
                digitalSession.getId(),
                updateDigitalSessionRequest
            );

            return ResponseEntity.ok(true);

         } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digitalSessionId " + digitalSessionId + " does not exist.", e);
         } catch (RestClientException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digitalSessionId " + digitalSessionId + " does not exist.", e);
         }
    }

    @DeleteMapping("/dropDigital/{digitalSessionId}")
    public ResponseEntity<Boolean> dropDigitalSession(@PathVariable @NotNull Long digitalSessionId) {
        log.trace("dropDigitalSession");
    try {
       DigitalSession digitalSession = this.digitalSessionService.findDigitalSessionById(digitalSessionId)
       .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified id " + digitalSessionId + " does not exist."));

       digitalSessionService.dropDigitalSession(digitalSession.getId());

       return ResponseEntity.ok(true);

    } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digitalSessionId " + digitalSessionId + " does not exist.", e);
    } catch (RestClientException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified digitalSessionId " + digitalSessionId + " does not exist.", e);
    }
  }
}
