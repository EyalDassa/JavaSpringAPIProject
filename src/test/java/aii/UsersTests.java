package aii;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;

import aii.Boundary.NewUserBoundary;
import aii.Boundary.UserBoundary;
import aii.Boundary.UserId;
import aii.data.RoleEnum;

public class UsersTests extends ApplicationTests {
	
	//Tests users from admin controller 
		@Test
		@DisplayName("Test Getting All Users as Admin Returns the Correct List")
		public void testGettingAllUsersAsAdmin() throws Exception {
		    // GIVEN the server contains a User with email "admin@gmail.com" and role ADMIN
		    UserBoundary adminUser = this.userRestClient
		        .post()
		        .body(new NewUserBoundary("admin@gmail.com", "jane", "0-0", RoleEnum.ADMIN))
		        .retrieve()
		        .body(UserBoundary.class);

		    // AND the server contains users with role END_USER
		    List<UserBoundary> expectedUsers = new ArrayList<>();
		    expectedUsers.add(adminUser); // Admin user is also in the expected list
		    
		    UserBoundary user1 = this.userRestClient
		        .post()
		        .body(new NewUserBoundary("endUser1@gmail.com", "endUser1", "0-0", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);
		    expectedUsers.add(user1);

		    UserBoundary user2 = this.userRestClient
		        .post()
		        .body(new NewUserBoundary("endUser2@gmail.com", "endUser2", "0-0", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);
		    expectedUsers.add(user2);

		    // WHEN I get the users list with admin credentials
		    UserBoundary[] actualUsers = this.adminRestClient
		        .get()
		        .uri("/users?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
		        		adminUser.getUserId().getSystemID(), adminUser.getUserId().getEmail(), 0, 10)
		        .retrieve()
		        .body(UserBoundary[].class);

		    // THEN the server returns all users including the admin and the two end users
		    assertThat(actualUsers)
		        .usingRecursiveFieldByFieldElementComparator()
		        .containsExactlyInAnyOrderElementsOf(expectedUsers);
		}
		@Test
		@DisplayName("Test Admin Deletes All Users and Gets Only Their Own User")
		public void testAdminDeletesAllUsersAndGetsOnlyTheirOwnUser() throws Exception {
		    // GIVEN the server contains an admin user and two end users
		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("admin@gmail.com", "admin", "admin-avatar", RoleEnum.ADMIN))
		        .retrieve()
		        .body(UserBoundary.class);

		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("user1@gmail.com", "user1", "avatar1", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);

		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("user2@gmail.com", "user2", "avatar2", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);

		    // WHEN the admin deletes all users
		    this.adminRestClient
		        .delete()
		        .uri("/users?userSystemID={systemID}&userEmail={email}", 
		             "2025a.integrative.nagar.yuval", "admin@gmail.com")
		        .retrieve()
		        .body(Void.class);

		    // AND the admin adds their user back and tries to export the users list
		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("admin@gmail.com", "admin", "admin-avatar", RoleEnum.ADMIN))
		        .retrieve()
		        .body(UserBoundary.class);

		    UserBoundary[] actualUsers = this.adminRestClient
		        .get()
		        .uri("/users?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
		             "2025a.integrative.nagar.yuval", "admin@gmail.com", 0, 10)
		        .retrieve()
		        .body(UserBoundary[].class);

		    // THEN the list contains only the admin user
		    assertThat(actualUsers)
		        .hasSize(1)
		        .allMatch(user -> user.getUserId().getEmail().equals("admin@gmail.com"));
		}
		@Test
		@DisplayName("Test Unauthorized Deletion of All Users by END USER")
		public void testUnauthorizedDeletionOfAllUsersByEndUser() throws Exception {
		    // GIVEN the server contains a non-admin user with role END_USER
		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("user1@gmail.com", "user1", "avatar1", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);

		    // WHEN the non-admin user attempts to delete all users
		    try {
		        this.adminRestClient
		            .delete()
		            .uri("/users?userSystemID={systemID}&userEmail={email}", 
		                 "2025a.integrative.nagar.yuval", "user1@gmail.com")
		            .retrieve()
		            .body(Void.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Non-admin user was able to delete all users").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
		        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
		        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test Unauthorized Export of Users by END USER")
		public void testUnauthorizedExportOfUsersByEndUser() throws Exception {
		    // GIVEN the server contains a non-admin user with role END_USER
		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("user1@gmail.com", "user1", "avatar1", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);

		    // WHEN the non-admin user attempts to export the users list
		    try {
		        this.adminRestClient
		            .get()
		            .uri("/users?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
		                 "2025a.integrative.nagar.yuval", "user1@gmail.com", 0, 10)
		            .retrieve()
		            .body(UserBoundary[].class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Non-admin user was able to export the users list").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
		        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
		        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test Unauthorized Deletion of All Users by OPERATOR")
		public void testUnauthorizedDeletionOfAllUsersByOPERATOR() throws Exception {
		    // GIVEN the server contains a non-admin user with role OPERATOR
		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("user1@gmail.com", "user1", "avatar1", RoleEnum.OPERATOR))
		        .retrieve()
		        .body(UserBoundary.class);

		    // WHEN the non-admin user attempts to delete all users
		    try {
		        this.adminRestClient
		            .delete()
		            .uri("/users?userSystemID={systemID}&userEmail={email}", 
		                 "2025a.integrative.nagar.yuval", "user1@gmail.com")
		            .retrieve()
		            .body(Void.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Non-admin user was able to delete all users").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
		        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
		        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test Unauthorized Export of Users by OPERATOR")
		public void testUnauthorizedExportOfUsersByOPERATOR() throws Exception {
		    // GIVEN the server contains a non-admin user with role END_USER
		    this.userRestClient
		        .post()
		        .body(new NewUserBoundary("user1@gmail.com", "user1", "avatar1", RoleEnum.OPERATOR))
		        .retrieve()
		        .body(UserBoundary.class);

		    // WHEN the non-admin user attempts to export the users list
		    try {
		        this.adminRestClient
		            .get()
		            .uri("/users?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
		                 "2025a.integrative.nagar.yuval", "user1@gmail.com", 0, 10)
		            .retrieve()
		            .body(UserBoundary[].class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Non-admin user was able to export the users list").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
		        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
		        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
		    }
		}
	//tests from users controller
		@Test
		@DisplayName("Test Valid User Creation and User Exists in Database")
		public void testValidUserCreation() throws Exception {
		    // GIVEN a valid end user
		    NewUserBoundary newUser = new NewUserBoundary("validuser@gmail.com", "validuser", "avatar1", RoleEnum.END_USER);
		    
		    // AND create the user and store the expected result
		    UserBoundary expected = this.userRestClient
		        .post()
		        .body(newUser)
		        .retrieve()
		        .body(UserBoundary.class);

		    // WHEN the user tries to retrieve their user
		    UserBoundary retrievedUser = this.userRestClient
		        .get()
		        .uri("/login/{systemID}/{userEmail}", "2025a.integrative.nagar.yuval", "validuser@gmail.com")
		        .retrieve()
		        .body(UserBoundary.class);   

		    // THEN the retrieved user matches the expected user
		    assertThat(retrievedUser)
		        .usingRecursiveComparison()
		        .isEqualTo(expected);
		}
		@Test
		@DisplayName("Test User Creation with Invalid Email")
		public void testUserCreationWithInvalidEmail() throws Exception {
		    // GIVEN a user boundary with an invalid email
		    NewUserBoundary invalidUser = new NewUserBoundary("invalid-email", "user", "avatar1", RoleEnum.END_USER);

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .body(invalidUser)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation with invalid email was accepted").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		        
		    }
		    
		}
		@Test
		@DisplayName("Test User Creation with Null Username")
		public void testUserCreationWithNullUsername() throws Exception {
		    // GIVEN a user boundary with a null username
		    NewUserBoundary invalidUser = new NewUserBoundary("valid-email@gmail.com", null, "avatar1", RoleEnum.END_USER);

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .body(invalidUser)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite a null username").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test User Creation with Empty Username")
		public void testUserCreationWithEmptyUsername() throws Exception {
		    // GIVEN a user boundary with an empty username
		    NewUserBoundary invalidUser = new NewUserBoundary("valid-email@gmail.com", "", "avatar1", RoleEnum.END_USER);

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .body(invalidUser)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite an empty username").isTrue();
		    } catch (HttpClientErrorException ex) {
		    	// THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test User Creation with Null Avatar")
		public void testUserCreationWithNullAvatar() throws Exception {
		    // GIVEN a user boundary with a null avatar
		    NewUserBoundary invalidUser = new NewUserBoundary("valid-email@gmail.com", "user", null, RoleEnum.END_USER);

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .body(invalidUser)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite a null avatar").isTrue();
		    } catch (HttpClientErrorException ex) {
		    	// THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test User Creation with Empty Avatar")
		public void testUserCreationWithEmptyAvatar() throws Exception {
		    // GIVEN a user boundary with an empty avatar
		    NewUserBoundary invalidUser = new NewUserBoundary("valid-email@gmail.com", "user", "", RoleEnum.END_USER);

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .body(invalidUser)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite a empty avatar").isTrue();
		    } catch (HttpClientErrorException ex) {
		    	// THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test User Creation with Null Role")
		public void testUserCreationWithNullRole() throws Exception {
		    // GIVEN a user boundary with a null role
		    NewUserBoundary invalidUser = new NewUserBoundary("valid-email@gmail.com", "user", "avatar1", null);

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .body(invalidUser)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite a null role").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches

		    }
		}
		@Test
		@DisplayName("Test User Creation with Empty Role")
		public void testUserCreationWithEmptyRole() throws Exception {
		    // GIVEN a user boundary with an empty role
		    String emptyRoleJson = """
		        {
		            "email": "valid-email@gmail.com",
		            "username": "user",
		            "avatar": "avatar1",
		            "role": ""
		        }
		    """;

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .header("Content-Type", "application/json") // Explicitly set the Content-Type
		            .body(emptyRoleJson)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite a empty role").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		    }
		}
		@Test
		@DisplayName("Test User Creation with Invalid Role")
		public void testUserCreationWithInvalidRole() throws Exception {
		    // GIVEN a user boundary with an invalid role
		    String invalidRoleJson = """
		        {
		            "email": "valid-email@gmail.com",
		            "username": "user",
		            "avatar": "avatar1",
		            "role": "SUPER_ADMIN"
		        }
		    """;

		    try {
		        // WHEN the user is created
		        this.userRestClient
		            .post()
		            .header("Content-Type", "application/json") // Explicitly set the Content-Type
		            .body(invalidRoleJson)
		            .retrieve()
		            .body(UserBoundary.class);

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("User creation succeeded despite an invalid role").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN the server responds with a 400 Bad Request
		        assertThat(ex.getStatusCode().value()).isEqualTo(400); // HTTP 400 Bad Request
		        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
		    }
		}
		
		@Test
		@DisplayName("Test User Cannot Update Email")
		public void testUserCannotUpdateEmail() throws Exception {
		    // GIVEN a user exists
		    NewUserBoundary originalUser = new NewUserBoundary("testuser@gmail.com", "TestUser", "avatar1", RoleEnum.END_USER);
		    this.userRestClient
		            .post()
		            .body(originalUser)
		            .retrieve()
		            .body(UserBoundary.class);

		    // WHEN the user attempts to update their email
		    UserBoundary updatedUser = new UserBoundary(new UserId("2025a.integrative.nagar.yuval","updateEmail@gmail.com"),originalUser);

		    this.userRestClient
		            .put()
		            .uri("/{systemID}/{userEmail}", "2025a.integrative.nagar.yuval", "testuser@gmail.com")
		            .body(updatedUser)
		            .retrieve()
		            .body(void.class);

		    // THEN after a GET request, the email should remain the same
		    UserBoundary retrievedUser = this.userRestClient
		            .get()
		            .uri("/login/{systemID}/{userEmail}", "2025a.integrative.nagar.yuval", "testuser@gmail.com")
		            .retrieve()
		            .body(UserBoundary.class);

		    assertThat(retrievedUser.getUserId().getEmail()).isEqualTo("testuser@gmail.com");
		}
		@Test
		@DisplayName("Test User Can Update Username, Avatar, and Role")
		public void testUserCanUpdateDetails() throws Exception {
		    // GIVEN a user exists
		    NewUserBoundary originalUser = new NewUserBoundary("testuser@gmail.com", "TestUser", "avatar1", RoleEnum.END_USER);
		    UserBoundary createdUser = this.userRestClient
		            .post()
		            .body(originalUser)
		            .retrieve()
		            .body(UserBoundary.class);

		    // WHEN the user updates their details
		    UserBoundary updateRequest = new UserBoundary(createdUser.getUserId(),RoleEnum.OPERATOR,"UpdatedUsername","new-avatar");

		    this.userRestClient
		            .put()
		            .uri("/{systemID}/{userEmail}", "2025a.integrative.nagar.yuval", "testuser@gmail.com")
		            .body(updateRequest)
		            .retrieve()
		            .body(void.class);

		    // THEN after a GET request, the updated details should be reflected
		    UserBoundary retrievedUser = this.userRestClient
		            .get()
		            .uri("/login/{systemID}/{userEmail}", "2025a.integrative.nagar.yuval", "testuser@gmail.com")
		            .retrieve()
		            .body(UserBoundary.class);

		    assertThat(retrievedUser.getUsername()).isEqualTo("UpdatedUsername");
		    assertThat(retrievedUser.getAvatar()).isEqualTo("new-avatar");
		    assertThat(retrievedUser.getRole()).isEqualTo(RoleEnum.OPERATOR);
		}
		@Test
		@DisplayName("Test Update with Wrong Email Fails")
		public void testUpdateWithWrongEmailFails() throws Exception {
		    // GIVEN a user exists
		    NewUserBoundary originalUser = new NewUserBoundary("testuser@gmail.com", "TestUser", "avatar1", RoleEnum.END_USER);
		    this.userRestClient
		            .post()
		            .body(originalUser)
		            .retrieve()
		            .body(UserBoundary.class);

		    // WHEN the user attempts to update using a wrong email
		    UserBoundary updatedUser = new UserBoundary(new UserId("2025a.integrative.nagar.yuval","testuser@gmail.com"),RoleEnum.OPERATOR,"TestUser","avatar1");

		    try {
		        this.userRestClient
		                .put()
		                .uri("/{systemID}/{userEmail}", "2025a.integrative.nagar.yuval", "wrongemail@gmail.com")
		                .body(updatedUser)
		                .retrieve()
		                .toBodilessEntity();

		        // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Update succeeded despite an incorrect email").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN the server responds with a 404 Not Found
		        assertThat(ex.getStatusCode().value()).isEqualTo(404); // HTTP 404 Not Found
		        assertThat(ex.getStatusText()).isEqualTo("Not Found"); // Ensure status text matches
		    }
		}


}
