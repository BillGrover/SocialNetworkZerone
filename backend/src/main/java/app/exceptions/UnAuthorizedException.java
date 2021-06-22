package app.exceptions;

import app.model.enums.ErrorType;

public class UnAuthorizedException extends AppException {

    public UnAuthorizedException(String description) {
        super(ErrorType.UNAUTHORIZED, description);
    }

}
