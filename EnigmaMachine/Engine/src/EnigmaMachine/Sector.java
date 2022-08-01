package EnigmaMachine;

import java.util.List;

public abstract class Sector<T>  {
    protected final char openSector = '<';
    protected final char closeSector = '>';
    protected List<T> elements;
    protected SectorType type;

    public Sector(List<T> elements, SectorType type) {

        this.elements = elements;
        this.type = type;
    }

    //region Getters
    public List<T> getElements() {
        return elements;
    }
    //endregion
}
