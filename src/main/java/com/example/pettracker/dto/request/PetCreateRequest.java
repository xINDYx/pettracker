package com.example.pettracker.dto.request;

import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;

public record PetCreateRequest(PetType petType, TrackerType trackerType, Integer ownerId, Boolean inZone,
                               Boolean lostTracker) {
}

