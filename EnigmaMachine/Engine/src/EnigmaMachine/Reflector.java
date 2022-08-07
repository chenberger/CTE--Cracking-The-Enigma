package EnigmaMachine;

import java.util.*;

public class Reflector implements Cloneable{
    private final RomanNumber id;
    private final Map<Integer,Integer> reflectorPairs;

    public Reflector(RomanNumber Id, Map<Integer,Integer> reflectedNumbers ){
        //TODO needs to fill the map, which means that I get a pair and needs to put the as key/value with each other
        //example: key = 5 -> value = 2 and key = 2 -> value = 5.
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

    //endregion
}
