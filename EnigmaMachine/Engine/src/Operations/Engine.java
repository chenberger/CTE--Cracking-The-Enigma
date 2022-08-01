package Operations;

import java.io.*;

import Jaxb.Schema.Generated;
import Jaxb.Schema.Generated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class Engine implements OperationsMachine, Serializable {
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


