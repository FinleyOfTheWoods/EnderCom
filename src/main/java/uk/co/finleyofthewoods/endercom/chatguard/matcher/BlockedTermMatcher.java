package uk.co.finleyofthewoods.endercom.chatguard.matcher;

import org.jspecify.annotations.NonNull;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Matches whole words and multi-word phrases against a set of blocked terms.
 * Immutable and thread-safe.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public final class BlockedTermMatcher {
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private static final Pattern APOSTROPHES = Pattern.compile("['’`]");
    private static final Pattern NON_WORD = Pattern.compile("[^\\p{L}\\p{N}]+");
    private static final java.util.Map<Character, Character> LEET = java.util.Map.of(
            '0', 'o', '1', 'i', '3', 'e', '4', 'a',
            '5', 's', '7', 't', '@', 'a', '$', 's');


    private final Set<String> terms;
    private final int maxTokens;

    /**
     * Create a new instance
     * @param blockedTerms set of blocked terms
     */
    public BlockedTermMatcher(@NonNull Set<String> blockedTerms) {
        Set<String> normalised = new HashSet<>();
        int max = 0;
        for (String term : blockedTerms) {
            String[] tokens = tokenise(term);
            if (tokens.length == 0) {
                continue; // ignore blank/punctuation-only entries
            }
            normalised.add(String.join(" ", tokens));
            max = Math.max(max, tokens.length);
        }
        this.terms = Set.copyOf(normalised);
        this.maxTokens = max;
    }

    /**
     * @return the matched (normalised) term, or empty if the message is clean
     */
    public @NonNull Optional<String> findMatch(@NonNull String message) {
        if (terms.isEmpty()) {
            return Optional.empty();
        }

        String[] tokens = tokenise(message);

        StringBuilder candidate = new StringBuilder();

        for (int start = 0; start < tokens.length; start++) {
            candidate.setLength(0);
            int limit = Math.min(tokens.length, start + maxTokens);
            for (int end = start; end < limit; end++) {
                if (end > start) {
                    candidate.append(' ');
                }
                candidate.append(tokens[end]);
                String run = candidate.toString();
                if (terms.contains(run)) {
                    return Optional.of(run);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Tokenise the input message
     * @param input value to tokenise
     * @return {@code String[]} of tokens
     */
    static String @NonNull [] tokenise(@NonNull String input) {
        String cleanedInput = deLeet(input);
        String normalisedInput = Normalizer.normalize(cleanedInput, Normalizer.Form.NFD);
        normalisedInput = DIACRITICS.matcher(normalisedInput).replaceAll("");
        normalisedInput = normalisedInput.toLowerCase(Locale.ROOT);
        normalisedInput = APOSTROPHES.matcher(normalisedInput).replaceAll("");   // "it's" -> "its"
        normalisedInput = NON_WORD.matcher(normalisedInput).replaceAll(" ").strip();
        return normalisedInput.isEmpty() ? new String[0] : normalisedInput.split(" ");
    }

    /**
     * De-leet the input message
     * @param message value to de-leet
     * @return {@code String} de-leet message
     */
    private static @NonNull String deLeet(@NonNull String message) {
        StringBuilder builder = new StringBuilder(message.length());
        for (char character : message.toCharArray()) {
            builder.append(LEET.getOrDefault(character, character));
        }
        return builder.toString();
    }
}