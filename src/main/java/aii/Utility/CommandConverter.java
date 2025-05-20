package aii.Utility;

import org.springframework.stereotype.Component;

import aii.Boundary.CommandBoundary;
import aii.data.CommandEntity;

@Component
public class CommandConverter {
	public CommandEntity toEntity (CommandBoundary boundary) {
		CommandEntity entity = new CommandEntity();
		entity.setId(boundary.getCommandId());	
		entity.setCommand(boundary.getCommand());
		entity.setTargetObject(boundary.getTargetObject());
		entity.setInvocationTimestamp(boundary.getInvocationTimestamp());
		entity.setInvokedBy(boundary.getInvokedBy());
		entity.setCommandAttributes(boundary.getCommandAttributes());
		
		return entity;	
	}
	
	public CommandBoundary toBoundary (CommandEntity entity) {
		CommandBoundary boundary = new CommandBoundary();
    	boundary.setCommandId(entity.generateCommandId());
    	boundary.setCommand(entity.getCommand());
    	boundary.setTargetObject(entity.getTargetObject());
    	boundary.setInvocationTimestamp(entity.getInvocationTimestamp());
    	boundary.setInvokedBy(entity.getInvokedBy());
    	boundary.setCommandAttributes(entity.getCommandAttributes());
    	return boundary;
	}

}
