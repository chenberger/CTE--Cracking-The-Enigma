package EnigmaMachine;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Rotor {
    private final Integer id;
    private Integer notch;
    private final Integer startingPosition;
    private Integer currentPosition;
    private final Boolean isFirstRotor;
    private final Integer ONE_STEP = 1;
    private final List<Pair<Character, Character>> mappingABC;

    public Rotor(Integer i_Id, Integer i_Notch, Boolean i_FirstRotor){
        mappingABC = new ArrayList<>();
        id = i_Id;
        notch = i_Notch;
        startingPosition = 0;
        currentPosition = startingPosition;
        isFirstRotor = i_FirstRotor;
    }

    public void Initialize(List<Pair<Character, Character>> i_Words) {
        mappingABC.addAll(i_Words);
    }

    public int Set(Integer inputCharIndex, Direction direction){
        int outPutCharIndex = -1;

        if(IsRotorNeedToMove()) {
            SpinRotator(ONE_STEP);
        }

        if(direction == Direction.FORWARD) {
            outPutCharIndex = SearchKey(mappingABC.get(inputCharIndex).getValue());
        }
        else if(direction == Direction.BACKWARD){
            outPutCharIndex = SearchValue(mappingABC.get(inputCharIndex).getKey());
        }

        return outPutCharIndex;
    }

    private int SearchValue(Character key) {
        int valueIndex = -1;

        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getValue() == key) {
                valueIndex = i;
                break;
            }
        }

        return valueIndex;
    }

    private int SearchKey(Character value) {
        int keyIndex = -1;

        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getKey() == value) {
                keyIndex = i;
                break;
            }
        }

        return keyIndex;
    }

    private void SpinRotator(int stepsToSpin) {
        Collections.rotate(mappingABC, stepsToSpin);
         if(stepsToSpin <= notch) {
             notch -= stepsToSpin;
         }
         else {
             notch = mappingABC.size() - (stepsToSpin - notch);
         }

   }

    private Boolean IsRotorNeedToMove() {
        return isFirstRotor || notch == startingPosition;
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
}