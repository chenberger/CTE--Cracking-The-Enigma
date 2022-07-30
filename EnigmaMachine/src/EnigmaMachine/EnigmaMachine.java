package EnigmaMachine;

import java.util.List;

public class EnigmaMachine {
    private List<Rotor> rotors;
    private List<Reflctor> reflectors;

    public EnigmaMachine(List<Rotor> i_Rotors, List<Reflctor> i_Reflectors) {
        rotors = i_Rotors;
        reflectors = i_Reflectors;
    }
}
