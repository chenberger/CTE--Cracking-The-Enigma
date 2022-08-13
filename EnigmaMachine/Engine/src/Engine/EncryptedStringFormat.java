package Engine;

import java.util.List;
import java.util.stream.Collectors;

public class EncryptedStringFormat extends StringFormat<Character>{

    public EncryptedStringFormat(List<Character> elements) {

        super(elements, StringFormatType.ENCRYPTED_STRING);
    }

    @Override
    public String toString() {

        return super.openSector + elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
