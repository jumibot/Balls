package model;


import model.bodies.AbstractBody;


public class EventDTO {

    public final AbstractBody entity;
    public final EventType eventType;
    
    public EventDTO(AbstractBody entity, EventType eventType){
        this.entity = entity;
        this.eventType = eventType;
    }
}
