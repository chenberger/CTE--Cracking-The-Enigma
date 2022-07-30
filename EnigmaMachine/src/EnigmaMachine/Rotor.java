package EnigmaMachine;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Rotor implements Decoder{
    private final Integer id;
    private Integer notch;
    private final Integer startingPosition;
    private final Boolean isFirstRotor;
    private final Integer ONE_STEP = 1;
    private final List<Pair<Character, Character>> mappingABC;

    public Rotor(Integer i_Id, Integer i_Notch, Boolean i_FirstRotor){
        mappingABC = new ArrayList<>();
        id = i_Id;
        notch = i_Notch;
        startingPosition = 0;
        isFirstRotor = i_FirstRotor;
    }

    public void Initialize(List<Pair<Character, Character>> i_Words) {
        mappingABC.addAll(i_Words);
    }

    public boolean Rotate(boolean isPreviewsRotorReachedToWindow) {
        boolean result = notch == startingPosition;

        if(IsRotorNeedToMove(isPreviewsRotorReachedToWindow)) {
            SpinRotor(ONE_STEP);
        }

        return result;
    }

    @Override
    public int Decode(int inputCharIndex, Direction direction){
        int outPutCharIndex = -1;

        if(direction == Direction.FORWARD) {
            outPutCharIndex = SearchKey(mappingABC.get(inputCharIndex).getValue());
        }
        else if(direction == Direction.BACKWARD){
            outPutCharIndex = SearchValue(mappingABC.get(inputCharIndex).getKey());
        }

        return outPutCharIndex;
    }

    private int SearchValue(Character left) {
        int leftIndex = -1;

        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getValue() == left) {
                leftIndex = i;
                break;
            }
        }

        return leftIndex;
    }

    private int SearchKey(Character right) {
        int rightIndex = -1;

        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getKey() == right) {
                rightIndex = i;
                break;
            }
        }

        return rightIndex;
    }

    private void SpinRotor(int stepsToSpin) {
        Collections.rotate(mappingABC, stepsToSpin);  //[1,2,3] - > [2,3,1]
         if(stepsToSpin <= notch) {
             notch -= stepsToSpin;
         }
         else {
             notch = mappingABC.size() - (stepsToSpin - notch);
         }
}

    private Boolean IsRotorNeedToMove(boolean isPreviewsRotorReachedToWindow) {
        return isFirstRotor || isPreviewsRotorReachedToWindow;
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