package org.example.ss06.repository;

import org.example.ss06.model.entity.DentalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DentalServiceRepository extends JpaRepository<DentalService, Long> {
    List<DentalService> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}