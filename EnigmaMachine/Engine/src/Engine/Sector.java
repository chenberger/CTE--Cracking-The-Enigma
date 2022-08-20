package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.PluginBoardSettingsException;
import EnigmaMachineException.ReflectorSettingsException;
import EnigmaMachineException.RotorsInUseSettingsException;
import EnigmaMachineException.StartingPositionsOfTheRotorException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class Sector<T> implements Serializable {
    protected final char openSector = '<';
    protected final char closeSector = '>';
    protected List<T> elements;
    public SectorType type;

    public Sector(List<T> elements, SectorType type) {
        this.elements = elements;
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sector)) return false;
        Sector<?> sector = (Sector<?>) o;
        return elements.equals(sector.elements) && type == sector.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements, type);
    }

    //region Getters
    public List<T> getElements() {
        return elements;
    }

    public abstract void validateSector(EnigmaMachine enigmaMachine) throws RotorsInUseSettingsException, ReflectorSettingsException, PluginBoardSettingsException, StartingPositionsOfTheRotorException;

    public abstract void setSectorInTheMachine(EnigmaMachine enigmaMachine) throws CloneNotSupportedException;

    public abstract void addSectorToSettingsFormat(EnigmaMachine enigmaMachine);
    //endregion
}
