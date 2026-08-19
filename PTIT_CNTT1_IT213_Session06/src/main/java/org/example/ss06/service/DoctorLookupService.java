package org.example.ss06.service;

import lombok.RequiredArgsConstructor;
import org.example.ss06.model.entity.Doctor;
import org.example.ss06.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorLookupService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> findBySpecialty(String specialty) {
        if (specialty == null || specialty.isBlank()) {
            return List.of();
        }
        return doctorRepository.findBySpecialtyContainingIgnoreCaseAndActiveTrue(specialty.trim());
    }
}