package pl.gda.pg.eti.kask.sa.migration.behaviours;

import jade.core.Location;
import jade.core.behaviours.Behaviour;
import java.util.Random;
import pl.gda.pg.eti.kask.sa.migration.agents.MigratingAgent;

public class MigratingBehaviour extends Behaviour {

    protected final MigratingAgent myAgent;
    private final Random random = new Random();

    public MigratingBehaviour(MigratingAgent agent) {
        super(agent);
        myAgent = agent;
    }

    @Override
    public void action() {
        Location location = myAgent.getLocations().get(0);
        myAgent.getLocations().remove(location);
        myAgent.getLocations().add(location);
        myAgent.doMove(location);

    }

    @Override
    public boolean done() {
        // Ustaw done() na false, aby zachowanie trwało wiecznie
        return false;
    }
}
