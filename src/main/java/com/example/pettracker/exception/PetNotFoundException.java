package com.example.pettracker.exception;

import java.util.UUID;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(UUID id) {
        super("Pet with id " + id + " not found");
    }
}
