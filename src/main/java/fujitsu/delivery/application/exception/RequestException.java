package fujitsu.delivery.application.exception;

import lombok.Getter;

@Getter
public class RequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public RequestException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.errorCode = errorCode;
    }

    public RequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}