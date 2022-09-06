package EnigmaMachineException;

public class DecryptionMessegeNotInitializedException extends Exception{
    @Override
    public String getMessage() {
        return "Failed to start the brute force deciphering, There is no encrypted string to decrypt";
    }
}
