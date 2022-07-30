package Operations;

import EnigmaMachine.EnigmaMachine;
import TDO.MachineDetails;

public class Engine implements OperationsMachine{
    private EnigmaMachine enigmaMachine;

    @Override
    public void automaticSettingsInitialize() {

    }

    @Override
    public void manualSettingsInitialize() {

    }

    @Override
    public void setMachineDetails() {

    }

    @Override
    public MachineDetails getMachineDetails() throws Exception {
        if(enigmaMachine != null)
            return new MachineDetails(enigmaMachine.getAllrotors(), enigmaMachine.getCurrentRotorsInUse(), enigmaMachine.getAllReflectors(), enigmaMachine.getCurrentReflectorInUse(), enigmaMachine.getKeyboard(), enigmaMachine.getPluginBoard());
        else {
            throw new Exception("There is no exists Machine");
        }
    }

    @Override
    public void analyzeMachineHistoryAndStatistics() {

    }

    @Override
    public void processInput() {

    }
}
