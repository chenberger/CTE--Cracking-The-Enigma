package EnigmaMachine;

public enum RomanNumber {
    I, IV, V, II, III;

    public static RomanNumber convertStringToRomanNumber(String romanNumberString){
        RomanNumber convertedRomanNumber = null;
        for(RomanNumber romanNumber: RomanNumber.values()){
            if(romanNumber.toString().equals(romanNumberString)){
                convertedRomanNumber = romanNumber;
            }
        }
        return convertedRomanNumber;
    }


}
