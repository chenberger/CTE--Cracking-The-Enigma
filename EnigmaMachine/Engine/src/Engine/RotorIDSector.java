package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.RotorsInUseSettingsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RotorIDSector extends Sector<Integer>{

    private final CharSequence delimiter = ",";
    private List<Integer> notchPositions;

    public RotorIDSector(List<Integer> rotorsId) {
        super(rotorsId, SectorType.ROTORS_ID);
        this.notchPositions = new ArrayList<>();
    }

    public void setCurrentNotchPositions(List<Integer> notchPositions) {

        this.notchPositions = new ArrayList<>(notchPositions);
    }

    @Override
    public String toString() {
        List<Integer> reversedId = new ArrayList<>(elements);
        List<Integer> reversedNotchPositions = new ArrayList<>(notchPositions);
        Collections.reverse(reversedNotchPositions);
        Collections.reverse(reversedId);
        List<String> rotorsIdString = reversedId.stream().map(Object::toString).collect(Collectors.toList());

        for (int i = 0; i < reversedNotchPositions.size(); i++) {
            rotorsIdString.set(i, rotorsIdString.get(i) + "(" + reversedNotchPositions.get(i) + ")");
        }

        return super.openSector + rotorsIdString.stream().map(Object::toString).collect(Collectors.joining(delimiter)) + super.closeSector;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RotorIDSector clonedRotorIdSector =  new RotorIDSector(new ArrayList<>(getElements()));
        clonedRotorIdSector.setCurrentNotchPositions(this.notchPositions);

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
        setCurrentNotchPositions(enigmaMachine.getCurrentRotorsInUse().stream().map(rotor -> rotor.getStartingNotchPosition()).collect(Collectors.toList()));
        enigmaMachine.getOriginalSettingsFormat().addSector(this);
    }
}
