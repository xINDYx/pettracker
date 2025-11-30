package com.example.pettracker.exception;

import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;

public class InvalidTrackerTypeException extends RuntimeException {
    public InvalidTrackerTypeException(TrackerType trackerType, PetType petType) {
        super("TrackerType " + trackerType + " not suitable for PetType " + petType);
    }
}
