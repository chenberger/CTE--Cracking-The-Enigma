package Operations;

import java.util.List;
import java.util.stream.Collectors;

public class EncryptedStringFormat extends Sector<Character>{

    public EncryptedStringFormat(List<Character> elements) {
        super(elements, SectorType.ENCRYPTED_STRING);
    }

    @Override
    public String toString() {

        return super.openSector + elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
