package org.example.ss06.tools;

import lombok.RequiredArgsConstructor;
import org.example.ss06.model.entity.DentalService;
import org.example.ss06.model.entity.Doctor;
import org.example.ss06.service.AvailabilityLookupService;
import org.example.ss06.service.DoctorLookupService;
import org.example.ss06.service.ServiceLookupService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Nhóm @Tool tra cứu (read-only) cho chatbot SmileCare.
 * KHÔNG chứa logic ghi/sửa dữ liệu — đó là phần việc của tool giao dịch (Thành viên D).
 *
 * Class này sẽ được A đăng ký vào ChatClient.builder().defaultTools(...)
 * (hoặc tự động nếu A cấu hình quét theo @Component + @Tool).
 */
@Component
@RequiredArgsConstructor
public class LookupTools {

    private final DoctorLookupService doctorLookupService;
    private final ServiceLookupService serviceLookupService;
    private final AvailabilityLookupService availabilityLookupService;

    // ---- DTO trả về cho LLM (không trả thẳng Entity để tránh lộ field thừa: active, v.v.) ----

    public record DoctorInfo(
            Long id,
            String name,
            String specialty,
            String phone,
            String description
    ) {}

    public record ServiceInfo(
            String name,
            String description,
            BigDecimal price,
            Integer durationMinutes
    ) {}

    // ---- Tool 1: tìm bác sĩ theo chuyên khoa ----

    @Tool(description = "Tìm danh sách bác sĩ nha khoa theo chuyên khoa. Dùng khi khách hỏi về " +
            "bác sĩ phù hợp với vấn đề răng miệng của họ, hoặc hỏi phòng khám có bác sĩ chuyên " +
            "khoa nào đó không. Kết quả trả về bao gồm id của bác sĩ - cần giữ lại id này để " +
            "dùng cho việc tra lịch trống hoặc đặt lịch sau đó.")
    public List<DoctorInfo> findDoctorsBySpecialty(
            @ToolParam(description = "Tên chuyên khoa nha khoa, ví dụ: Nha chu, Chỉnh nha, " +
                    "Phẫu thuật miệng, Nha khoa tổng quát")
            String specialty) {

        List<Doctor> doctors = doctorLookupService.findBySpecialty(specialty);
        return doctors.stream()
                .map(d -> new DoctorInfo(d.getId(), d.getName(), d.getSpecialty(), d.getPhone(), d.getDescription()))
                .collect(Collectors.toList());
    }

    // ---- Tool 2: tra dịch vụ & bảng giá ----

    @Tool(description = "Tra cứu dịch vụ nha khoa và bảng giá theo tên dịch vụ. Dùng khi khách " +
            "hỏi về giá, thời gian thực hiện, hoặc phòng khám có cung cấp dịch vụ nào đó không.")
    public List<ServiceInfo> findServicesByName(
            @ToolParam(description = "Tên hoặc từ khóa dịch vụ nha khoa, ví dụ: trám răng, " +
                    "tẩy trắng, nhổ răng khôn, niềng răng")
            String keyword) {

        List<DentalService> services = serviceLookupService.findByName(keyword);
        return services.stream()
                .map(s -> new ServiceInfo(s.getName(), s.getDescription(), s.getPrice(), s.getDurationMinutes()))
                .collect(Collectors.toList());
    }

    // ---- Tool 3: tra lịch trống ----

    @Tool(description = "Tra cứu các khung giờ còn trống của một bác sĩ trong một ngày cụ thể. " +
            "CHỈ gọi tool này khi đã biết rõ doctorId (lấy từ kết quả tool tìm bác sĩ) và ngày " +
            "cụ thể mà khách muốn khám. Nếu khách chưa cho biết muốn khám với bác sĩ nào, hãy " +
            "gọi tool tìm bác sĩ trước hoặc hỏi lại khách.")
    public List<String> findAvailableSlots(
            @ToolParam(description = "ID của bác sĩ, lấy từ kết quả tool findDoctorsBySpecialty")
            Long doctorId,
            @ToolParam(description = "Ngày muốn khám, định dạng yyyy-MM-dd. Nếu khách nói ngày " +
                    "tương đối như 'ngày mai' hoặc 'thứ 5 tuần sau', hãy tự quy đổi thành ngày " +
                    "cụ thể dựa trên ngày hôm nay trước khi gọi tool này.")
            String date) {

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (Exception ex) {
            // Ngày sai định dạng: trả rỗng thay vì crash, để model tự nhận ra và hỏi lại khách
            return List.of();
        }

        List<LocalTime> slots = availabilityLookupService.findAvailableSlots(doctorId, parsedDate);
        return slots.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());
    }
}
