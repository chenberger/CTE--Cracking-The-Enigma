package EnigmaMachine;

import java.util.List;
import java.util.stream.Collectors;

public class ReflectorIdSector extends Sector<RomanNumber>{

    public ReflectorIdSector(List<RomanNumber> reflectorId) {
        super(reflectorId, SectorType.REFLECTOR);
    }

    @Override
    public String toString() {
        return super.openSector + super.elements.stream().map(RomanNumber::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
