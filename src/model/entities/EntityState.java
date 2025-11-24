
package model.entities;


import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum EntityState implements Serializable {
    STARTING,
    ALIVE,
    PAUSED,
    HANDS_OFF,
    DEAD
}
