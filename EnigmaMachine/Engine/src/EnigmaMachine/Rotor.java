package EnigmaMachine;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.logging.Logger;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Logger
public class Rotor implements Decoder, Serializable {
    private final int id;
    private Integer notch;
    private final Integer WINDOW_POSITION;
    private Character startingRightCharToWindow;
    private Boolean isFirstRotor;
    private static final Integer ONE_STEP = 1;
    private final List<Pair<Character, Character>> mappingABC;
    private int startingNotchPosition;

    public Rotor(int id, int notch, List<Pair<Character, Character>> mappingABC) {
        this.id = id;
        this.notch = notch;
        this.mappingABC = mappingABC;
        this.WINDOW_POSITION = 0;
    }

    public void setIsFirstRotor(boolean isFirstRotor) {
        this.isFirstRotor = isFirstRotor;
    }
    public Rotor(Integer id, Integer notch, Boolean isFirstRotor, List<Pair<Character, Character>> mappingABC, Character startingRightCharToWindow){
        this.mappingABC = mappingABC;
        this.startingRightCharToWindow = startingRightCharToWindow;
        this.id = id;
        this.notch = notch;
        this.WINDOW_POSITION = 0;
        this.isFirstRotor = isFirstRotor;
        reset();
    }

    public void reset() {
        spinRotor(getIndexByValue(startingRightCharToWindow));
        this.startingNotchPosition = notch;
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

        return notch == WINDOW_POSITION;
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

    //region Setters
    public void setIsFirstRotor(Boolean isFirstRotor) {
        this.isFirstRotor = isFirstRotor;
    }
    //endregion
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

    public int getStartingNotchPosition() {
        return startingNotchPosition;
    }

    public Character getCurrentRightCharInTheWindow() {
        return mappingABC.get(0).getValue();
    }

    //endregion
}