package DTO;

import EnigmaMachine.RomanNumber;

import java.util.List;
import java.util.Set;

public class DetailsToManualCodeInitializer {
    private List<Integer> allRotorsIds;
    private List<RomanNumber> allReflectorsIds;
    private Set<Character> keyBoardCharacters;
    private int numOfRotorsInUse;

    public DetailsToManualCodeInitializer(List<Integer> allRotorsIds, List<RomanNumber> allReflectorsIds, Set<Character> keyBoardCharacters, int numOfRotorsInUse) {
        this.allRotorsIds = allRotorsIds;
        this.allReflectorsIds = allReflectorsIds;
        this.keyBoardCharacters = keyBoardCharacters;
        this.numOfRotorsInUse = numOfRotorsInUse;
    }
    // getters and setters
    public List<Integer> getAllRotorsIds() {
        return allRotorsIds;
    }
    public void setAllRotorsIds(List<Integer> allRotorsIds) {
        this.allRotorsIds = allRotorsIds;
    }
    public List<RomanNumber> getAllReflectorsIds() {
        return allReflectorsIds;
    }
    public void setAllReflectorsIds(List<RomanNumber> allReflectorsIds) {
        this.allReflectorsIds = allReflectorsIds;
    }
    public Set<Character> getKeyBoardCharacters() {
        return keyBoardCharacters;
    }
    public void setKeyBoardCharacters(Set<Character> keyBoardCharacters) {
        this.keyBoardCharacters = keyBoardCharacters;
    }
    public int getNumOfRotorsInUse() {
        return numOfRotorsInUse;
    }
    public void setNumOfRotorsInUse(int numOfRotorsInUse) {
        this.numOfRotorsInUse = numOfRotorsInUse;
    }
    // end of getters and setters
}
