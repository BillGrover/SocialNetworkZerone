package app.exceptions;

import app.model.enums.ErrorType;

public abstract class AppException extends Exception {

    private final ErrorType type;

    private final String description;

    protected AppException(ErrorType type, String description) {
        super(description);
        this.type = type;
        this.description = description;
    }


    public ErrorType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }


}
