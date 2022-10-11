package DTO;

import EnigmaMachine.Reflector;
import EnigmaMachine.RomanNumber;
import EnigmaMachine.Rotor;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataToInitializeMachine {

    private Map<Integer, Rotor> rotors;
    private List<Rotor> rotorsInUse;
    private Map<RomanNumber, Reflector> reflectors;
    private final Reflector reflectorInUse;
    private final Map<Character, Integer> keyboard;

    public DataToInitializeMachine(Map<Integer, Rotor> rotors, List<Rotor> rotorsInUse, Map<RomanNumber, Reflector> reflectors, Reflector reflectorInUse, Map<Character, Integer> keyboard) {
        this.rotors = rotors;
        this.rotorsInUse = rotorsInUse;
        this.reflectors = reflectors;
        this.reflectorInUse = reflectorInUse;
        this.keyboard = keyboard;
    }
    //setters and getters
    public Map<Integer, Rotor> getRotors() {
        return rotors;
    }
    public List<Rotor> getRotorsInUse() {
        return rotorsInUse;
    }
    public Map<RomanNumber, Reflector> getReflectors() {
        return reflectors;
    }
    public Reflector getReflectorInUse() {
        return reflectorInUse;
    }
    public Map<Character, Integer> getKeyboard() {
        return keyboard;
    }
    public int getAmountCurrentRotorsInUse() {
        return rotorsInUse.size();
    }
    public int getAmountOfTotalRotors() {
        return rotors.size();
    }
    public int getAmountOfTotalReflectors(){
        return reflectors.size();
    }
    public void setRotors(Map<Integer, Rotor> rotors) {
        this.rotors = rotors;
    }
    public void setRotorsInUse(List<Rotor> rotorsInUse) {
        this.rotorsInUse = rotorsInUse;
    }
    public void setReflectors(Map<RomanNumber, Reflector> reflectors) {
        this.reflectors = reflectors;
    }



}
