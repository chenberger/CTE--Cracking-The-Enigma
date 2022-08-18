package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.RotorsInUseSettingsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RotorIDSector extends Sector<Integer> implements Serializable {

    private final CharSequence delimiter = ",";

    public RotorIDSector(List<Integer> rotorsId) {
        super(rotorsId, SectorType.ROTORS_ID);
    }

    @Override
    public String toString() {
        List<Integer> reversedId = new ArrayList<>(elements);
        Collections.reverse(reversedId);

        return super.openSector + reversedId.stream().map(Object::toString).collect(Collectors.joining(delimiter)) + super.closeSector;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RotorIDSector clonedRotorIdSector =  new RotorIDSector(new ArrayList<>(getElements()));

        return clonedRotorIdSector;
    }

    @Override
    public void validateSector(EnigmaMachine enigmaMachine) throws RotorsInUseSettingsException {
        enigmaMachine.validateRotorsInUseSettings(this);
    }

    @Override
    public void setSectorInTheMachine(EnigmaMachine enigmaMachine) {
        enigmaMachine.setRotorsInUseSettings(this);
    }

    @Override
    public void addSectorToSettingsFormat(EnigmaMachine enigmaMachine) {
        enigmaMachine.getOriginalSettingsFormat().addSector(this);
    }
}
