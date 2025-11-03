package com.ashish.library_management_system.exception;

public class BorrowerNotFoundException extends Exception{
    public BorrowerNotFoundException() {
    }

    public BorrowerNotFoundException(String message) {
        super(message);
    }

    public BorrowerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BorrowerNotFoundException(Throwable cause) {
        super(cause);
    }

    public BorrowerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
