/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balls.model;


import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum BallState implements Serializable {
    STARTING,
    ALIVE,
    PAUSED,
    HANDS_OFF,
    DEAD
}
