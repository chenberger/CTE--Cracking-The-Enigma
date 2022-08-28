package Engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Dictionary {
    private final Set<String> words;
    private final Set<Character> excludeChars;

    public Dictionary() {
        this.words = new HashSet<>();
        this.excludeChars = new HashSet<>();
    }
    public void setDictionary(String words, String excludeChars){
        this.excludeChars.addAll(excludeChars.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));

        cleanDictionaryFromExcludeChars(Arrays.asList(words.trim().split(" ")));
    }

    private void cleanDictionaryFromExcludeChars(List<String> unCleanedWords) {
        unCleanedWords.forEach(word -> {
            this.words.add(word.replaceAll("[" + excludeChars + "]", ""));
        });
    }
}
