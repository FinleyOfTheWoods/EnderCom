package uk.co.finleyofthewoods.endercom.chatrelay.configuration;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Configuration class for ChatRelayService.
 *
 * @since 1.0
 * @author FinleyOfTheWoods
 */
public class ChatRelayConfig {
    private static ChatRelayConfig INSTANCE;
    private boolean enabled;
    private long chatChannelId;
    private long modChannelId;

    public ChatRelayConfig() {
        this.enabled = false;
        this.chatChannelId = 0;
        this.modChannelId = 0;
    }

    public static @NonNull ChatRelayConfig get() {
        return INSTANCE;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getChatChannelId() {
        return chatChannelId;
    }

    public void setChatChannelId(long chatChannelId) {
        this.chatChannelId = chatChannelId;
    }

    public long getModChannelId() {
        return modChannelId;
    }

    public void setModChannelId(long modChannelId) {
        this.modChannelId = modChannelId;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatRelayConfig that = (ChatRelayConfig) o;
        return enabled == that.enabled
                && chatChannelId == that.chatChannelId
                && modChannelId == that.modChannelId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, chatChannelId, modChannelId);
    }

    @Override
    public @NonNull String toString() {
        return "ChatRelayConfig{" +
                "enabled=" + enabled +
                ", chatChannelId=" + chatChannelId +
                ", modChannelId=" + modChannelId +
                '}';
    }
}
