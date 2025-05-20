package aii.Controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import aii.Boundary.NewUserBoundary;
import aii.Boundary.UserBoundary;
import aii.Boundary.UserId;
import aii.Exceptions.UserNotFoundException;
import aii.logic.EnhancedUsersLogic;

@RestController
@RequestMapping("/aii/users")
public class UserController {

	
	private EnhancedUsersLogic userService;

	public UserController(EnhancedUsersLogic userService) {
		this.userService = userService;
	}

	// Create a new user
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary insertToDb(@RequestBody NewUserBoundary newUser) {
		return userService.createUser(new UserBoundary(new UserId("", newUser.getEmail()), newUser));

	}

	@GetMapping(path = "/login/{systemID}/{userEmail}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary loginUser(@PathVariable("systemID") String systemId,
			@PathVariable("userEmail") String userEmail) {
		return userService.login(systemId, userEmail)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	@PutMapping(path = "/{systemID}/{userEmail}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void updateUser(@PathVariable("systemID") String systemId,
			@PathVariable("userEmail") String userEmail, @RequestBody UserBoundary update) {
		userService.updateUser(systemId, userEmail, update);
	}

//	@DeleteMapping
    public void deleteAllUsers(String adminSystemID, String adminEmail) {
        // Use admin credentials if needed in UserService
        userService.deleteAllUsers(adminSystemID,adminEmail);
    }

    // Export all users
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserBoundary[] exportAllUsers(String adminSystemID, String adminEmail,int page,int size) {
        return userService.getAllUsers(adminSystemID,adminEmail,page,size).toArray(new UserBoundary[0]);
    }
}
