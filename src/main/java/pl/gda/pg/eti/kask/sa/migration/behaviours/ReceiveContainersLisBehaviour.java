package pl.gda.pg.eti.kask.sa.migration.behaviours;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Result;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import lombok.extern.java.Log;
import pl.gda.pg.eti.kask.sa.migration.agents.MigratingAgent;

@Log
public class ReceiveContainersLisBehaviour extends Behaviour {

    private boolean done = false;
    protected final MigratingAgent myAgent;
    protected final String conversationId;
    protected MessageTemplate mt;

    public ReceiveContainersLisBehaviour(MigratingAgent agent, String conversationId) {
        super(agent);
        myAgent = agent;
        this.conversationId = conversationId;
    }

    @Override
    public void onStart() {
        super.onStart();
        mt = MessageTemplate.MatchConversationId(conversationId);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            done = true;
            try {
                ContentElement ce = myAgent.getContentManager().extractContent(msg);
                jade.util.leap.List items = ((Result) ce).getItems();
                List<Location> locations = new ArrayList<>();

                items.iterator().forEachRemaining(i -> {
                    locations.add((Location) i);
                });

                
                Location currentLocation = myAgent.here();
                if (locations.contains(currentLocation)) {
                    locations.remove(currentLocation);
                } else {
                    log.warning("Bieżący kontener nie istnieje na liście, odświeżam listę lokalizacji...");
                    myAgent.addBehaviour(new RequestContainersListBehaviour(myAgent));
                    return;
                }

                if (!locations.isEmpty()) {
                    Location nextLocation = locations.get(0);  // Wybór pierwszej dostępnej lokalizacji
                    myAgent.setLocations(locations);  // Aktualizacja listy lokalizacji
                    myAgent.addBehaviour(new MigratingBehaviour(myAgent));
                } else {
                    log.warning("Brak dostępnych kontenerów do migracji.");
                }

            } catch (Codec.CodecException | OntologyException ex) {
                log.log(Level.SEVERE, "Błąd podczas przetwarzania wiadomości: ", ex);
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return done;
    }
}
