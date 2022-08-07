package Operations;

import java.util.List;
import java.util.stream.Collectors;

public class OriginalStringFormat extends Sector<Character>{


    public OriginalStringFormat(List<Character> originalString) {
        super(originalString, SectorType.ORIGINAL_STRING);
    }

    @Override
    public String toString() {

        return super.openSector + elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
