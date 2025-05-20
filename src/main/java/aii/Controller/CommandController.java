package aii.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import aii.Boundary.CommandBoundary;
import aii.logic.EnhancedCommandsLogic;


@Tag(name = "Commands API")
@RestController
@RequestMapping("/aii/commands")
public class CommandController {
	
	private EnhancedCommandsLogic commands;
    public CommandController(EnhancedCommandsLogic commands) {
    	this.commands = commands;
    }

    public void deleteAllCommands(String adminSystemID, String adminEmail) {
        this.commands.deleteAllCommands(adminSystemID,adminEmail);
        System.out.println("All commands history has been deleted");
    }
    public CommandBoundary[] exportAllCommands(String adminSystemID, String adminEmail,int page,int size) {
        return this.commands.getAllCommands(adminSystemID,adminEmail,page, size).toArray(new CommandBoundary[0]); 
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public Object[] invokeCommand(@RequestBody CommandBoundary commandBoundary) {
    	
    	return this.commands.invokeCommand(commandBoundary).toArray(new Object[0]);
    }
}
