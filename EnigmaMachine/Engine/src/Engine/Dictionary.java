package Engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionary {
    private Set<String> words;

    public Dictionary() {
        this.words = new HashSet<>();
    }
    public void setDictionary(String Words){
        List<String> ListOfWords = Arrays.asList(Words.split(" "));
        words.addAll(ListOfWords);
    }
}
