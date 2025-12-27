package model.bodies;


import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum BodyState implements Serializable {
    STARTING,
    ALIVE,
    PAUSED,
    HANDS_OFF,
    DEAD
}
