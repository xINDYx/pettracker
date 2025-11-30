package com.example.pettracker.repository;

import com.example.pettracker.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DogRepository extends JpaRepository<Dog, UUID> {
}
