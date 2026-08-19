package org.example.ss06.tool;

import java.util.List;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DentalToolConfiguration {

    /**
     * Collects every C/D tool bean into one provider. Thành viên A can pass
     * this provider to {@code defaultToolCallbacks(...)} when creating the
     * shared ChatClient; the conversation endpoint also uses it directly.
     */
    @Bean
    ToolCallbackProvider dentalToolCallbackProvider(List<DentalChatTool> toolBeans) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(toolBeans.toArray())
                .build();
    }
}
