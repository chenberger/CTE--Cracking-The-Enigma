package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StartingRotorPositionSector extends Sector<Character>{

    public StartingRotorPositionSector(List<Character> startingPosition) {
        super(startingPosition, SectorType.START_POSITION_ROTORS);
    }

    @Override
    public String toString() {
        List<Character> reversedId = new ArrayList<>(elements);
        Collections.reverse(reversedId);
        return super.openSector + reversedId.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
