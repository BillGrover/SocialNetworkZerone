package app.model.enums;

public enum ErrorType {
    INVALID_REQUEST("invalid_request"),
    UNAUTHORIZED("unauthorized"),
    SERVICE_UNAVAILABLE("service_unavailable");

    java.lang.String text;

    ErrorType(java.lang.String text) {
        this.text = text;
    }

    public java.lang.String getText() {
        return text;
    }
}
