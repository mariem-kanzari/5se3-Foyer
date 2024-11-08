package com.example.projetdevops.Exceptions;

public class UniversiteNotFoundException extends RuntimeException {

    // Constructeur avec un message
    public UniversiteNotFoundException(String message) {
        super(message);
    }

    // Constructeur avec un message et une cause
    public UniversiteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
