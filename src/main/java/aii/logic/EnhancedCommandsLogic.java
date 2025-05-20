package aii.logic;
import java.util.List;
import aii.Boundary.CommandBoundary;

public interface EnhancedCommandsLogic extends CommandsService {
	public List<CommandBoundary> getAllCommands(String adminSystemID, String adminEmail,int page, int size);
}
