package EnigmaMachine;

import java.util.List;
import java.util.stream.Collectors;

public class StartingRotorPositionSector extends Sector<Character>{

    public StartingRotorPositionSector(List<Character> startingPosition) {
        super(startingPosition, SectorType.START_POSITION_ROTORS);
    }

    @Override
    public String toString() {
        return super.openSector + super.elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
