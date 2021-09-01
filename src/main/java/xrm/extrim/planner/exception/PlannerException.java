package xrm.extrim.planner.exception;

public class PlannerException extends RuntimeException {
    private static final long serialVersionUID = -4505819924771635158L;
    private String plannerMessage;

    public PlannerException(String message) {
        super(message);
        this.plannerMessage = message;
    }

    public PlannerException(Throwable cause, String plannerMessage) {
        super(cause);
        this.plannerMessage = plannerMessage;
    }

    public String getPlannerMessage() {
        return plannerMessage;
    }
}
