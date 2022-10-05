package DTO;

public class MachineConfigurationToShow {

    private int numOfPossibleRotors;
    private int numOfPossibleReflectors;
    private int numOfRotorsInUse;
    private int numberOfReflectors;
    private int numOfProcessedMessages;
    private String currentCodeConfiguration;
    private String originalCodeConfiguration;

    public MachineConfigurationToShow(int numOfPossibleRotors, int numOfPossibleReflectors, int numOfRotorsInUse,int numOfProcessedMessages, String currentCodeConfiguration, String originalCodeConfiguration) {
        this.numOfPossibleRotors = numOfPossibleRotors;
        this.numOfPossibleReflectors = numOfPossibleReflectors;
        this.numOfRotorsInUse = numOfRotorsInUse;
        this.numberOfReflectors = numberOfReflectors;
        this.currentCodeConfiguration = currentCodeConfiguration;
        this.originalCodeConfiguration = originalCodeConfiguration;
        this.numOfProcessedMessages = numOfProcessedMessages;
    }
    // getters and setters
    public int getNumOfPossibleRotors() {
        return numOfPossibleRotors;
    }
    public void setNumOfPossibleRotors(int numOfPossibleRotors) {
        this.numOfPossibleRotors = numOfPossibleRotors;
    }
    public int getNumOfPossibleReflectors() {
        return numOfPossibleReflectors;
    }
    public void setNumOfPossibleReflectors(int numOfPossibleReflectors) {
        this.numOfPossibleReflectors = numOfPossibleReflectors;
    }
    public int getNumOfRotorsInUse() {
        return numOfRotorsInUse;
    }
    public void setNumOfRotorsInUse(int numOfRotorsInUse) {
        this.numOfRotorsInUse = numOfRotorsInUse;
    }
    public int getNumberOfReflectors() {
        return numberOfReflectors;
    }
    public void setNumberOfReflectors(int numberOfReflectors) {
        this.numberOfReflectors = numberOfReflectors;
    }
    public String getCurrentCodeConfiguration() {
        return currentCodeConfiguration;
    }
    public void setCurrentCodeConfiguration(String currentCodeConfiguration) {
        this.currentCodeConfiguration = currentCodeConfiguration;
    }
    public String getOriginalCodeConfiguration() {
        return originalCodeConfiguration;
    }
    public void setOriginalCodeConfiguration(String originalCodeConfiguration) {
        this.originalCodeConfiguration = originalCodeConfiguration;
    }
    public int getNumOfProcessedMessages() {
        return numOfProcessedMessages;
    }
    public void setNumOfProcessedMessages(int numOfProcessedMessages) {
        this.numOfProcessedMessages = numOfProcessedMessages;
    }
    // end of getters and setters
}
