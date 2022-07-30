package EnigmaMachine;

import java.sql.Ref;
import java.util.List;

public class EnigmaMachine implements Decoder{
    private List<Rotor> rotors;
    private List<Rotor> currentRotors;
    private Reflctor currentReflector;
    private List<Reflctor> reflectors;
    private PluginBoard pluginBoard;
    private List<Character> ABC;

    public EnigmaMachine(List<Rotor> i_Rotors, List<Reflctor> i_Reflectors) {
        rotors = i_Rotors;
        reflectors = i_Reflectors;
    }

    @Override
    public int Decode(int inputCharIndex, Direction direction) {
        int currentCharIndex;

        RotateRotors();

        currentCharIndex = pluginBoard.getPlugedPair(ABC.get(inputCharIndex));
        currentCharIndex = DecodeByDirection(currentCharIndex, Direction.FORWARD);
        currentCharIndex = currentReflector.SetIndex(currentCharIndex);
        currentCharIndex = DecodeByDirection(currentCharIndex, Direction.BACKWARD);
        currentCharIndex = pluginBoard.getPlugedPair(ABC.get(inputCharIndex));

        return currentCharIndex;
    }

    private void RotateRotors() {
        boolean isPreviewsRotorReachedToWindow = false;
        for(Rotor rotor : rotors) {
            isPreviewsRotorReachedToWindow = rotor.Rotate(isPreviewsRotorReachedToWindow);
        }
    }

    private int DecodeByDirection(int currentCharIndex, Direction direction) {
        for(Rotor rotor : rotors) {
            currentCharIndex = rotor.Decode(currentCharIndex, direction);
        }

        return currentCharIndex;
    }
}
