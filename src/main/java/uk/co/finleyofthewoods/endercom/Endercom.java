package uk.co.finleyofthewoods.endercom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import uk.co.finleyofthewoods.endercom.chatguard.interceptor.MinecraftChatInterceptor;

import static uk.co.finleyofthewoods.endercom.logger.EndercomLogger.log;

/**
 * Main entry point for the Endercom mod.
 */
public class Endercom implements ModInitializer {


    @Override
    public void onInitialize() {
        log.debug("DEBUG LOGS ENABLED");
        log.info("initialising EnderCom");
        // register the Minecraft Chat Interceptor
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(MinecraftChatInterceptor::intercept);
    }
}
