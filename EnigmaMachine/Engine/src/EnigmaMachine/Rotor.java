package EnigmaMachine;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.logging.Logger;

import java.util.*;

@Logger
public class Rotor implements Decoder{
    private final int id;
    private Integer notch;
    private final Integer startingPosition;
    private Character startingRightCharToWindow;
    private final Boolean isFirstRotor;
    private static final Integer ONE_STEP = 1;
    private final List<Pair<Character, Character>> mappingABC;

    public Rotor(Integer id, Integer notch, Boolean isFirstRotor, List<Pair<Character, Character>> mappingABC, Character startingRightCharToWindow){
        this.mappingABC = mappingABC;
        this.startingRightCharToWindow = startingRightCharToWindow;
        this.id = id;
        this.notch = notch;
        this.startingPosition = 0;
        this.isFirstRotor = isFirstRotor;

        spinRotor(getIndexByValue(startingRightCharToWindow));
    }

    private int getIndexByValue(Character startingCharToWindow) {
        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getValue() == startingCharToWindow) {
                return i;
            }
        }

        return -1;
    }

    public boolean rotate(boolean isPreviewsRotorNotchReachedTheWindow) {
        if(isRotorNeedToSpin(isPreviewsRotorNotchReachedTheWindow)) {
            spinRotor(ONE_STEP);
        }

        return notch == startingPosition;
    }

    @Override
    public int decode(int inputCharIndex, Direction direction){
        int outPutCharIndex = -1;

        if(direction == Direction.FORWARD) {
            outPutCharIndex = searchKey(mappingABC.get(inputCharIndex).getValue());
        }
        else if(direction == Direction.BACKWARD){
            outPutCharIndex = searchValue(mappingABC.get(inputCharIndex).getKey());
        }

        return outPutCharIndex;
    }

    private int searchValue(Character left) {
        int leftIndex = -1;

        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getValue() == left) {
                leftIndex = i;
                break;
            }
        }

        return leftIndex;
    }

    private int searchKey(Character right) {
        int rightIndex = -1;

        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getKey() == right) {
                rightIndex = i;
                break;
            }
        }

        return rightIndex;
    }

    private void spinRotor(int stepsToSpin) {
        Collections.rotate(mappingABC, stepsToSpin * -1);
        spinNotch(stepsToSpin);
    }

    private void spinNotch(int stepsToSpin) {
        if(stepsToSpin <= notch) {
            notch -= stepsToSpin;
        }
        else {
            notch = mappingABC.size() - (stepsToSpin - notch);
        }
    }

    private Boolean isRotorNeedToSpin(boolean isPreviewsRotorNotchReachedTheWindow) {
        return isFirstRotor || isPreviewsRotorNotchReachedTheWindow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rotor rotor = (Rotor) o;
        return id == rotor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //region Getters
    public int notch() {
        return notch;
    }

    public int id(){
        return id;
    }

    public Character getStartingRightCharToWindow() {
        return startingRightCharToWindow;
    }
    //endregion

    //region Setters
    public void setStartingRightCharToWindow(Character startingCharacter) {
        startingRightCharToWindow = startingCharacter;
    }

    //endregion
}