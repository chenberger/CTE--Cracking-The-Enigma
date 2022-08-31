package Engine.StatisticsAndHistory;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class OriginalStringFormat extends StringFormat<Character> implements Serializable {

    public OriginalStringFormat(List<Character> originalString) {
        super(originalString, StringFormatType.ORIGINAL_STRING);
    }
    public void add(Character character) {
        elements.add(character);
    }
    @Override
    public String toString() {

        return super.openSector + elements.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }
}
