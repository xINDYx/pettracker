package com.example.pettracker.service;

import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.model.Pet;

import java.util.UUID;

public interface PetService {

    OutOfZoneResponse getOutOfZoneCount();

    Pet createPet(PetCreateRequest petCreateRequest);

    Pet updatePetStatus(UUID id, PetUpdateRequest petUpdateRequest);
}
