package controller.ports;

import java.util.List;

import model.ActionDTO;
import model.EventDTO;
import model.bodies.AbstractBody;

public interface DomainEventProcesor {

    public void notifyNewProjectileFired(String entityId, String assetId);

    public List<ActionDTO> decideActions(AbstractBody entity, List<EventDTO> events);
}
