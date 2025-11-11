/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum VObjectEventType implements Serializable {
    COLLIDED,
    NORTH_LIMIT_REACHED,
    SOUTH_LIMIT_REACHED,
    EAST_LIMIT_REACHED,
    WEST_LIMIT_REACHED,
    NONE
}
