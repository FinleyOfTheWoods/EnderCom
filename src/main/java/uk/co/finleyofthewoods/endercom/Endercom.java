package uk.co.finleyofthewoods.endercom;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import uk.co.finleyofthewoods.endercom.chatguard.commands.ChatGuardCommand;
import uk.co.finleyofthewoods.endercom.chatguard.interceptor.ChatGuardMinecraftInterceptor;
import uk.co.finleyofthewoods.endercom.config.EnderComConfig;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * Main entry point for the Endercom mod.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class Endercom implements ModInitializer {

    @Override
    public void onInitialize() {
        log.debug("DEBUG LOGS ENABLED");
        log.info("initialising EnderCom");

        EnderComConfig.load();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            log.info("EnderCom shutting down");
            EnderComConfig.save();
        });

        log.info("ChatGuard enabled, registering commands");
        CommandRegistrationCallback.EVENT.register(ChatGuardCommand::register);


        // register the Minecraft Chat Interceptor
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatGuardMinecraftInterceptor::intercept);
    }
}
