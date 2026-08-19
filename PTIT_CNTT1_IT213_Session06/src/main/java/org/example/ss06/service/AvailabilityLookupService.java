package org.example.ss06.service;

import lombok.RequiredArgsConstructor;
import org.example.ss06.model.entity.Appointment;
import org.example.ss06.model.utils.AppointmentStatus;
import org.example.ss06.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityLookupService {
    private final AppointmentRepository appointmentRepository;

    private static final LocalTime OPEN_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(17, 0);
    private static final LocalTime LUNCH_START = LocalTime.of(12, 0);
    private static final LocalTime LUNCH_END = LocalTime.of(13, 0);
    private static final int SLOT_MINUTES = 30;

    public List<LocalTime> findAvailableSlots(Long doctorId, LocalDate date) {
        if (doctorId == null || date == null) {
            return List.of();
        }

        List<Appointment> bookedAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);

        Set<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        return generateAllSlots().stream()
                .filter(slot -> !bookedTimes.contains(slot))
                .collect(Collectors.toList());
    }

    private List<LocalTime> generateAllSlots() {
        List<LocalTime> slots = new java.util.ArrayList<>();
        LocalTime current = OPEN_TIME;
        while (current.isBefore(CLOSE_TIME)) {
            boolean isLunchBreak = !current.isBefore(LUNCH_START) && current.isBefore(LUNCH_END);
            if (!isLunchBreak) {
                slots.add(current);
            }
            current = current.plusMinutes(SLOT_MINUTES);
        }
        return slots;
    }
}