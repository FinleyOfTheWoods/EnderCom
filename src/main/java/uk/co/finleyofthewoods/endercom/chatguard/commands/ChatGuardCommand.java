package uk.co.finleyofthewoods.endercom.chatguard.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
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

    public ChatGuardCommand() {

    }

    public static void register(
            @NonNull CommandDispatcher<CommandSourceStack> dispatcher,
            @NonNull CommandBuildContext ignored,
            Commands.CommandSelection ignored2
    ) {
        dispatcher.register(Commands.literal("chatguard")
                        //.requires(Permissions.require("chatguard.admin"))
                .then(Commands.literal("add")
                        .then(Commands.argument("word", StringArgumentType.string())
                        .executes(ChatGuardCommand::executeAddWord)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("word", StringArgumentType.string())
                                .suggests(ChatGuardCommand::suggestWordsList)
                                .executes(ChatGuardCommand::executeRemoveWord)))
        );
    }

    private static int executeAddWord(@NonNull CommandContext<CommandSourceStack> context) {
        if (isDisabled()) return 1;
        String word = getParameter(context);
        filterService.addBlockedWord(word);
        filterService.reload();
        return 0;
    }

    private static int executeRemoveWord(@NonNull CommandContext<CommandSourceStack> context) {
        if (isDisabled()) return 1;
        String word = getParameter(context);
        filterService.removeBlockedWord(word);
        filterService.reload();
        return 0;
    }

    private static @NonNull CompletableFuture<Suggestions> suggestWordsList(
            @NonNull CommandContext<CommandSourceStack> context,
            @NonNull SuggestionsBuilder builder
    ) {
        String word = "";
        try {
            word = getParameter(context);
        } catch (IllegalArgumentException ignored) {}
        if (word.isBlank()) return builder.buildFuture();
        Set<String> blockedWords = config.getBlockedWords();
        for (String blockedWord : blockedWords) {
            if (!blockedWord.contains(word)) continue;
            builder.suggest(blockedWord);
        }
        return builder.buildFuture();
    }

    private static boolean isDisabled() {
        log.debug("ChatGuard is {}", config.isEnabled() ? "enabled" : "disabled");
        return !config.isEnabled();
    }

    private static String getParameter(@NonNull CommandContext<CommandSourceStack> context) {
        return StringArgumentType.getString(context, "word");
    }
}
