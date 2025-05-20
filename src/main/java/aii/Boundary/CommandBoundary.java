package aii.Boundary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject;
    private Date invocationTimestamp;
    private InvokedBy invokedBy;
    private Map<String, Object> commandAttributes; // Updated to Map<String, Object>

    public CommandBoundary() {
        this.commandAttributes = new HashMap<>();
    }

    public CommandBoundary(CommandId commandId, String command, TargetObject targetObject, InvokedBy invokedBy) {
        this.commandId = commandId;
        this.command = command;
        this.targetObject = targetObject;
        this.invocationTimestamp = new Date();
        this.invokedBy = invokedBy;
        this.commandAttributes = new HashMap<>();
    }

    public CommandId getCommandId() {
        return commandId;
    }

    public void setCommandId(CommandId commandId) {
        this.commandId = commandId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public TargetObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
    }

    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public Map<String, Object> getCommandAttributes() { // Updated getter
        return commandAttributes;
    }

    public void setCommandAttributes(Map<String, Object> commandAttributes) { // Updated setter
        this.commandAttributes = commandAttributes;
    }

    @Override
    public String toString() {
        return "CommandBoundary{" +
                "commandId=" + commandId +
                ", command='" + command + '\'' +
                ", targetObject=" + targetObject +
                ", invocationTimestamp='" + invocationTimestamp + '\'' +
                ", invokedBy=" + invokedBy +
                ", commandAttributes=" + commandAttributes +
                '}';
    }

    public static class InvokedBy {
        private UserId userId;

        public InvokedBy() {
        }

        public InvokedBy(UserId userId) {
            this.userId = userId;
        }

        public UserId getUserId() {
            return userId;
        }

        public void setUserId(UserId userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "InvokedBy{" +
                    "userId=" + userId +
                    '}';
        }
    }
}
