package model.ports;

public class ActionDTO {
    final public ActionType type;
    final public ActionExecutor executor;
    final public ActionPriority priority;

    public ActionDTO(ActionType type, ActionExecutor executor, ActionPriority priority) {
        this.type = type;
        this.executor = executor;
        this.priority = priority;
    }
}