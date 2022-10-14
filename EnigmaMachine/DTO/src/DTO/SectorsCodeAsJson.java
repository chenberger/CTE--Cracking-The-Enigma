package DTO;

import EnigmaMachine.RomanNumber;
import EnigmaMachine.Settings.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SectorsCodeAsJson {
    private final List<Integer> notchPositions;
    private int[] rotorIdElements;
    private char[] startingRotorPositionElements;
    private RomanNumber reflectorIdElement;
    private char[] pluginBoardElements;

    public SectorsCodeAsJson(RotorIDSector rotorIDSector, StartingRotorPositionSector startingRotorPositionSector,
                             ReflectorIdSector reflectorIdSector, PluginBoardSector pluginBoardSector) {
        rotorIdElements = rotorIDSector.getElements().stream().mapToInt(Integer::intValue).toArray();

        startingRotorPositionElements = new char[startingRotorPositionSector.getElements().size()];
        for (int i = 0; i < startingRotorPositionElements.length; i++) {
            startingRotorPositionElements[i] = startingRotorPositionSector.getElements().get(i);
        }
        notchPositions = startingRotorPositionSector.getNotchPositions();
        pluginBoardElements = new char[pluginBoardSector.getElements().size() * 2];
        int pairIndex = 0;
        for(int i = 0; i < pluginBoardElements.length; i++) {
            pluginBoardElements[i] = pluginBoardSector.getElements().get(pairIndex).getKey();
            pluginBoardElements[++i] = pluginBoardSector.getElements().get(pairIndex).getValue();
            pairIndex++;
        }
        if(reflectorIdSector.getElements().size() > 0) {
            reflectorIdElement = reflectorIdSector.getElements().get(0);
        }
    }


    public int[] getRotorIdElements() {
        return rotorIdElements;
    }

    public void setRotorIdElements(int[] rotorIdElements) {
        this.rotorIdElements = rotorIdElements;
    }

    public RomanNumber getReflectorIdElement() {
        return reflectorIdElement;
    }

    public void setReflectorIdElement(RomanNumber reflectorIdElement) {
        this.reflectorIdElement = reflectorIdElement;
    }

    public char[] getStartingRotorPositionElements() {
        return startingRotorPositionElements;
    }

    public void setStartingRotorPositionElements(char[] startingRotorPositionElements) {
        this.startingRotorPositionElements = startingRotorPositionElements;
    }

    public char[] getPluginBoardElements() {
        return pluginBoardElements;
    }
    public List<Integer> getNotchPositions() {
        return notchPositions;
    }

    public void setPluginBoardElements(char[] pluginBoardElements) {
        this.pluginBoardElements = pluginBoardElements;
    }

    public List<Sector> getSectors() {
        List<Sector> sectors = new ArrayList<>();

        RotorIDSector rotorIDSector = new RotorIDSector(Arrays.stream(rotorIdElements).boxed().collect(Collectors.toList()));
        StartingRotorPositionSector startingRotorPositionSector = new StartingRotorPositionSector(new ArrayList<>());
        for(char position : startingRotorPositionElements) {
            startingRotorPositionSector.getElements().add(position);
        }
        ReflectorIdSector reflectorIdSector = new ReflectorIdSector(new ArrayList<>(Arrays.asList(reflectorIdElement)));
        PluginBoardSector pluginBoardSector = new PluginBoardSector(new ArrayList<>());

        for(int i = 0; i < pluginBoardElements.length; i++) {
            Pair pair = new Pair(pluginBoardElements[i], pluginBoardElements[++i]);
            pluginBoardSector.getElements().add(pair);
        }

        sectors.add(rotorIDSector);
        sectors.add(startingRotorPositionSector);
        sectors.add(reflectorIdSector);
        sectors.add(pluginBoardSector);

        return sectors;
    }
}
