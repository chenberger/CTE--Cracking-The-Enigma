package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RotorIDSector extends Sector<Integer>{

    private final CharSequence delimiter = ",";

    public RotorIDSector(List<Integer> rotorsId) {
        super(rotorsId, SectorType.ROTORS_ID);
    }

    @Override
    public String toString() {
        List<Integer> reversedId = new ArrayList<>(elements);
        Collections.reverse(reversedId);
        return super.openSector + reversedId.stream().map(Object::toString).collect(Collectors.joining(delimiter)) + super.closeSector;
    }
}
