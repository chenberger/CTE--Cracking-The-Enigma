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
    public Dictionary(Set<String> words, Set<Character> excludeChars) {
        this.words = words;
        this.excludeChars = excludeChars;
    }
    public Set<String> cloneWords() {
        return new HashSet<>(words);
    }
    public Set<Character> cloneExcludeChars() {
        return new HashSet<>(excludeChars);
    }
    public void setDictionary(String words, String excludeChars){
        this.words.clear();
        this.excludeChars.clear();

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
    public Set<Character> getExcludeChars() {
        return excludeChars;
    }

    public List<String> validateWordsAfterCleanExcludeChars(List<String> wordsToCheck) throws WordNotValidInDictionaryException {
        List<String> wordsToCheckAfterCleanExcludeChars = new ArrayList<>();

        wordsToCheck.forEach(word -> {
            wordsToCheckAfterCleanExcludeChars.add(word.replaceAll("[" + excludeChars + "]", ""));
        });

        validateWords(wordsToCheckAfterCleanExcludeChars);

        return wordsToCheckAfterCleanExcludeChars;
    }

    public void validateWords(List<String> wordsToCheckAfterCleanExcludeChars) throws WordNotValidInDictionaryException {
        WordNotValidInDictionaryException wordNotValidInDictionary = new WordNotValidInDictionaryException(words);

        wordsToCheckAfterCleanExcludeChars.forEach(word -> {
            if(!words.contains(word)) {
                wordNotValidInDictionary.addIllegalWord(word);
            }
        });

        if(wordNotValidInDictionary.isExceptionNeedToThrown()) {
            throw wordNotValidInDictionary;
        }
    }
}
