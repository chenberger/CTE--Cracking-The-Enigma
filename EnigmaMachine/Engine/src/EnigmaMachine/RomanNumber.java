package EnigmaMachine;

import java.io.Serializable;

public enum RomanNumber implements Serializable {
    I(1), II(2), III(3),IV(4), V(5);

    private final int decimalRepresentation;

    RomanNumber(int decimalRepresentation) {
        this.decimalRepresentation = decimalRepresentation;
    }

    public static RomanNumber convertStringToRomanNumber(String romanNumberString){
        RomanNumber convertedRomanNumber = null;
        for(RomanNumber romanNumber: RomanNumber.values()){
            if(romanNumber.toString().equals(romanNumberString)){
                convertedRomanNumber = romanNumber;
                break;
            }
        }

        return convertedRomanNumber;
    }

    public static RomanNumber convertIntToRomanNumber(int intRepresentation) {
        RomanNumber convertedRomanNumber = null;
        for (RomanNumber romanNumber : RomanNumber.values()) {
            if (romanNumber.getDecimalRepresentation() == intRepresentation) {
                convertedRomanNumber =  romanNumber;
                break;
            }
        }

        return convertedRomanNumber;
    }

    public int getDecimalRepresentation() {
        return decimalRepresentation;
    }

    public static int minRomanValue() {
        return RomanNumber.I.decimalRepresentation;
    }

    public static int maxRomanValue() {
        return RomanNumber.V.decimalRepresentation;
    }
}
