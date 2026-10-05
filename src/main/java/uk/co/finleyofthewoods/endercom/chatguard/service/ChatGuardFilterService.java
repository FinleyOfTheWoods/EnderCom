package uk.co.finleyofthewoods.endercom.chatguard.service;

import org.jspecify.annotations.NonNull;
import uk.co.finleyofthewoods.endercom.chatguard.configuration.ChatGuardConfig;
import uk.co.finleyofthewoods.endercom.chatguard.matcher.BlockedTermMatcher;
import uk.co.finleyofthewoods.endercom.config.EnderComConfig;
import java.util.*;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * Filter for Minecraft chat messages to check
 * for forbidden words and phrases.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class ChatGuardFilterService {
    private static ChatGuardFilterService INSTANCE;
    private static final ChatGuardConfig chatGuardConfig = EnderComConfig.get().getChatGuardConfig();
    private volatile BlockedTermMatcher matcher = new BlockedTermMatcher(chatGuardConfig.getBlockedWords());

    public ChatGuardFilterService() {
    }

    public static ChatGuardFilterService get() {
        if (INSTANCE == null) {
            INSTANCE = new ChatGuardFilterService();
        }
        return INSTANCE;
    }

    /**
     * Reload the blocked terms.
     */
    public void reload() {
        matcher = new BlockedTermMatcher(chatGuardConfig.getBlockedWords());
    }

    /**
     * Filter the messages based on regex filtering.
     * @param message sent by the player to be filtered
     * @return {@code true} if the message is allowed, {@code false} otherwise
     */
    public boolean filter(@NonNull String message) {
        if (!chatGuardConfig.isEnabled()) return true;
        Optional<String> match = matcher.findMatch(message);
        match.ifPresent(term -> log.warn("Blocked message, matched term: {}", term));
        return match.isEmpty();
    }

    public void addBlockedWord(String word) {
        chatGuardConfig.addBlockedWord(word);
    }

    public void removeBlockedWord(String word) {
        chatGuardConfig.removeBlockedWord(word);
    }
}
