package uk.co.finleyofthewoods.endercom.chatguard.configuration;

import org.jspecify.annotations.NonNull;

import java.util.Set;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * ChatGuard configuration class.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class ChatGuardConfig {
    private static ChatGuardConfig INSTANCE;
    private boolean enabled;
    private Set<String> blockedWords;

    public ChatGuardConfig() {
        this.enabled = true;
        this.blockedWords = Set.of();
    }

    public static ChatGuardConfig get() {
        if (INSTANCE == null) {
            INSTANCE = new ChatGuardConfig();
        }
        return INSTANCE;
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

    public void addBlockedWord(String word) {
        if (this.blockedWords.contains(word)) {
            log.debug("Word {} already exists in ChatGuard filter", word);
            return;
        }
        log.debug("Adding {} to ChatGuard filter", word);
        this.blockedWords.add(word);
    }

    public void removeBlockedWord(String word) {
        if (!this.blockedWords.contains(word)) {
            log.debug("Word {} does not exist in ChatGuard filter", word);
            return;
        }
        log.debug("Removing {} from ChatGuard filter", word);
        this.blockedWords.remove(word);
    }

    @Override
    public @NonNull String toString() {
        return "ChatGuardConfig{" +
                "enabled=" + enabled +
                ", blockedWordsCount=" + blockedWords.size() +
                '}';
    }
}
