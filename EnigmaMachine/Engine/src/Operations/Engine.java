package Operations;

import java.io.*;

import EnigmaMachine.EnigmaMachine;
import TDO.MachineDetails;
import Jaxb.Schema.Generated.CTEEnigma;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import EnigmaMachine.SettingsFormat;


public class Engine implements OperationsMachine, Serializable {
    private EnigmaMachine enigmaMachine;
    private MachineDetails machineDetails;
    public void setMachineDetails(String machineDetailsXmlFilePath) {
        // TODO implement here
            try {
                InputStream inputStream = new FileInputStream(new File(machineDetailsXmlFilePath));
                CTEEnigma Enigma = deserializeFrom(inputStream);
                System.out.println("name of first country is: " + Enigma.getCTEMachine().getABC());
            }
            catch (JAXBException | FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    public CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("Jaxb.Schema.Generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    @Override
    public void automaticSettingsInitialize() {

    }

    @Override
    public void manualSettingsInitialize(SettingsFormat settingsFormat) throws Exception {
        if(enigmaMachine == null) {
            throw new Exception("There is no exists Machine");
        }

        enigmaMachine.initializeSetting(settingsFormat);
    }

    @Override
    public void resetMachineSettings() {

    }

    @Override
    public MachineDetails getMachineDetails() throws Exception {
        if(enigmaMachine == null) {
            throw new Exception("There is no exists Machine");
       }
       else if (machineDetails == null) {
            machineDetails = new MachineDetails(enigmaMachine.getAllrotors(), enigmaMachine.getCurrentRotorsInUse(), enigmaMachine.getAllReflectors(), enigmaMachine.getCurrentReflectorInUse(), enigmaMachine.getKeyboard(), enigmaMachine.getPluginBoard());
            machineDetails.initializeSettingFormat();
        }

       return machineDetails;
    }

    @Override
    public void analyzeMachineHistoryAndStatistics() {

    }

    @Override
    public void processInput() {

    }
  }
