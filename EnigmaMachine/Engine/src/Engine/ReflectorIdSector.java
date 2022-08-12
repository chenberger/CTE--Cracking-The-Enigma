package Engine;

import EnigmaMachine.RomanNumber;

import java.util.ArrayList;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ReflectorIdSector(new ArrayList<>(getElements()));
    }
}
