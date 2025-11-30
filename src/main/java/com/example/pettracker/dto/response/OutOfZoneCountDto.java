package com.example.pettracker.dto.response;

import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;

public record OutOfZoneCountDto(PetType petType, TrackerType trackerType, Long count) {}
