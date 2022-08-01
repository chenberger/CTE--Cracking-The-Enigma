package EnigmaMachine;

import javafx.util.Pair;
import jdk.nashorn.internal.runtime.logging.Logger;

import java.util.*;

@Logger
public class Rotor implements Decoder{
    private final Integer id;
    private Integer notch;
    private final Integer startingPosition;
    private final Boolean isFirstRotor;
    private final Integer ONE_STEP = 1;
    private final List<Pair<Character, Character>> mappingABC;

    //private static final Logger logger = LoggerFactory.getLogger();

    public Rotor(Integer i_Id, Integer i_Notch, Boolean i_FirstRotor, List<Pair<Character, Character>> i_MappingABC, Character startingCharToWindow){
        mappingABC = i_MappingABC;
        id = i_Id;
        notch = i_Notch;
        startingPosition = 0;
        isFirstRotor = i_FirstRotor;
        SpinRotor(GetIndexByValue(startingCharToWindow));
    }

    private int GetIndexByValue(Character startingCharToWindow) {
        for(int i = 0; i < mappingABC.size(); i++) {
            if(mappingABC.get(i).getValue() == startingCharToWindow) {
                return i;
            }
        }

        return -1;
    }

    public void Initialize(List<Pair<Character, Character>> i_Words) {
        mappingABC.addAll(i_Words);
    }

    public boolean Rotate(boolean isPreviewsRotorNotchReachedTheWindow) {
        if(IsRotorNeedToMove(isPreviewsRotorNotchReachedTheWindow)) {
            SpinRotor(ONE_STEP);
        }

        return notch == startingPosition;
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
        Collections.rotate(mappingABC, stepsToSpin * -1);
         if(stepsToSpin <= notch) {
             notch -= stepsToSpin;
         }
         else {
             notch = mappingABC.size() - (stepsToSpin - notch);
         }
}

    private Boolean IsRotorNeedToMove(boolean isPreviewsRotorNotchReachedTheWindow) {
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
}