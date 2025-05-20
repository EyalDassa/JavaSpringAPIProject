
package aii.Controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aii.Boundary.CommandBoundary;
import aii.Boundary.UserBoundary;

@RestController
@RequestMapping("/aii/admin")
public class AdminController {
    
    private final UserController userController;
    private final ObjectController objectController;
    private final CommandController commandController;
    
    //@Autowired
    public AdminController(UserController userController,ObjectController objectController,CommandController commandController) {
        this.userController = userController;
        this.objectController= objectController;
        this.commandController=commandController;
        
    }

    //Delete all users
    
    @DeleteMapping(path = "/users")
    public void deleteAllUsers(@RequestParam(name="userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email) {
    	userController.deleteAllUsers(systemID,email);
    }
    
    //Delete all objects
    
    @DeleteMapping(path = "/objects")
    public void deleteAllObjects(@RequestParam(name="userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email) {
        objectController.deleteAllObjects(systemID,email);
    }
    
    // Delete all commands
    
    @DeleteMapping(path = "/commands")
    public void deleteAllCommands(@RequestParam(name="userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email) {
        commandController.deleteAllCommands(systemID,email);
    }
    // export all users
    
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] exportAllUsers(@RequestParam(name="userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    	return userController.exportAllUsers(systemID,email,page,size);     
    }
    // export all commands history
    
    
    @GetMapping(path = "/commands", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandBoundary[] exportAllCommands(@RequestParam(name="userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return commandController.exportAllCommands(systemID,email,page,size);
    }
    
}
