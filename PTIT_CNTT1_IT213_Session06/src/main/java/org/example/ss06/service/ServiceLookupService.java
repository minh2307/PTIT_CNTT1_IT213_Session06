package org.example.ss06.service;

import lombok.RequiredArgsConstructor;
import org.example.ss06.model.entity.DentalService;
import org.example.ss06.repository.DentalServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceLookupService {
    private final DentalServiceRepository dentalServiceRepository;

    public List<DentalService> findByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return dentalServiceRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword.trim());
    }
}