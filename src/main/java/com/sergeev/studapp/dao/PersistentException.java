package com.sergeev.studapp.dao;

import com.sergeev.studapp.service.ApplicationException;

public class PersistentException extends ApplicationException {

    public PersistentException() {
    }

    public PersistentException(String message) {
        super(message);
    }

    public PersistentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistentException(Throwable cause) {
        super(cause);
    }

    public PersistentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}