package org.example.ss06.tool;

import org.example.ss06.model.entity.Appointment;
import org.example.ss06.service.AppointmentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class AppointmentTools {

    private final AppointmentService appointmentService;

    public AppointmentTools(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Tool(
            name = "bookAppointment",
            description = """
            Đặt lịch khám mới tại phòng khám nha khoa SmileCare.

            Chỉ sử dụng tool này khi đã có đầy đủ:
            - họ tên khách hàng
            - số điện thoại
            - ID bác sĩ
            - ID dịch vụ
            - ngày khám
            - giờ khám

            Nếu khách hàng chưa cung cấp đủ thông tin thì KHÔNG được
            gọi tool này. Hãy hỏi khách hàng bổ sung thông tin còn thiếu.

            Không được tự suy đoán hoặc tự tạo ra thông tin còn thiếu.

            Tool sẽ kiểm tra xem bác sĩ có bị trùng lịch hay không.
            Nếu khung giờ đã được đặt, tool sẽ trả về lỗi và khách hàng
            cần chọn khung giờ khác.
            """
    )
    public Appointment bookAppointment(
            String customerName,
            String phone,
            Long doctorId,
            Long serviceId,
            LocalDate date,
            LocalTime time) {

        return appointmentService.book(
                customerName,
                phone,
                doctorId,
                serviceId,
                date,
                time
        );
    }

    @Tool(
            name = "rescheduleAppointment",
            description = """
            Đổi ngày hoặc giờ của một lịch hẹn đã tồn tại.

            Sử dụng khi khách hàng muốn thay đổi lịch khám đã đặt.

            Cần có:
            - ID lịch hẹn
            - ngày mới
            - giờ mới

            Không được đổi lịch đã bị hủy.

            Tool sẽ kiểm tra khung giờ mới có bị trùng với lịch hẹn
            khác của cùng bác sĩ hay không.
            """
    )
    public Appointment rescheduleAppointment(
            Long appointmentId,
            LocalDate newDate,
            LocalTime newTime) {

        return appointmentService.reschedule(
                appointmentId,
                newDate,
                newTime
        );
    }

    @Tool(
            name = "cancelAppointment",
            description = """
            Hủy một lịch hẹn khám đã đặt tại SmileCare.

            Sử dụng khi khách hàng yêu cầu hủy lịch khám.

            Cần có ID lịch hẹn chính xác.

            Không được tự tạo ID lịch hẹn.
            Nếu không xác định được lịch hẹn cần hủy,
            hãy yêu cầu khách hàng cung cấp thêm thông tin.
            """
    )
    public Appointment cancelAppointment(Long appointmentId) {

        return appointmentService.cancel(appointmentId);
    }
}