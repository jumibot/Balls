package model;


import model.entities.AbstractEntity;


public class EventDTO {

    public final AbstractEntity entity;
    public final EventType eventType;
    
    public EventDTO(AbstractEntity entity, EventType eventType){
        this.entity = entity;
        this.eventType = eventType;
    }
}
