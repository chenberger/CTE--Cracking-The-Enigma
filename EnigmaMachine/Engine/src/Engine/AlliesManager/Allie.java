package Engine.AlliesManager;

import BruteForce.Agent;
import BruteForce.DecryptionManager;

import java.util.ArrayList;
import java.util.List;

public class Allie {
    private String Name;
    private final List<Agent> agents;
    private DecryptionManager decryptionManager;

    public Allie(String name){
        Name = name;
        agents = new ArrayList<>();
        decryptionManager = new DecryptionManager();
    }
}
