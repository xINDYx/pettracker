package com.example.pettracker.service;

import com.example.pettracker.configuration.TestPetConfig;
import com.example.pettracker.dto.request.PetCreateRequest;
import com.example.pettracker.dto.request.PetUpdateRequest;
import com.example.pettracker.dto.response.OutOfZoneCountDto;
import com.example.pettracker.dto.response.OutOfZoneResponse;
import com.example.pettracker.dto.response.PetOutOfZoneStats;
import com.example.pettracker.enums.PetType;
import com.example.pettracker.enums.TrackerType;
import com.example.pettracker.exception.PetNotFoundException;
import com.example.pettracker.factory.PetFactory;
import com.example.pettracker.model.Cat;
import com.example.pettracker.model.Pet;
import com.example.pettracker.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private PetFactory petFactory;

    private final TestPetConfig.TestPetFactory factory = new TestPetConfig.TestPetFactory();

    @Test
    void testCreatePet_Success() {
        PetCreateRequest request = factory.catCreateRequest(true, false);

        Cat cat = factory.createCat(true, false);

        when(petFactory.create(request)).thenReturn(cat);
        when(petRepository.save(any(Pet.class))).thenReturn(cat);

        Pet result = petService.createPet(request);

        assertNotNull(result);
        assertEquals(PetType.CAT, result.getPetType());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void testUpdatePetStatus_Success() {
        UUID id = UUID.randomUUID();
        Cat cat = factory.createCat(true, false);

        when(petRepository.findById(id)).thenReturn(Optional.of(cat));
        when(petRepository.save(cat)).thenReturn(cat);

        PetUpdateRequest updateRequest = new PetUpdateRequest(false, true);
        Pet result = petService.updatePetStatus(id, updateRequest);

        assertNotNull(result);
        assertFalse(result.getInZone());
        verify(petRepository).save(cat);
    }

    @Test
    void testUpdatePetStatus_NotFound() {
        UUID id = UUID.randomUUID();
        when(petRepository.findById(id)).thenReturn(Optional.empty());

        PetUpdateRequest updateRequest = new PetUpdateRequest(false, true);

        assertThrows(PetNotFoundException.class, () ->
                petService.updatePetStatus(id, updateRequest));
    }

    @Test
    void testGetOutOfZoneCount() {
        OutOfZoneCountDto dto1 = new OutOfZoneCountDto(PetType.CAT, TrackerType.SMALL_CAT, 2L);
        OutOfZoneCountDto dto2 = new OutOfZoneCountDto(PetType.DOG, TrackerType.MEDIUM_DOG, 3L);

        when(petRepository.countOutOfZoneGrouped()).thenReturn(List.of(dto1, dto2));

        OutOfZoneResponse response = petService.getOutOfZoneCount();

        assertNotNull(response);
        assertEquals(2, response.stats().size());

        Map<PetType, PetOutOfZoneStats> map = response.stats().stream()
                .collect(Collectors.toMap(PetOutOfZoneStats::petType, s -> s));

        assertEquals(2L, map.get(PetType.CAT).trackers().get(0).count());
        assertEquals(3L, map.get(PetType.DOG).trackers().get(0).count());
    }
}
