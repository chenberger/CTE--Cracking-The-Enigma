package AllieMainScenePane.Body.DashBoardTabPane.ContestData;

public class IllegibleContestAmountChosenException extends Exception{
    private final int legalContestAmount = 1;
    private int illegalContestAmountInserted;

    public IllegibleContestAmountChosenException(int illegalContestAmountInserted) {
        this.illegalContestAmountInserted = illegalContestAmountInserted;
    }

    @Override
    public String getMessage() {
        return "Failed to register to contest, the amount of contest that was chosen is illegal" +
                System.lineSeparator() + "The illegal amount that was chosen is: " + illegalContestAmountInserted +
                System.lineSeparator() + "The legal amount is: " + legalContestAmount;
    }
}

