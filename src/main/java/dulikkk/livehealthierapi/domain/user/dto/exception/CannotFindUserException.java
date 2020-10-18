package dulikkk.livehealthierapi.domain.user.dto.exception;

public class CannotFindUserException extends RuntimeException {

    public CannotFindUserException(String message) {
        super(message);
    }
}
