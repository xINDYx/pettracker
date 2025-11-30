package com.example.pettracker.dto.response;

import com.example.pettracker.enums.TrackerType;

public record TrackerOutOfZoneStats(TrackerType trackerType, Long count) {
}
