package aii.logic;

import java.util.List;

import aii.Boundary.CommandBoundary;

public interface CommandsService {
	public List<Object> invokeCommand(CommandBoundary command);
	@Deprecated
	public List<CommandBoundary> getAllCommands(String adminSystemID, String adminEmail);
	public void deleteAllCommands(String adminSystemID, String adminEmail);
}
