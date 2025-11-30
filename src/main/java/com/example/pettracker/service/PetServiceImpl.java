package com.example.pettracker.service;

import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.dto.response.OutOfZoneCountDto;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import com.example.pettracker.exception.PetNotFoundException;
import com.example.pettracker.factory.PetFactory;
import com.example.pettracker.mapper.OutOfZoneMapper;
import com.example.pettracker.model.Pet;
import com.example.pettracker.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetFactory petFactory;

    public PetServiceImpl(PetRepository petRepository, PetFactory petFactory) {
        this.petRepository = petRepository;
        this.petFactory = petFactory;
    }

    @Override
    public OutOfZoneResponse getOutOfZoneCount() {
        List<OutOfZoneCountDto> results = petRepository.countOutOfZoneGrouped();

        Map<PetType, Map<TrackerType, Long>> map = results.stream()
                .collect(Collectors.groupingBy(
                        OutOfZoneCountDto::petType,
                        Collectors.toMap(
                                OutOfZoneCountDto::trackerType,
                                OutOfZoneCountDto::count
                        )
                ));

        return OutOfZoneMapper.toResponse(map);
    }

    public Pet createPet(PetCreateRequest request) {
        Pet pet = petFactory.create(request);
        return petRepository.save(pet);
    }

    @Override
    public Pet updatePetStatus(UUID id, PetUpdateRequest petUpdateRequest) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        pet.updateStatus(petUpdateRequest);
        return petRepository.save(pet);
    }
}