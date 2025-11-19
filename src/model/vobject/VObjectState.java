
package model.vobject;


import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum VObjectState implements Serializable {
    STARTING,
    ALIVE,
    PAUSED,
    HANDS_OFF,
    DEAD
}
