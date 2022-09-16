package EnigmaMachineException;

public class BruteForceInProgressException extends Exception{

    private String messege;

    public BruteForceInProgressException(String messege) {
        this.messege = messege;
    }

    @Override
    public String getMessage() {
        return messege;
    }
}
