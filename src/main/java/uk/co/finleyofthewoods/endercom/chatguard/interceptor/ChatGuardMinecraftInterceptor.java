package uk.co.finleyofthewoods.endercom.chatguard.interceptor;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;
import uk.co.finleyofthewoods.endercom.chatguard.service.ChatGuardFilterService;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * Interceptor for chat messages sent by players from the Minecraft Server Chat.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class ChatGuardMinecraftInterceptor {
    private static final ChatGuardFilterService filterService = ChatGuardFilterService.get();

    /**
     * Intercept a chat message and check if it should be allowed or rejected.
     * @param message {@link PlayerChatMessage} sent by the player
     * @param player {@link ServerPlayer} sending the message
     * @param ignored {@link ChatType.Bound}
     * @return {@code true} if the message should be allowed, {@code false} otherwise
     */
    public static boolean intercept(
            @NonNull PlayerChatMessage message,
            @NonNull ServerPlayer player,
            ChatType.Bound ignored
    ) {
        log.debug("Intercepting chat message from: {}", player.getPlainTextName());
        String body = message.signedBody().content();
        return filterService.filter(body);
    }
}
