package uk.co.finleyofthewoods.endercom.chatrelay.service;

import uk.co.finleyofthewoods.endercom.chatrelay.configuration.ChatRelayConfig;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * Service for handling chat relay functionality.
 *
 * @since 1.0
 * @author FinleyOfTheWoods
 */
public class ChatRelayService {
    private static final ChatRelayConfig config = ChatRelayConfig.get();

    public ChatRelayService() {}

    public boolean isEnabled() {
        log.debug("ChatRelay is: {}", config.isEnabled() ? "enabled" : "disabled");
        return config.isEnabled();
    }
}
