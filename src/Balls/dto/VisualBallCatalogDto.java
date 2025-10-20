/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.dto;


import java.util.ArrayList;


/**
 *
 * @author juanm
 */
public class VisualBallCatalogDto {

    public int version;
    public ArrayList<VisualBallDto> visualBalls;


    public VisualBallCatalogDto(int version, ArrayList<VisualBallDto> visualBalls) {
        this.version = version;
        this.visualBalls = visualBalls;
    }
}
