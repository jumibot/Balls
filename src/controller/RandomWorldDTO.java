package controller;


import _helpers.DoubleVector;


public class RandomWorldDTO {

    public int maxCreationDelay;
    public RandomDBodyDTO dBodyParams;
    public RandomSBodyDTO sBodyParams;


    public RandomWorldDTO(
            int maxCreationDelay,
            RandomDBodyDTO dBodyParams,
            RandomSBodyDTO sBodyParams) {

        this.maxCreationDelay = maxCreationDelay;
        this.dBodyParams = dBodyParams;
        this.sBodyParams = sBodyParams;
    }
}
