package org.example.ss06;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ChatEndToEndTest {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Test
    public void testMultiTurnConversation() {
        // Build ChatClient with memory for multi-turn conversation
        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();

        // Turn 1: Xin chào và hỏi về phòng khám
        String response1 = chatClient.prompt()
                .user("Xin chào, phòng khám có dịch vụ nào vậy?")
                .call()
                .content();
        System.out.println("User: Xin chào, phòng khám có dịch vụ nào vậy?");
        System.out.println("AI: " + response1);
        assertNotNull(response1);
        assertTrue(response1.length() > 0);

        // Turn 2: Hỏi cụ thể về một dịch vụ và bác sĩ (VD: Nhổ răng khôn)
        String response2 = chatClient.prompt()
                .user("Tôi muốn biết giá dịch vụ nhổ răng khôn và có bác sĩ nào chuyên nhổ răng không?")
                .call()
                .content();
        System.out.println("User: Tôi muốn biết giá dịch vụ nhổ răng khôn và có bác sĩ nào chuyên nhổ răng không?");
        System.out.println("AI: " + response2);
        assertNotNull(response2);

        // Turn 3: Đặt lịch khám
        String response3 = chatClient.prompt()
                .user("Tôi tên là Phạm Văn D, số điện thoại 0111222333. Hãy đặt lịch nhổ răng khôn cho tôi vào 14:00 ngày mai với bác sĩ Nguyễn Văn A nhé.")
                .call()
                .content();
        System.out.println("User: Tôi tên là Phạm Văn D, số điện thoại 0111222333. Hãy đặt lịch nhổ răng khôn cho tôi vào 14:00 ngày mai với bác sĩ Nguyễn Văn A nhé.");
        System.out.println("AI: " + response3);
        assertNotNull(response3);
        
        // Turn 4: Kiểm tra lại lịch khám đã đặt (Hỏi lại thông tin lịch hẹn để test memory)
        String response4 = chatClient.prompt()
                .user("Bạn có thể nhắc lại tôi vừa đặt lịch vào thời gian nào không?")
                .call()
                .content();
        System.out.println("User: Bạn có thể nhắc lại tôi vừa đặt lịch vào thời gian nào không?");
        System.out.println("AI: " + response4);
        assertNotNull(response4);
    }
}
