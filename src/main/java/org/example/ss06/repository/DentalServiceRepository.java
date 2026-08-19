package org.example.ss06.repository;

import org.example.ss06.model.entity.DentalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentalServiceRepository extends JpaRepository<DentalService, Long> {
}
