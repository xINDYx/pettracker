package com.example.pettracker.mapper;

import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.dto.response.PetOutOfZoneStats;
import com.example.pettracker.dto.response.TrackerOutOfZoneStats;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;

import java.util.List;
import java.util.Map;

public interface OutOfZoneMapper {

    static OutOfZoneResponse toResponse(Map<PetType, Map<TrackerType, Long>> map) {
        List<PetOutOfZoneStats> petStats = map.entrySet().stream()
                .map(e -> {
                    PetType petType = e.getKey();
                    List<TrackerOutOfZoneStats> trackers = e.getValue().entrySet().stream()
                            .map(te -> new TrackerOutOfZoneStats(te.getKey(), te.getValue()))
                            .toList();
                    return new PetOutOfZoneStats(petType, trackers);
                })
                .toList();

        return new OutOfZoneResponse(petStats);
    }
}
