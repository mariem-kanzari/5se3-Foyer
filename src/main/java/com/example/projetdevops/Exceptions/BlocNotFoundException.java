package com.example.projetdevops.Exceptions;
public class BlocNotFoundException extends RuntimeException {
    public BlocNotFoundException(long id) {
        super("Bloc not found with id: " + id);
    }
}