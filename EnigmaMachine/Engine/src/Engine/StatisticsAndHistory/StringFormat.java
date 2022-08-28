package Engine.StatisticsAndHistory;

import java.io.Serializable;
import java.util.List;

public class StringFormat<T> implements Serializable {
    protected final char openSector = '<';
    protected final char closeSector = '>';
    protected List<T> elements;
    private StringFormatType type;

    public StringFormat(List<T> elements, StringFormatType type) {
        this.elements = elements;
        this.type = type;
    }

    public List<T> getElements() {
        return elements;
    }
}
