package EnigmaMachine.Settings;

import EnigmaMachine.EnigmaMachine;
import EnigmaMachineException.StartingPositionsOfTheRotorException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StartingRotorPositionSector extends Sector<Character> implements Serializable {
    private List<Integer> notchPositions;
    private final CharSequence delimiter = ",";

    public StartingRotorPositionSector(List<Character> startingPosition) {
        super(startingPosition, SectorType.START_POSITION_ROTORS);
        this.notchPositions = new ArrayList<>();
    }

    public void setCurrentNotchPositions(List<Integer> notchPositions) {
        this.notchPositions = new ArrayList<>(notchPositions);
    }

    public void setCurrentCharactersInTheWindow(List<Character> charactersInTheWindow) {
        this.elements = new ArrayList<>(charactersInTheWindow);
    }

    @Override
    public String toString() {
        List<Character> reversedStartingPositions = new ArrayList<>(elements);
        Collections.reverse(reversedStartingPositions);
        List<Integer> reversedNotchPositions = new ArrayList<>(notchPositions);
        Collections.reverse(reversedNotchPositions);
        List<String> startingPositionsString = reversedStartingPositions.stream().map(Object::toString).collect(Collectors.toList());

        for (int i = 0; i < reversedNotchPositions.size(); i++) {
            startingPositionsString.set(i, startingPositionsString.get(i) + "(" + reversedNotchPositions.get(i) + ")");
        }

        return super.openSector + startingPositionsString.stream().map(Object::toString).collect(Collectors.joining(delimiter)) + super.closeSector;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        StartingRotorPositionSector clonedStartingRotorPositionSector =  new StartingRotorPositionSector(new ArrayList<>(getElements()));
        clonedStartingRotorPositionSector.setCurrentNotchPositions(this.notchPositions);

        return clonedStartingRotorPositionSector;
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
        setCurrentNotchPositions(enigmaMachine.getCurrentRotorsInUse().stream().map(rotor -> rotor.getStartingNotchPosition()).collect(Collectors.toList()));
        enigmaMachine.getOriginalSettingsFormat().addSector(this);
    }
}
