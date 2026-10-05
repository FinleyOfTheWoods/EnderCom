package uk.co.finleyofthewoods.endercom.chatguard.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jspecify.annotations.NonNull;
import uk.co.finleyofthewoods.endercom.chatguard.configuration.ChatGuardConfig;
import uk.co.finleyofthewoods.endercom.chatguard.service.ChatGuardFilterService;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * ChatGuard command registration.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class ChatGuardCommand {
    private static final ChatGuardConfig config = ChatGuardConfig.get();
    private static final ChatGuardFilterService filterService = ChatGuardFilterService.get();

    public ChatGuardCommand() {}

    /**
     * register commands for ChatGuard to allow admins to add and remove words from the filter list.
     * @param dispatcher {@link CommandDispatcher<CommandSourceStack>}
     * @param ignored {@link CommandBuildContext}
     * @param ignored2 {@link Commands.CommandSelection}
     */
    public static void register(
            @NonNull CommandDispatcher<CommandSourceStack> dispatcher,
            @NonNull CommandBuildContext ignored,
            Commands.CommandSelection ignored2
    ) {
        dispatcher.register(Commands.literal("chatguard")
                        .requires(Permissions.require("chatguard.admin"))
                .then(Commands.literal("add")
                        .then(Commands.argument("word", StringArgumentType.string())
                        .executes(ChatGuardCommand::executeAddWord)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("word", StringArgumentType.string())
                                .suggests(ChatGuardCommand::suggestWordsList)
                                .executes(ChatGuardCommand::executeRemoveWord)))
        );
    }

    /**
     * execute the add word command
     * @param context {@link CommandContext<CommandSourceStack>}
     * @return int
     */
    private static int executeAddWord(@NonNull CommandContext<CommandSourceStack> context) {
        if (isDisabled()) return 1;
        String word = getParameter(context);
        filterService.addBlockedWord(word);
        filterService.reload();
        return 0;
    }

    /**
     * execute the remove word command
     * @param context {@link CommandContext<CommandSourceStack>}
     * @return int
     */
    private static int executeRemoveWord(@NonNull CommandContext<CommandSourceStack> context) {
        if (isDisabled()) return 1;
        String word = getParameter(context);
        filterService.removeBlockedWord(word);
        filterService.reload();
        return 0;
    }

    /**
     * suggest words from the filter list
     * @param context {@link CommandContext<CommandSourceStack>}
     * @param builder {@link SuggestionsBuilder}
     * @return {@link CompletableFuture<Suggestions>}
     */
    private static @NonNull CompletableFuture<Suggestions> suggestWordsList(
            @NonNull CommandContext<CommandSourceStack> context,
            @NonNull SuggestionsBuilder builder
    ) {
        String word = "";
        try {
            word = getParameter(context);
        } catch (IllegalArgumentException ignored) {
            log.debug("Invalid word parameter provided");
        }
        if (word.isBlank()) return builder.buildFuture();
        Set<String> blockedWords = config.getBlockedWords();
        for (String blockedWord : blockedWords) {
            if (!blockedWord.contains(word)) continue;
            builder.suggest(blockedWord);
        }
        return builder.buildFuture();
    }

    /**
     * check if ChatGuard is disabled
     * @return boolean
     */
    private static boolean isDisabled() {
        log.debug("ChatGuard is {}", config.isEnabled() ? "enabled" : "disabled");
        return !config.isEnabled();
    }

    /**
     * get the parameter from the command context
     * @param context {@link CommandContext<CommandSourceStack>}
     * @return String
     */
    private static String getParameter(@NonNull CommandContext<CommandSourceStack> context) {
        return StringArgumentType.getString(context, "word");
    }
}
