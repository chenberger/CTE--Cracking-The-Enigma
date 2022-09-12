package EnigmaMachine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Keyboard {
    private final Map<Character, Integer> keyboardCharacters;
    private final Integer radixBase;
    private final int maxValueCharacter;

    public Keyboard(Map<Character, Integer> keyboardCharacters) {
        this.keyboardCharacters = keyboardCharacters;
        this.radixBase = keyboardCharacters.size();
        this.maxValueCharacter = getMaxCharacter();
    }

    private int getMaxCharacter() {
        final int[] maxValueCharacter = {-1};

        keyboardCharacters.values().forEach(valueCharacter -> {
            if (valueCharacter > maxValueCharacter[0]) {
                maxValueCharacter[0] = valueCharacter;
            }
        });

        return maxValueCharacter[0];
    }

    synchronized public List<Character> increaseRotorPositions(List<Character> rotorsPositionsToIncrease) throws Exception {
        return increaseRotorPositions(rotorsPositionsToIncrease, 1);
    }

    public List<Character> increaseRotorPositions(List<Character> rotorsPositionsToIncrease, int amountToIncrease) throws Exception {
        if(!isPossibleToIncrease(rotorsPositionsToIncrease, rotorsPositionsToIncrease.size())) {
            throw new Exception();
        }

        int rotorsPositionsInDecimal = convertFromStringToDecimal(rotorsPositionsToIncrease);

        return convertFromDecimalToString(rotorsPositionsInDecimal + amountToIncrease ,  rotorsPositionsToIncrease.size());
    }

    private List<Character> convertFromDecimalToString(int decimalNumberToConvert, int amountOfRotorsInUse) {
        StringBuilder result = new StringBuilder();

        while(decimalNumberToConvert > 0) {
            result.append(getKeyByValue(decimalNumberToConvert % radixBase));
            decimalNumberToConvert /= radixBase;
        }

        while(result.length() < amountOfRotorsInUse) {
            result.append(getKeyByValue(0));
        }

        return result.toString().chars().mapToObj(obj -> (char)obj).collect(Collectors.toList());
    }

    private int convertFromStringToDecimal(List<Character> charactersToConvert) {
        final int[] power = new int[1];
        final int[] result = {0};

        charactersToConvert.forEach(character -> {
            result[0] += keyboardCharacters.get(character) * Math.pow(radixBase, power[0]);
            power[0]++;
        });

        return result[0];
    }

    public boolean containsKey(Character key) {
        return keyboardCharacters.containsKey(key);
    }

    public Set<Character> keySet() {
        return keyboardCharacters.keySet();
    }

    public int size() {
        return keyboardCharacters.size();
    }

    public int get(Character currentChar) {
        return keyboardCharacters.get(currentChar);
    }

    public Character getKeyByValue(int valueToSearch) {
        for (Map.Entry<Character,Integer> entry : keyboardCharacters.entrySet())
            if(entry.getValue() == valueToSearch) {
                return entry.getKey();
            }

        return null;
    }

    public boolean isPossibleToIncrease(List<Character> elements, int numOfActiveRotors) {
        for(Character character : elements) {
            if(keyboardCharacters.get(character) != maxValueCharacter) {
                return true;
            }
        }

        return false;
    }
    public Character getFirstCharacter() {
        return getKeyByValue(0);
    }
    public Iterable<? extends Map.Entry<Character, Integer>> entrySet() {
        return keyboardCharacters.entrySet();
    }
}

