package EnigmaMachine;

import java.util.List;
import java.util.stream.Collectors;

public class RotorIDSector extends Sector<Integer>{

    private final CharSequence delimiter = ",";

    public RotorIDSector(List<Integer> rotorsId) {
        super(rotorsId, SectorType.ROTORS_ID);
    }

    @Override
    public String toString() {
        return super.openSector + super.elements.stream().map(Object::toString).collect(Collectors.joining(delimiter)) + super.closeSector;
    }
}
