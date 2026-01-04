package model.bodies.core;

import model.physics.ports.PhysicsValuesDTO;

public class BodyDTO {
    public final String entityId;
    public final PhysicsValuesDTO physicsValues;

    public BodyDTO(String entityId, PhysicsValuesDTO phyValues) {
        this.entityId = entityId;
        this.physicsValues = phyValues;
    }

}
