package org.example.ss06.service;

import org.example.ss06.model.entity.Appointment;
import org.example.ss06.model.utils.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AppointmentService {

    private final List<Appointment> appointments = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Đặt lịch khám mới.
     */
    public synchronized Appointment book(
            String customerName,
            String phone,
            Long doctorId,
            Long serviceId,
            LocalDate date,
            LocalTime time) {

        validateBookingData(
                customerName,
                phone,
                doctorId,
                serviceId,
                date,
                time
        );

        if (isSlotAlreadyBooked(doctorId, date, time)) {
            throw new IllegalStateException(
                    "Khung giờ này đã có người đặt. Vui lòng chọn khung giờ khác."
            );
        }

        Appointment appointment = new Appointment();

        appointment.setId(idGenerator.getAndIncrement());
        appointment.setCustomerName(customerName);
        appointment.setPhone(phone);
        appointment.setDoctorId(doctorId);
        appointment.setServiceId(serviceId);
        appointment.setAppointmentDate(date);
        appointment.setAppointmentTime(time);
        appointment.setStatus(AppointmentStatus.BOOKED);

        appointments.add(appointment);

        return appointment;
    }

    /**
     * Đổi lịch hẹn.
     */
    public synchronized Appointment reschedule(
            Long appointmentId,
            LocalDate newDate,
            LocalTime newTime) {

        Appointment appointment = findAppointment(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Lịch hẹn đã bị hủy nên không thể đổi lịch."
            );
        }

        if (isSlotAlreadyBooked(
                appointment.getDoctorId(),
                newDate,
                newTime,
                appointmentId)) {

            throw new IllegalStateException(
                    "Khung giờ mới đã có người đặt. Vui lòng chọn khung giờ khác."
            );
        }

        appointment.setAppointmentDate(newDate);
        appointment.setAppointmentTime(newTime);

        return appointment;
    }

    /**
     * Hủy lịch hẹn.
     */
    public synchronized Appointment cancel(Long appointmentId) {

        Appointment appointment = findAppointment(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Lịch hẹn này đã được hủy trước đó."
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointment;
    }

    /**
     * Kiểm tra một bác sĩ đã có lịch ở thời gian này chưa.
     */
    private boolean isSlotAlreadyBooked(
            Long doctorId,
            LocalDate date,
            LocalTime time) {

        return appointments.stream()
                .anyMatch(appointment ->
                        appointment.getDoctorId().equals(doctorId)
                                && appointment.getAppointmentDate().equals(date)
                                && appointment.getAppointmentTime().equals(time)
                                && appointment.getStatus() == AppointmentStatus.BOOKED
                );
    }

    /**
     * Kiểm tra khi đổi lịch.
     * Bỏ qua chính appointment đang được đổi.
     */
    private boolean isSlotAlreadyBooked(
            Long doctorId,
            LocalDate date,
            LocalTime time,
            Long ignoredAppointmentId) {

        return appointments.stream()
                .anyMatch(appointment ->
                        !appointment.getId().equals(ignoredAppointmentId)
                                && appointment.getDoctorId().equals(doctorId)
                                && appointment.getAppointmentDate().equals(date)
                                && appointment.getAppointmentTime().equals(time)
                                && appointment.getStatus() == AppointmentStatus.BOOKED
                );
    }

    private Appointment findAppointment(Long appointmentId) {

        return appointments.stream()
                .filter(appointment ->
                        appointment.getId().equals(appointmentId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy lịch hẹn có ID: "
                                        + appointmentId
                        )
                );
    }

    private void validateBookingData(
            String customerName,
            String phone,
            Long doctorId,
            Long serviceId,
            LocalDate date,
            LocalTime time) {

        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException(
                    "Họ tên khách hàng không được để trống."
            );
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Số điện thoại không được để trống."
            );
        }

        if (doctorId == null) {
            throw new IllegalArgumentException(
                    "Chưa xác định bác sĩ."
            );
        }

        if (serviceId == null) {
            throw new IllegalArgumentException(
                    "Chưa xác định dịch vụ."
            );
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "Chưa xác định ngày khám."
            );
        }

        if (time == null) {
            throw new IllegalArgumentException(
                    "Chưa xác định giờ khám."
            );
        }
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }
}