/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import model.*;
import java.io.Serializable;


/**
 *
 * @author juanm
 */
public enum EngineState implements Serializable {
    STARTING,
    ALIVE,
    PAUSED,
    STOPPED
}
