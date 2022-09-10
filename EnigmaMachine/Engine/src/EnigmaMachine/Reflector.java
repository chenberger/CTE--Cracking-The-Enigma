package EnigmaMachine;

import java.io.Serializable;
import java.util.*;
public class Reflector implements Cloneable, Serializable {
    private final RomanNumber id;
    private final Map<Integer,Integer> reflectorPairs;

    public Reflector(RomanNumber Id, Map<Integer,Integer> reflectedNumbers ){
        id = Id;
        reflectorPairs = reflectedNumbers;
    }

    public Reflector(RomanNumber Id){
        id = Id;
        reflectorPairs = new HashMap<>();
    }

    public void clear() {
        reflectorPairs.clear();
    }


    public Integer SetIndex(Integer inputtedIndex){
        return reflectorPairs.get(inputtedIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reflector reflector = (Reflector) o;
        return Objects.equals(id, reflector.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Reflector(id, new HashMap<Integer, Integer>(reflectorPairs));
    }

    //region Getters
    public RomanNumber id() {
        return id;
    }
    public int size() {
        return reflectorPairs.size();
    }

    public Object cloneReflector() {
        return new Reflector(id, cloneReflectorPairs());
    }

    private Map<Integer, Integer> cloneReflectorPairs() {
        return new HashMap<Integer, Integer>(reflectorPairs);
    }

    public Map<Integer, Integer> getReflectedPairs() {
        return new HashMap<>(reflectorPairs);
    }

    //endregion
}
