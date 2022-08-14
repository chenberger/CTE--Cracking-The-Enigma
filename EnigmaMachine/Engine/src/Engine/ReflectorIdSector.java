package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachine.RomanNumber;
import EnigmaMachineException.ReflectorSettingsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectorIdSector extends Sector<RomanNumber> implements Serializable {

    public ReflectorIdSector(List<RomanNumber> reflectorId) {
        super(reflectorId, SectorType.REFLECTOR);
    }

    @Override
    public String toString() {
        return super.openSector + super.elements.stream().map(RomanNumber::toString).collect(Collectors.joining()) + super.closeSector;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ReflectorIdSector(new ArrayList<>(getElements()));
    }

    @Override
    public void validateSector(EnigmaMachine enigmaMachine) throws ReflectorSettingsException {
        enigmaMachine.validateReflectorInUseSettings(this);
    }

    @Override
    public void setSectorInTheMachine(EnigmaMachine enigmaMachine) throws CloneNotSupportedException {
        enigmaMachine.setReflectorInUseSettings(this);
    }

    @Override
    public void addSectorToSettingsFormat(EnigmaMachine enigmaMachine) {
        enigmaMachine.getOriginalSettingsFormat().addSector(this);
    }
}
