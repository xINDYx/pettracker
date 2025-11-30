package com.example.pettracker.controller;

import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.dto.response.PetResponse;
import com.example.pettracker.model.Pet;
import com.example.pettracker.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/out-of-zone")
    public OutOfZoneResponse getOutOfZoneCount() {
        return petService.getOutOfZoneCount();
    }

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@RequestBody PetCreateRequest petCreateRequest) {
        Pet createdPet = petService.createPet(petCreateRequest);
        return ResponseEntity.ok(PetResponse.from(createdPet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> updatePetStatus(
            @PathVariable UUID id,
            @RequestBody PetUpdateRequest petUpdateRequest
    ) {
        Pet updatedPet = petService.updatePetStatus(id, petUpdateRequest);
        return ResponseEntity.ok(PetResponse.from(updatedPet));
    }
}
