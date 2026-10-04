package uk.co.finleyofthewoods.endercom.chatguard.interceptor;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;

import static uk.co.finleyofthewoods.endercom.logger.EndercomLogger.log;

/**
 * Interceptor for chat messages sent by players from the Minecraft Server Chat.
 */
public class MinecraftChatInterceptor {
    /**
     * Intercept a chat message and check if it should be allowed or rejected.
     * @param message {@link PlayerChatMessage} sent by the player
     * @param player {@link ServerPlayer} sending the message
     * @param bound {@link ChatType.Bound}
     * @return {@code true} if the message should be allowed, {@code false} otherwise
     */
    public static boolean intercept(
            @NonNull PlayerChatMessage message,
            @NonNull ServerPlayer player,
            ChatType.Bound bound
    ) {
        log.debug("Intercepting chat message from: {}", player.getPlainTextName());
        return true;
    }
}
