package com.example.pettracker.dto.response;

import java.util.List;

public record OutOfZoneResponse(List<PetOutOfZoneStats> stats) {
}
