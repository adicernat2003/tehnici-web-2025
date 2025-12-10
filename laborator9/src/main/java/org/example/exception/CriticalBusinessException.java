package org.example.exception;

public class CriticalBusinessException extends RuntimeException {
    public CriticalBusinessException(String message) {
        super(message);
    }
}
