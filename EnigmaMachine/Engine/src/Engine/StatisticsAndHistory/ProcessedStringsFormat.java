package Engine.StatisticsAndHistory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessedStringsFormat implements Serializable {
    private final String INDEX_IDENTATION = "                 ";
    private List<StringFormat> processedStringsFormat;
    private long encryptionTimeDurationNanoSeconds;
    private static final String sectorDelimiter = " --> ";
    private Integer formatIndex;
    private OriginalStringFormat originalStringFormat;
    private EncryptedStringFormat encryptedStringFormat;
    public ProcessedStringsFormat() {
        this.processedStringsFormat =new ArrayList<>();
        this.originalStringFormat = new OriginalStringFormat(new ArrayList<Character>());
        this.encryptedStringFormat = new EncryptedStringFormat(new ArrayList<Character>());
        this.encryptionTimeDurationNanoSeconds= 0;
    }

    public ProcessedStringsFormat(List<StringFormat> processedStringsFormat, long encryptionTimeDurationNanoSeconds, int formatIndex) {
        this.processedStringsFormat = processedStringsFormat;
        this.encryptionTimeDurationNanoSeconds = encryptionTimeDurationNanoSeconds;
        this.formatIndex = formatIndex;
    }
    public void addToOriginalString(Character character){
        originalStringFormat.add(character);
    }
    public void addToEncryptedString(Character processedString){
        encryptedStringFormat.add(processedString);
    }
    public void addStringToProcessedStringsFormat(StringFormat processedString){
        processedStringsFormat.add(processedString);
    }
    public void addToTotalTime(long time){
        encryptionTimeDurationNanoSeconds+=time;
    }
    @Override
    public String toString() {
        return INDEX_IDENTATION + formatIndex + ". " + processedStringsFormat.stream().map(Object::toString).collect(Collectors.joining(sectorDelimiter))
                + " (" + encryptionTimeDurationNanoSeconds + " nano-seconds)";
    }

    public void clear() {
        this.processedStringsFormat.clear();
    }

    public void addIndexFormat(int indexFormat) {
        this.formatIndex = indexFormat;
    }

    public long getTime() {
        return encryptionTimeDurationNanoSeconds;
    }

    public int getFormatIndex() {
        return formatIndex;
    }

    public List<StringFormat> getProcessedStringsFormat() {
        return processedStringsFormat;
    }

    public void setFormatIndex(int indexFormat) {
        this.formatIndex = indexFormat;
    }

    public StringFormat getOriginalString() {
        return originalStringFormat;
    }

    public StringFormat getEncryptedString() {
        return encryptedStringFormat;
    }

    public void addToStrings(StringFormat OriginalString, StringFormat EncryptedString) {
        for (Character character : OriginalString.toString().toCharArray()) {
            if(character != '<' && character != '>') {
                addToOriginalString(character);
            }
        }
        for (Character character : EncryptedString.toString().toCharArray()) {
            if(character != '<' && character != '>') {
                addToEncryptedString(character);
            }
        }
    }

    public void clearCurrentOriginalAndEncryptedStrings() {
        originalStringFormat.clear();
        encryptedStringFormat.clear();
    }
}
