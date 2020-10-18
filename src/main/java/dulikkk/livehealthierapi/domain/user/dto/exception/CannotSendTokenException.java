package dulikkk.livehealthierapi.domain.user.dto.exception;

public class CannotSendTokenException extends RuntimeException {

    public CannotSendTokenException(String msg) {
        super(msg);
    }
}
