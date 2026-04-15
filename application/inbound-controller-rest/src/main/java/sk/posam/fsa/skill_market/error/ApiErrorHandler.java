package sk.posam.fsa.skill_market.error;

import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sk.posam.fsa.skill_market.domain.project.ProjectAlreadyExistsException;
import sk.posam.fsa.skill_market.rest.dto.ErrorResponse;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProjectAlreadyExists(ProjectAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("PROJECT_ALREADY_EXISTS", exception.getMessage()));
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
