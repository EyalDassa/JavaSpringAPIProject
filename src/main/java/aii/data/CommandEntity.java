package aii.data;

import java.util.Date;
import java.util.Map;

import aii.Boundary.CommandId;
import aii.Boundary.ObjectId;
import aii.Boundary.TargetObject;
import aii.Boundary.UserId;
import aii.Utility.MapToStringConverter;
import aii.Boundary.CommandBoundary.InvokedBy;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "COMMANDS")
public class CommandEntity {
	@Id
	private String id;

    private String command;

	private String targetObject;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date invocationTimestamp;
	
	private String invokedBy;
	@Lob
	@Convert(converter = MapToStringConverter.class)
	private Map<String, Object> commandAttributes;
    
	public CommandEntity() {
	}
   
	public String getId() {
		return id;
	}

	public void setId(CommandId command) {
		this.id = consturctId(command.getId(), command.getSystemID());
	}

	private String consturctId(String id, String systemID) {
		return id + "@@" + systemID;
	}

	public CommandId generateCommandId() {
		String[] parts = this.id.split("@@");
	    return new CommandId( parts[1], parts[0]);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTargetObjectAsString() {
		return targetObject;
	}
	
	public TargetObject getTargetObject() {
		String[] parts = this.targetObject.split("@@");
		return new TargetObject(new ObjectId(parts[1], parts[0]));
	}

	public void setTargetObject(TargetObject targetObject) {
		this.targetObject = consturctId(targetObject.getObjectId().getId(),targetObject.getObjectId().getSystemID());
	}

	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}

	public String getInvokedByAsString() {
		return invokedBy;
	}
	
	public InvokedBy getInvokedBy() {
		String[] parts = this.invokedBy.split("@@");
		return new InvokedBy(new UserId(parts[0], parts[1]));
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = consturctId(invokedBy.getUserId().getSystemID(),invokedBy.getUserId().getEmail());
	}

	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

}
