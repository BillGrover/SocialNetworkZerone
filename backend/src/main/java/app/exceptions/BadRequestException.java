package app.exceptions;

import app.model.enums.ErrorType;

public class BadRequestException extends AppException {

    public BadRequestException(String description) {
        super(ErrorType.INVALID_REQUEST, description);
    }

}
