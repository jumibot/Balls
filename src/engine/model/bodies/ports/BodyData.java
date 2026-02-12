package engine.model.bodies.ports;

import engine.model.physics.ports.PhysicsValuesMDTO;

public class BodyData {

    // region Fields
    public final String entityId;
    public final BodyType type;
    private PhysicsValuesMDTO physicsValues;
    // endregion

    // region Constructors
    public BodyData(String entityId, BodyType type, PhysicsValuesMDTO phyValues) {
        this.entityId = entityId;
        this.type = type;
        this.physicsValues = phyValues;
    }
    // endregion

    // *** PUBLICS ***

    public PhysicsValuesMDTO getPhysicsValues() {
        return physicsValues;
    }

    public void setPhysicsValues(PhysicsValuesMDTO physicsValues) {
        this.physicsValues = physicsValues;
    }

}
