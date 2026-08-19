package org.example.ss06.tool;

/**
 * Marker interface for tool groups exposed to the dental assistant.
 *
 * <p>Members who implement lookup or transaction tools should make their
 * Spring bean implement this interface and annotate the callable methods with
 * Spring AI's {@code @Tool}. The shared chat endpoint will then discover the
 * bean automatically.</p>
 */
public interface DentalChatTool {
}
