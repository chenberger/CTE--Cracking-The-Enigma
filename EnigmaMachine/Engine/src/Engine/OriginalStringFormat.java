package Engine;

import java.util.List;
import java.util.stream.Collectors;

public class OriginalStringFormat extends StringFormat<Character>{


    public OriginalStringFormat(List<Character> originalString) {
        super(originalString, StringFormatType.ORIGINAL_STRING);
    }

    @Override
    public String toString() {

        return super.openSector + elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
