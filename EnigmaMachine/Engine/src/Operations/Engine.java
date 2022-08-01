package Operations;


import java.io.*;
import EnigmaMachine.EnigmaMachine;
import TDO.MachineDetails;
import Jaxb.Schema.Generated;
import Jaxb.Schema.Generated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class Engine implements OperationsMachine, Serializable {
    private EnigmaMachine enigmaMachine;
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

    }

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
