package engine.model.bodies.ports;

import engine.model.bodies.core.AbstractBody;
import engine.model.physics.ports.PhysicsValuesMDTO;

public interface BodyEventProcessor {

    public void processBodyEvents(AbstractBody body, PhysicsValuesMDTO newPhyValues, PhysicsValuesMDTO oldPhyValues);

}
