package DesktopUserInterface.MainScene.BodyScene.BruteForce;

import BruteForce.DecipherStatistics;

import java.util.Objects;

public class AgentTaskData {
    private final DecipherStatistics decipherStatistics;
    private final Integer id;

    public AgentTaskData(DecipherStatistics decipherStatistics, Integer id) {
        this.decipherStatistics = decipherStatistics;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getCandidates() {
        return decipherStatistics.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgentTaskData)) return false;
        AgentTaskData that = (AgentTaskData) o;
        return decipherStatistics.equals(that.decipherStatistics) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decipherStatistics, id);
    }
}
