package Operations;

import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class ProcessedStringsFormat {
    private final String INDEX_IDENTATION = "                 ";
    private List<Sector> processedStringsFormat;
    private long encryptionTimeDurationNanoSeconds;
    private static final String sectorDelimiter = " --> ";
    private Integer formatIndex;

    public ProcessedStringsFormat(List<Sector> processedStringsFormat, long encryptionTimeDurationNanoSeconds, int formatIndex) {
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
