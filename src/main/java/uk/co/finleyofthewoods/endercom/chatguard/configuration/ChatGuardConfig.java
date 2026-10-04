package uk.co.finleyofthewoods.endercom.chatguard.configuration;

import org.jspecify.annotations.NonNull;

import java.util.Set;

/**
 * ChatGuard configuration class.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class ChatGuardConfig {
    private boolean enabled;
    private Set<String> blockedWords;

    public ChatGuardConfig() {
        this.enabled = true;
        this.blockedWords = Set.of();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @NonNull Set<String> getBlockedWords() {
        return blockedWords;
    }

    public void setBlockedWords(@NonNull Set<String> blockedWords) {
        this.blockedWords = blockedWords;
    }

    @Override
    public @NonNull String toString() {
        return "ChatGuardConfig{" +
                "enabled=" + enabled +
                ", blockedWordsCount=" + blockedWords.size() +
                '}';
    }
}
