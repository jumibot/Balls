package controller.ports;

import java.util.List;

import model.bodies.core.AbstractBody;
import model.ports.ActionDTO;
import model.ports.EventDTO;

public interface DomainEventProcesor {

    public void notifyNewProjectileFired(String entityId, String assetId);

    public List<ActionDTO> decideActions(AbstractBody entity, List<EventDTO> events);
}
