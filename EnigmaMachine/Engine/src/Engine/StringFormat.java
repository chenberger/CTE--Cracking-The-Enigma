package Engine;

import java.util.List;

public class StringFormat<T> {
    protected final char openSector = '<';
    protected final char closeSector = '>';
    protected List<T> elements;
    public StringFormatType type;

    public StringFormat(List<T> elements, StringFormatType type) {
        this.elements = elements;
        this.type = type;
    }

    public List<T> getElements() {
        return elements;
    }
}
