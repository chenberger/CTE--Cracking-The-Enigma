package Engine;

import EnigmaMachineException.WordNotValidInDictionaryException;

import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private final Set<String> words;
    private final Set<Character> excludeChars;

    public Dictionary() {
        this.words = new HashSet<>();
        this.excludeChars = new HashSet<>();
    }
    public void setDictionary(String words, String excludeChars){
        this.excludeChars.addAll(excludeChars.toUpperCase().chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));

        cleanDictionaryFromExcludeChars(Arrays.asList(words.toUpperCase().trim().split(" ")));
    }

    public void cleanDictionaryFromExcludeChars(List<String> unCleanedWords) {
        unCleanedWords.forEach(word -> {
            this.words.add(word.replaceAll("[" + excludeChars + "]", ""));
        });
    }

    public Set<String> getDictionary() {
        return words;
    }

    public List<String> validateWords(List<String> wordsToCheck) throws WordNotValidInDictionaryException {
        WordNotValidInDictionaryException wordNotValidInDictionary = new WordNotValidInDictionaryException(words);
        List<String> wordsToCheckAfterCleanExcludeChars = new ArrayList<>();

        wordsToCheck.forEach(word -> {
            wordsToCheckAfterCleanExcludeChars.add(word.replaceAll("[" + excludeChars + "]", ""));
        });

        wordsToCheckAfterCleanExcludeChars.forEach(word -> {
            if(!words.contains(word)) {
                wordNotValidInDictionary.addIllegalWord(word);
            }
        });

        if(wordNotValidInDictionary.isExceptionNeedToThrown()) {
            throw wordNotValidInDictionary;
        }

        return wordsToCheckAfterCleanExcludeChars;
    }
}
