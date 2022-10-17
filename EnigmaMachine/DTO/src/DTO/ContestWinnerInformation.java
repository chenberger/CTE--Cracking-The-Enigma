package DTO;

public class ContestWinnerInformation {
    private String winnerName;
    private String originalWord;

    public ContestWinnerInformation(String winnerName, String originalWord) {
        this.winnerName = winnerName;
        this.originalWord = originalWord;
    }
    // getters and setters
    public String getWinnerName() {
        return winnerName;
    }
    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
    public String getOriginalWord() {
        return originalWord;
    }
    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }
}
