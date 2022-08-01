package EnigmaMachine;

import Operations.RomanNumber;

import java.util.*;

public class Reflctor{
    private final RomanNumber id;
    private final Map<Integer,Integer> reflectorPairs;

    public Reflctor(RomanNumber Id, Map<Integer,Integer> reflectedNumbers ){
        //needs to fill the map, which means that I get a pair and needs to put the as key/value with each other
        //example: key = 5 -> value = 2 and key = 2 -> value = 5.
        id = Id;
        reflectorPairs = reflectedNumbers;
    }


    public Integer SetIndex(Integer inputtedIndex){
        return reflectorPairs.get(inputtedIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reflctor reflctor = (Reflctor) o;
        return Objects.equals(id, reflctor.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
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
