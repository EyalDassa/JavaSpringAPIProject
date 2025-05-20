package aii;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;

import aii.Boundary.CommandBoundary;
import aii.Boundary.CommandId;
import aii.Boundary.CreatedBy;
import aii.Boundary.Location;
import aii.Boundary.NewUserBoundary;
import aii.Boundary.ObjectBoundary;
import aii.Boundary.ObjectId;
import aii.Boundary.TargetObject;
import aii.Boundary.UserBoundary;
import aii.Boundary.UserId;
import aii.Boundary.CommandBoundary.InvokedBy;
import aii.data.RoleEnum;

public class CommandsTests extends ApplicationTests{
	
	//Tests commands  
	@Test
	@DisplayName("Test Successful Command Invocation by END_USER")
	public void testSuccessfulCommandInvocationByEndUser() {
	    // GIVEN A valid END_USER 
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);
	 // AND A valid OPERATOR 
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);
	 // AND a valid active object
	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // WHEN The END_USER invokes a valid command
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    CommandBoundary[] result = this.commandRestClient.post()
	            .body(command)
	            .retrieve()
	            .body(CommandBoundary[].class);

	    // THEN Validate the response
	    assertThat(result).isNotEmpty();
	    CommandBoundary resultCommand = result[0];
	    assertThat(resultCommand.getCommand()).isEqualTo("Turn on the lamp");
	    assertThat(resultCommand.getTargetObject().getObjectId()).isEqualTo(createdObject.getObjectId());
	    assertThat(resultCommand.getInvokedBy().getUserId()).isEqualTo(endUser.getUserId());
	    assertThat(resultCommand.getCommandAttributes()).containsEntry("brightness", "high");
	}
	@Test
	@DisplayName("Test ADMIN Cannot Invoke Command")
	public void testAdminCannotInvokeCommand() {
	    // GIVEN A valid ADMIN user
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    try {
	        // WHEN The ADMIN attempts to invoke a command
	        CommandBoundary command = new CommandBoundary(
	                null,
	                "Turn on the lamp",
	                new TargetObject(createdObject.getObjectId()),
	                new InvokedBy(admin.getUserId())
	        );
	        command.setCommandAttributes(Map.of("brightness", "high"));

	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("ADMIN user was able to invoke a command").isTrue();
	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
	        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test OPERATOR Cannot Invoke Command")
	public void testOperatorCannotInvokeCommand() {
	    // GIVEN A valid OPERATOR user
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid active object
	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    try {
	        // WHEN The OPERATOR attempts to invoke a command
	        CommandBoundary command = new CommandBoundary(
	                null,
	                "Turn on the lamp",
	                new TargetObject(createdObject.getObjectId()),
	                new InvokedBy(operator.getUserId())
	        );
	        command.setCommandAttributes(Map.of("brightness", "high"));

	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("OPERATOR user was able to invoke a command").isTrue();
	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
	        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test Empty Command Invocation by END_USER")
	public void testEmptyCommandInvocationByEndUser() {
	    // GIVEN A valid END_USER 
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);
	 // AND A valid OPERATOR 
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);
	 // AND a valid active object
	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // WHEN The END_USER invokes a invalid command with empty command
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    try {
	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("Empty command (String)  was accepted by the system").isTrue();

	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 400 BAD REQUEST is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
	        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
	    }
	    
	}
	@Test
	@DisplayName("Test Null Command Invocation by END_USER")
	public void testNullCommandInvocationByEndUser() {
	    // GIVEN A valid END_USER 
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR 
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND a valid active object
	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // WHEN The END_USER attempts to invoke an invalid command with null command
	    CommandBoundary command = new CommandBoundary(
	            null,
	            null, // Invalid: null command
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    try {
	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("Null command was accepted by the system").isTrue();

	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 400 BAD REQUEST is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
	        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test Non-Existent TargetObject Invocation by END_USER")
	public void testNonExistentTargetObjectInvocationByEndUser() {
	    // GIVEN A valid END_USER
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // WHEN The END_USER attempts to invoke a command with a non-existent TargetObject
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(new ObjectId("nonExistentId", "2025a.integrative.nagar.yuval")), // Invalid: TargetObject does not exist
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    try {
	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("Command with non-existent TargetObject was accepted by the system").isTrue();

	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 404 NOT FOUND is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(404); // Ensure HTTP status is 404
	        assertThat(ex.getStatusText()).isEqualTo("Not Found"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test Inactive TargetObject Invocation by END_USER")
	public void testInactiveTargetObjectInvocationByEndUser() {
	    // GIVEN A valid END_USER
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an inactive object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary inactiveObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), false, new CreatedBy(operator.getUserId()), null)) // Active = false
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // WHEN The END_USER attempts to invoke a command on the inactive TargetObject
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(inactiveObject.getObjectId()), // TargetObject is inactive
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    try {
	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("Command with inactive TargetObject was accepted by the system").isTrue();

	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 404 NOT FOUND is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(404); // Ensure HTTP status is 404
	        assertThat(ex.getStatusText()).isEqualTo("Not Found"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test Command Invocation with Null TargetObject by END_USER")
	public void testCommandInvocationWithNullTargetObjectByEndUser() {
	    // GIVEN A valid END_USER
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // WHEN The command is invoked with a null TargetObject
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            null, // Invalid: Null TargetObject
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    try {
	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("Command with null TargetObject was accepted by the system").isTrue();

	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 400 BAD REQUEST is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
	        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test Command Invocation by Non-Existent InvokedBy User")
	public void testCommandInvocationByNonExistentInvokedByUser() {
	    // GIVEN A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // WHEN The command is invoked by a non-existent user
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(new UserId("2025a.integrative.nagar.yuval", "nonexistent-user@gmail.com")) // Invalid: Non-existent user
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    try {
	        this.commandRestClient.post()
	                .body(command)
	                .retrieve()
	                .body(CommandBoundary[].class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("Command by a non-existent user was accepted by the system").isTrue();

	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 404 NOT FOUND is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(404); // Ensure HTTP status is 404
	        assertThat(ex.getStatusText()).isEqualTo("Not Found"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test Admin Exports All Commands")
	public void testAdminExportsAllCommands() {
	    // GIVEN A valid ADMIN user
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid END_USER creates three commands
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // Create three valid commands by the END_USER
	    CommandBoundary command1 = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command1.setCommandAttributes(Map.of("brightness", "high"));

	    CommandBoundary command2 = new CommandBoundary(
	            null,
	            "Dim the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command2.setCommandAttributes(Map.of("brightness", "low"));

	    CommandBoundary command3 = new CommandBoundary(
	            null,
	            "Turn off the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command3.setCommandAttributes(Map.of("status", "off"));

	    // POST the commands to the system
	    this.commandRestClient.post().body(command1).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command2).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command3).retrieve().body(CommandBoundary[].class);

	    // WHEN The ADMIN user exports all commands
	    CommandBoundary[] exportedCommands = this.adminRestClient
	            .get()
	            .uri("/commands?userSystemID={systemID}&userEmail={email}&page={page}&size={size}",
	                    admin.getUserId().getSystemID(),
	                    admin.getUserId().getEmail(),
	                    0, // Page
	                    10 // Size
	            )
	            .retrieve()
	            .body(CommandBoundary[].class);

	    // THEN Verify the exported commands
	    assertThat(exportedCommands).hasSize(3); // Ensure all three commands are exported
	    assertThat(exportedCommands)
	            .extracting("command")
	            .containsExactlyInAnyOrder("Turn on the lamp", "Dim the lamp", "Turn off the lamp"); // Verify commands match
	    assertThat(exportedCommands)
	            .extracting("commandAttributes")
	            .containsExactlyInAnyOrder(
	                    Map.of("brightness", "high"),
	                    Map.of("brightness", "low"),
	                    Map.of("status", "off")
	            ); // Verify attributes match
	}
	@Test
	@DisplayName("Test Export All Commands with Page 1 and Size 4 Returns Empty List")
	public void testExportAllCommandsWithPage1AndSize4ReturnsEmptyList() {
	    // GIVEN A valid ADMIN user
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid END_USER creates three commands
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // Create three valid commands by the END_USER
	    CommandBoundary command1 = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command1.setCommandAttributes(Map.of("brightness", "high"));

	    CommandBoundary command2 = new CommandBoundary(
	            null,
	            "Dim the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command2.setCommandAttributes(Map.of("brightness", "low"));

	    CommandBoundary command3 = new CommandBoundary(
	            null,
	            "Turn off the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command3.setCommandAttributes(Map.of("status", "off"));

	    // POST the commands to the system
	    this.commandRestClient.post().body(command1).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command2).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command3).retrieve().body(CommandBoundary[].class);

	    // WHEN The ADMIN user exports commands with page=1 and size=4
	    CommandBoundary[] exportedCommands = this.adminRestClient
	            .get()
	            .uri("/commands?userSystemID={systemID}&userEmail={email}&page={page}&size={size}",
	                    admin.getUserId().getSystemID(),
	                    admin.getUserId().getEmail(),
	                    1, // Page 1
	                    4  // Size 4
	            )
	            .retrieve()
	            .body(CommandBoundary[].class);

	    // THEN Verify the response is an empty list
	    assertThat(exportedCommands).isEmpty(); // Ensure no commands are returned
	}
	@Test
	@DisplayName("Test Export All Commands with Page 1 and Size 2 Returns One Command")
	public void testExportAllCommandsWithPage1AndSize2ReturnsOneCommand() {
	    // GIVEN A valid ADMIN user
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid END_USER creates three commands
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // Create three valid commands by the END_USER
	    CommandBoundary command1 = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command1.setCommandAttributes(Map.of("brightness", "high"));

	    CommandBoundary command2 = new CommandBoundary(
	            null,
	            "Dim the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command2.setCommandAttributes(Map.of("brightness", "low"));

	    CommandBoundary command3 = new CommandBoundary(
	            null,
	            "Turn off the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command3.setCommandAttributes(Map.of("status", "off"));

	    // POST the commands to the system
	    this.commandRestClient.post().body(command1).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command2).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command3).retrieve().body(CommandBoundary[].class);

	    // WHEN The ADMIN user exports commands with page=1 and size=2
	    CommandBoundary[] exportedCommands = this.adminRestClient
	            .get()
	            .uri("/commands?userSystemID={systemID}&userEmail={email}&page={page}&size={size}",
	                    admin.getUserId().getSystemID(),
	                    admin.getUserId().getEmail(),
	                    1, // Page 1
	                    2  // Size 2
	            )
	            .retrieve()
	            .body(CommandBoundary[].class);

	    // THEN Verify the response contains only one command (the third command)
	    assertThat(exportedCommands).hasSize(1); // Ensure only one command is returned
	}
	@Test
	@DisplayName("Test Admin Deletes All Commands and Exports an Empty List")
	public void testAdminDeletesAllCommandsAndExportsEmptyList() {
	    // GIVEN A valid ADMIN user
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid END_USER creates three commands
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // Create three valid commands by the END_USER
	    CommandBoundary command1 = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command1.setCommandAttributes(Map.of("brightness", "high"));

	    CommandBoundary command2 = new CommandBoundary(
	            null,
	            "Dim the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command2.setCommandAttributes(Map.of("brightness", "low"));

	    CommandBoundary command3 = new CommandBoundary(
	            null,
	            "Turn off the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command3.setCommandAttributes(Map.of("status", "off"));

	    // POST the commands to the system
	    this.commandRestClient.post().body(command1).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command2).retrieve().body(CommandBoundary[].class);
	    this.commandRestClient.post().body(command3).retrieve().body(CommandBoundary[].class);

	    // WHEN The ADMIN user deletes all commands
	    this.adminRestClient
	            .delete()
	            .uri("/commands?userSystemID={systemID}&userEmail={email}",
	                    admin.getUserId().getSystemID(),
	                    admin.getUserId().getEmail()
	            )
	            .retrieve()
	            .body(Void.class);

	    // AND The ADMIN user exports commands
	    CommandBoundary[] exportedCommands = this.adminRestClient
	            .get()
	            .uri("/commands?userSystemID={systemID}&userEmail={email}&page={page}&size={size}",
	                    admin.getUserId().getSystemID(),
	                    admin.getUserId().getEmail(),
	                    0, // Page 0
	                    10 // Size 10
	            )
	            .retrieve()
	            .body(CommandBoundary[].class);

	    // THEN Verify the exported commands list is empty
	    assertThat(exportedCommands).isEmpty(); // Ensure no commands are returned
	}
	@Test
	@DisplayName("Test OPERATOR Cannot Delete All Commands")
	public void testOperatorCannotDeleteAllCommands() {
	    // GIVEN A valid OPERATOR user
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid ADMIN user to create commands
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid END_USER to create commands
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid active object
	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // Create a valid command by the END_USER
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    // POST the command to the system
	    this.commandRestClient.post().body(command).retrieve().body(CommandBoundary[].class);

	    // WHEN The OPERATOR tries to delete all commands
	    try {
	        this.adminRestClient
	                .delete()
	                .uri("/commands?userSystemID={systemID}&userEmail={email}",
	                        operator.getUserId().getSystemID(),
	                        operator.getUserId().getEmail()
	                )
	                .retrieve()
	                .body(Void.class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("OPERATOR was able to delete all commands").isTrue();
	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
	        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
	    }
	}
	@Test
	@DisplayName("Test END_USER Cannot Delete All Commands")
	public void testEndUserCannotDeleteAllCommands() {
	    // GIVEN A valid END_USER user
	    UserBoundary endUser = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid ADMIN user to create commands
	    UserBoundary admin = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
	            .retrieve()
	            .body(UserBoundary.class);

	    // AND A valid OPERATOR creates an active object
	    UserBoundary operator = this.userRestClient
	            .post()
	            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
	            .retrieve()
	            .body(UserBoundary.class);

	    ObjectBoundary createdObject = this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "lamp", "room1", "off", new Location(0.1, 0.1), true, new CreatedBy(operator.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

	    // Create a valid command by the END_USER
	    CommandBoundary command = new CommandBoundary(
	            null,
	            "Turn on the lamp",
	            new TargetObject(createdObject.getObjectId()),
	            new InvokedBy(endUser.getUserId())
	    );
	    command.setCommandAttributes(Map.of("brightness", "high"));

	    // POST the command to the system
	    this.commandRestClient.post().body(command).retrieve().body(CommandBoundary[].class);

	    // WHEN The END_USER tries to delete all commands
	    try {
	        this.adminRestClient
	                .delete()
	                .uri("/commands?userSystemID={systemID}&userEmail={email}",
	                        endUser.getUserId().getSystemID(),
	                        endUser.getUserId().getEmail()
	                )
	                .retrieve()
	                .body(Void.class);

	        // THEN the operation should fail (this point should not be reached)
	        assertThat(false).as("END_USER was able to delete all commands").isTrue();
	    } catch (HttpClientErrorException ex) {
	        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
	        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
	        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
	    }
	}
}
