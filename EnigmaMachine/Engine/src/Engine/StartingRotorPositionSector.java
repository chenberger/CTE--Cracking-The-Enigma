package Engine;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.StartingPositionsOfTheRotorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StartingRotorPositionSector extends Sector<Character>{

    public StartingRotorPositionSector(List<Character> startingPosition) {
        super(startingPosition, SectorType.START_POSITION_ROTORS);
    }

    @Override
    public String toString() {
        List<Character> reversedId = new ArrayList<>(elements);
        Collections.reverse(reversedId);
        return super.openSector + reversedId.stream().map(Object::toString).collect(Collectors.joining()) + super.closeSector;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new StartingRotorPositionSector(new ArrayList<>(getElements()));
    }

    @Override
    public void validateSector(EnigmaMachine enigmaMachine) throws StartingPositionsOfTheRotorException {
        enigmaMachine.validateStartingPositionRotorsSettings(this);
    }

    @Override
    public void setSectorInTheMachine(EnigmaMachine enigmaMachine) throws CloneNotSupportedException {
        enigmaMachine.setStartingPositionRotorsSettings(this);
    }

    @Override
    public void addSectorToSettingsFormat(EnigmaMachine enigmaMachine) {
        enigmaMachine.getOriginalSettingsFormat().addSector(this);
    }
}
