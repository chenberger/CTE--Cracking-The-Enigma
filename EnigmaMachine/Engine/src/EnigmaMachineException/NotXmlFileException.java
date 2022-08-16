package EnigmaMachineException;

public class NotXmlFileException extends Exception {
    @Override
    public String getMessage(){
        return System.lineSeparator() + "The file is not an xml file, the file path should end with: .xml";
    }
}

