package MainScene.ChatTabPane.chatarea.model;

import java.util.List;

public class ChatLinesWithVersion {

    private int version;
    private List<ChatTabPane.chatarea.model.SingleChatLine> entries;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ChatTabPane.chatarea.model.SingleChatLine> getEntries() {
        return entries;
    }

    public void setEntries(List<ChatTabPane.chatarea.model.SingleChatLine> entries) {
        this.entries = entries;
    }
}
