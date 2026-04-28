package sk.posam.fsa.skill_market.error;

import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sk.posam.fsa.skill_market.domain.offer.OfferCannotBeCancelledException;
import sk.posam.fsa.skill_market.domain.offer.OfferCannotBeDecidedException;
import sk.posam.fsa.skill_market.domain.offer.OfferNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFoundException;
import sk.posam.fsa.skill_market.domain.profile.UserProfileNotFreelancerException;
import sk.posam.fsa.skill_market.domain.project.ProjectAlreadyExistsException;
import sk.posam.fsa.skill_market.domain.project.ProjectNotAcceptingOffersException;
import sk.posam.fsa.skill_market.domain.project.ProjectCannotBeCancelledException;
import sk.posam.fsa.skill_market.domain.project.ProjectCannotBeEditedException;
import sk.posam.fsa.skill_market.domain.project.ProjectNotCompletedException;
import sk.posam.fsa.skill_market.domain.project.ProjectNotFoundException;
import sk.posam.fsa.skill_market.rest.dto.ErrorResponse;
import sk.posam.fsa.skill_market.security.ForbiddenOperationException;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProjectAlreadyExists(ProjectAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("PROJECT_ALREADY_EXISTS", exception.getMessage()));
    }

    @ExceptionHandler({
            ProjectNotFoundException.class,
            OfferNotFoundException.class,
            UserProfileNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error("RESOURCE_NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler({
            ProjectNotAcceptingOffersException.class,
            ProjectNotCompletedException.class,
            OfferCannotBeCancelledException.class,
            OfferCannotBeDecidedException.class,
            ProjectCannotBeCancelledException.class,
            ProjectCannotBeEditedException.class,
            UserProfileNotFreelancerException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("BUSINESS_RULE_VIOLATION", exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenOperationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error("FORBIDDEN_OPERATION", exception.getMessage()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(error("VALIDATION_ERROR", exception.getMessage()));
    }

    private ErrorResponse error(String code, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(OffsetDateTime.now());
        return response;
    }
}
