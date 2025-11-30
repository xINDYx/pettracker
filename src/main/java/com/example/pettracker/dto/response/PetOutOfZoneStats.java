package com.example.pettracker.dto.response;

import com.example.pettracker.enums.PetType;

import java.util.List;

public record PetOutOfZoneStats(PetType petType, List<TrackerOutOfZoneStats> trackers) {
}
