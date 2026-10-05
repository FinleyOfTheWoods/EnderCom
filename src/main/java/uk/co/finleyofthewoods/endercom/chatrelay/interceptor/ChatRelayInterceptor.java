package uk.co.finleyofthewoods.endercom.chatrelay.interceptor;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;
import uk.co.finleyofthewoods.endercom.chatrelay.service.ChatRelayService;

/**
 * Interceptor for handling chat relay functionality.
 *
 * @since 1.0
 * @author FinleyOfTheWoods
 */
public class ChatRelayInterceptor {
    private static final ChatRelayService relayService = new ChatRelayService();

    /**
     * Intercept a chat message and relay it to another message platform.
     * @param message {@link PlayerChatMessage} sent by the player
     * @param player {@link ServerPlayer} sending the message
     * @param ignored {@link ChatType.Bound}
     */
    public static void intercept(
            @NonNull PlayerChatMessage message,
            @NonNull ServerPlayer player,
            ChatType.Bound ignored
            ) {
        if (!relayService.isEnabled()) return;
        if (message.isSystem()) return;

    }
}
