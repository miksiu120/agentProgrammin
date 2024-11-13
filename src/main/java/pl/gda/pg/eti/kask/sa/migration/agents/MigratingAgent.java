package pl.gda.pg.eti.kask.sa.migration.agents;

import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.mobility.MobilityOntology;
import java.util.List;
import javax.swing.JOptionPane;
import lombok.Getter;
import lombok.Setter;
import pl.gda.pg.eti.kask.sa.migration.behaviours.RequestContainersListBehaviour;

public class MigratingAgent extends Agent {

    @Setter
    @Getter
    private List<Location> locations;

    private Location previousLocation;

    private final SLCodec codec = new SLCodec();

    public MigratingAgent() {
    }

    @Override
    protected void setup() {
        super.setup();

        // Zarejestruj język i ontologię
        ContentManager cm = getContentManager();
        cm.registerLanguage(codec);
        cm.registerOntology(MobilityOntology.getInstance());

        // Dodaj zachowanie, które żąda listy kontenerów
        this.addBehaviour(new RequestContainersListBehaviour(this));
    }

    @Override
    protected void beforeMove() {
        previousLocation = here();  // Zapisujemy aktualną lokalizację przed migracją
        JOptionPane.showMessageDialog(null, "Odchodzę z kontenera: " + previousLocation.getName());
        super.beforeMove();
    }

    @Override
    protected void afterMove() {
        super.afterMove();
        Location currentLocation = here();  // Pobieramy nową lokalizację po migracji
        JOptionPane.showMessageDialog(null, "Przybywam do kontenera: " + currentLocation.getName());
    }
}