package Engine.StatisticsAndHistory;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessedStringsFormat implements Serializable {
    private final String INDEX_IDENTATION = "                 ";
    private List<StringFormat> processedStringsFormat;
    private long encryptionTimeDurationNanoSeconds;
    private static final String sectorDelimiter = " --> ";
    private Integer formatIndex;

    public ProcessedStringsFormat(List<StringFormat> processedStringsFormat, long encryptionTimeDurationNanoSeconds, int formatIndex) {
        this.processedStringsFormat = processedStringsFormat;
        this.encryptionTimeDurationNanoSeconds = encryptionTimeDurationNanoSeconds;
        this.formatIndex = formatIndex;
    }

    @Override
    public String toString() {
        return INDEX_IDENTATION + formatIndex + ". " + processedStringsFormat.stream().map(Object::toString).collect(Collectors.joining(sectorDelimiter))
                + " (" + encryptionTimeDurationNanoSeconds + " nano-seconds)";
    }
}
