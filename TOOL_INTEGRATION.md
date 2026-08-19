# Quy ước tích hợp @Tool cho Thành viên C và D

Base đã cung cấp `DentalToolConfiguration`, tự gom các bean implement
`DentalChatTool` thành `ToolCallbackProvider`. Thành viên C và D chỉ cần:

1. Tạo class Spring bean (`@Component`).
2. Implement `DentalChatTool`.
3. Annotate từng hàm cho LLM gọi bằng Spring AI `@Tool`, với `name` và
   `description` rõ ràng.

Ví dụ:

```java
@Component
public class DoctorLookupTools implements DentalChatTool {

    @Tool(
        name = "findDoctorsBySpecialty",
        description = "Tìm các bác sĩ nha khoa theo chuyên khoa được khách hàng yêu cầu")
    public List<Doctor> findDoctorsBySpecialty(String specialty) {
        // Thành viên C triển khai nghiệp vụ tra cứu ở đây.
    }
}
```

Tool giao dịch của Thành viên D dùng cùng quy ước:

```java
@Component
public class AppointmentTools implements DentalChatTool {

    @Tool(
        name = "bookAppointment",
        description = "Đặt lịch khám khi đã có đủ khách hàng, bác sĩ và thời gian")
    public Appointment bookAppointment(String customerId, String doctorId, String startAt) {
        // Thành viên D triển khai kiểm tra trùng giờ và đặt lịch ở đây.
    }
}
```

## Cách endpoint sử dụng tool và memory

Sau khi Thành viên A cung cấp một bean `ChatClient`, gửi lượt chat bằng:

```http
POST /api/chat/sessions/{conversationId}/messages
Content-Type: application/json

{"message":"Tôi muốn đặt lịch khám"}
```

`ChatConversationService` tự truyền `ChatMemory.CONVERSATION_ID` vào
`MessageChatMemoryAdvisor` và đăng ký toàn bộ `@Tool` của C/D cho lượt gọi.
Vì vậy các lượt tiếp theo chỉ cần dùng lại cùng `conversationId`.

## Điểm nối với ChatClient của Thành viên A

Nếu A muốn các tool có sẵn cho những nơi gọi `ChatClient` khác ngoài endpoint,
có thể thêm provider vào builder:

```java
@Bean
ChatClient chatClient(
        ChatClient.Builder builder,
        ToolCallbackProvider dentalToolCallbackProvider) {
    return builder
            .defaultSystem("...")
            .defaultTools(dentalToolCallbackProvider)
            .build();
}
```

Không tạo thêm `ToolCallbackProvider` ở C/D; base đã cung cấp một provider dùng
chung để tránh đăng ký trùng tool.
