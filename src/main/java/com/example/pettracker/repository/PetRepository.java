package com.example.pettracker.repository;

import com.example.pettracker.dto.response.OutOfZoneCountDto;
import com.example.pettracker.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {

    @Query("SELECT p.petType, p.trackerType, COUNT(p) " +
            "FROM Pet p " +
            "WHERE p.inZone = false " +
            "GROUP BY p.petType, p.trackerType")
    List<OutOfZoneCountDto> countOutOfZoneGrouped();
}
