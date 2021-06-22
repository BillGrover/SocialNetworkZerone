package app.exceptions;

import app.model.enums.ErrorType;

public class ServiceUnavailableException extends AppException {

    public ServiceUnavailableException (String description) {
        super(ErrorType.SERVICE_UNAVAILABLE, description);
    }

}