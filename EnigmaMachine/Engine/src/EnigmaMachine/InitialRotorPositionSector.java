package EnigmaMachine;

import java.util.List;
import java.util.stream.Collectors;

public class InitialRotorPositionSector extends Sector<Character>{

    public InitialRotorPositionSector(List<Character> initialPosition) {
        super(initialPosition, SectorType.START_POSITION_ROTORS);
    }

    @Override
    public String toString() {
        return super.openSector + super.elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
