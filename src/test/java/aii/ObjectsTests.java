package aii;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;

import aii.Boundary.CreatedBy;
import aii.Boundary.Location;
import aii.Boundary.NewUserBoundary;
import aii.Boundary.ObjectBoundary;
import aii.Boundary.ObjectId;
import aii.Boundary.UserBoundary;
import aii.Boundary.UserId;
import aii.data.RoleEnum;

public class ObjectsTests extends ApplicationTests {
	
	//Tests objects  
			@Test
			@DisplayName("Test Valid Creation of Object With OPERATOR")
			public void TestValidCreationOfObjectWithOPERATOR() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);

			    // THEN the created object should match the provided details
			    assertThat(createdObject).isNotNull(); // Ensure the object is created
			    assertThat(createdObject.getObjectId()).isNotNull();
			    assertThat(createdObject.getType()).isEqualTo("lamp"); 
			    assertThat(createdObject.getAlias()).isEqualTo("room1"); 
			    assertThat(createdObject.getStatus()).isEqualTo("on");
			 // Check location fields
			    assertThat(createdObject.getLocation()).isNotNull(); 
			    assertThat(createdObject.getLocation().getLat()).isEqualTo(0.1); 
			    assertThat(createdObject.getLocation().getLng()).isEqualTo(0.1);
			    
			    assertThat(createdObject.getActive()).isTrue(); 
			    assertThat(createdObject.getCreatedBy().getUserId()).isEqualTo(operatorUser.getUserId()); 
			}

			@Test
			@DisplayName("Test Unauthorized Creation Object With ADMIN")
			public void TestUnauthorizedCreationObjectWithADMIN() throws Exception {
			 // GIVEN the server contains User with email "admin@gmail.com" and role ADMIN
			    UserBoundary adminUser= this.userRestClient
		        .post()
		        .body(new NewUserBoundary("admin@gmail.com", "admin", "0-0", RoleEnum.ADMIN))
		        .retrieve()
		        .body(UserBoundary.class);
			   
			 // WHEN I create object with ADMIN credentials
			    try {
			    	this.objectRestClient
			 	     .post()
			 	     .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(adminUser.getUserId()),null))
			 		 .retrieve()
			 		  .body(ObjectBoundary.class);
			 
			 // THEN the operation should fail (this point should not be reached)
			        assertThat(false).as("A non-operator user was able to create an object").isTrue();
			    } catch (HttpClientErrorException ex) {
			        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
			        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
			    }
				
			}
			@Test
			@DisplayName("Test Unauthorized Creation Object With END_USER")
			public void TestUnauthorizedCreationObjectWithEND_USER() throws Exception {
			 // GIVEN the server contains User with email "endUser@gmail.com" and role ADMIN
			    UserBoundary adminUser= this.userRestClient
		        .post()
		        .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
		        .retrieve()
		        .body(UserBoundary.class);
			   
			 // WHEN I create object with END_USER credentials
			    try {
			    	this.objectRestClient
			 	     .post()
			 	     .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(adminUser.getUserId()),null))
			 		 .retrieve()
			 		  .body(ObjectBoundary.class);
			 
			 // THEN the operation should fail (this point should not be reached)
			        assertThat(false).as("A non-operator user was able to create an object").isTrue();
			    } catch (HttpClientErrorException ex) {
			        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
			        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches
			    }
				
			}
			@Test
			@DisplayName("Test Creation Object With empty type value")
			public void TestCreationObjectWithemptytypevalue() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			  try { this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			
			  // THEN the operation should fail (this point should not be reached)
			  assertThat(false).as("An object was created despite an empty Type value").isTrue();
			  }catch (HttpClientErrorException ex) {
				  // THEN an HttpClientErrorException with status 400 BAD_REQUEST is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
			        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
				  
			  }
		   }
			
			@Test
			@DisplayName("Test Creation Object With null type value")
			public void TestCreationObjectWithNullTypeValue() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			  try { this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,null,"room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			
			  // THEN the operation should fail (this point should not be reached)
			  assertThat(false).as("An object was created despite an null Type value").isTrue();
			  }catch (HttpClientErrorException ex) {
				  // THEN an HttpClientErrorException with status 400 BAD_REQUEST is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
			        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
				  
			  }
		   } 
			@Test
			@DisplayName("Test Creation Object With empty alias")
			public void TestCreationObjectWithemptyAlias() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			  try { this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			
			  // THEN the operation should fail (this point should not be reached)
			  assertThat(false).as("An object was created despite an empty alias value").isTrue();
			  }catch (HttpClientErrorException ex) {
				  // THEN an HttpClientErrorException with status 400 BAD_REQUEST is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
			        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
				  
			  }
		   }
			@Test
			@DisplayName("Test Creation Object With null alias")
			public void TestCreationObjectWithNullAlias() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			  try { this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp",null,"on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			
			  // THEN the operation should fail (this point should not be reached)
			  assertThat(false).as("An object was created despite an null alias value").isTrue();
			  }catch (HttpClientErrorException ex) {
				  // THEN an HttpClientErrorException with status 400 BAD_REQUEST is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
			        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
				  
			  }
		   }
			@Test
			@DisplayName("Test Creation Object With empty status")
			public void TestCreationObjectWithemptystatus() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			  try { this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			
			  // THEN the operation should fail (this point should not be reached)
			  assertThat(false).as("An object was created despite an empty status value").isTrue();
			  }catch (HttpClientErrorException ex) {
				  // THEN an HttpClientErrorException with status 400 BAD_REQUEST is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
			        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
				  
			  }
		   }
			@Test
			@DisplayName("Test Creation Object With null status")
			public void TestCreationObjectWithNullstatus() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // WHEN the OPERATOR creates a valid object
			  try { this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1",null,new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			
			  // THEN the operation should fail (this point should not be reached)
			  assertThat(false).as("An object was created despite an null status value").isTrue();
			  }catch (HttpClientErrorException ex) {
				  // THEN an HttpClientErrorException with status 400 BAD_REQUEST is thrown
			        assertThat(ex.getStatusCode().value()).isEqualTo(400); // Ensure HTTP status is 400
			        assertThat(ex.getStatusText()).isEqualTo("Bad Request"); // Ensure status text matches
				  
			  }
		   } 
			
			@Test
			@DisplayName("Test Getting Specific Object With OPERATOR")
			public void TestGettingSpecificObjectWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			 ObjectBoundary ExpectedObject= this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR get a specific Object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",ExpectedObject.getObjectId().getSystemID(),
					 ExpectedObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object matches the expected object
			    assertThat(retrievedObject)
			        .usingRecursiveComparison()
			        .isEqualTo(ExpectedObject);
			 
		
		   }
			@Test
			@DisplayName("Test Update Object Type Value With OPERATOR")
			public void TestUpdateObjectTypeValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object type value
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(null,"lampNum2","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object matches the type value of the updatedObject
			 assertThat(retrievedObject.getType()).isEqualTo("lampNum2"); 
		
		   }
			@Test
			@DisplayName("Test Update Object Alias Value With OPERATOR")
			public void TestUpdateObjectAliasValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object type value
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(null,"lamp","room2","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object matches the alias value of the updatedObject
			 assertThat(retrievedObject.getAlias()).isEqualTo("room2"); 
		
		   }
			@Test
			@DisplayName("Test Update Object Status Value With OPERATOR")
			public void TestUpdateObjectStatusValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object type value
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(null,"lamp","room1","off",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object matches the status value of the updatedObject
			 assertThat(retrievedObject.getStatus()).isEqualTo("off"); 
		
		   }
			@Test
			@DisplayName("Test Update Object Location Value With OPERATOR")
			public void TestUpdateObjectLocationValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object Location value
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.5,0.6),true,new CreatedBy(operatorUser.getUserId()),null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object matches the Location values of the updatedObject
			 assertThat(retrievedObject.getLocation().getLat()).isEqualTo(0.5); 
			 assertThat(retrievedObject.getLocation().getLng()).isEqualTo(0.6); 
			   
		
		   }
			@Test
			@DisplayName("Test Update Object Active Value With OPERATOR")
			public void TestUpdateObjectActiveValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object active value
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),false,new CreatedBy(operatorUser.getUserId()),null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object matches the active value of the updatedObject
			 assertThat(retrievedObject.getActive()).isFalse(); 
			
			   
		
		   }
			@Test
			@DisplayName("Test Update ObjectId Value With OPERATOR")
			public void TestUpdateObjectIdValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object objectId value
			  ObjectId updatedObjectId = new ObjectId("wrongSystemID","wrongId");  
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(updatedObjectId,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object did not match the ObjectId (wrongSystemID,wrongId)
			 assertThat(retrievedObject.getObjectId()).isNotEqualTo(updatedObjectId);
			
			   
		
		   }
			@Test
			@DisplayName("Test Update CreatedBy Value With OPERATOR")
			public void TestUpdateCreatedByValueWithOPERATOR() throws Exception {
				// GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates a valid object
			    ObjectBoundary createdObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR update the object CreatedBy value
			   CreatedBy updatedCreatedBy = new CreatedBy(new UserId("wrongSystemId","wrong@mail.com"));  
			  this.objectRestClient
			 .put()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),createdObject.getObjectId().getId()
					 ,operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .body(new ObjectBoundary(null,"lamp","room1","on",new Location(0.1,0.1),true,updatedCreatedBy,null))
			 .retrieve()
			 .body(void.class);
			 
			 //AND the OPERATOR get the specific object
			 ObjectBoundary retrievedObject= this.objectRestClient
			 .get()
			 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",createdObject.getObjectId().getSystemID(),
					 createdObject.getObjectId().getId(),operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail())
			 .retrieve()
			 .body(ObjectBoundary.class);
			
			// THEN the retrieved object did not match the updated CreatedBy
			 assertThat(retrievedObject.getCreatedBy()).isNotEqualTo(updatedCreatedBy);
			
		   }
			@Test
			@DisplayName("Test Get Specific Object With END_USER When The Active Is True")
			public void TestGetSpecificObjectWithEND_USERWhenTheActiveIsTrue() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);
				// AND contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates valid object
			    ObjectBoundary expectedObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the END_USER get the specific object
			    ObjectBoundary retrievedObject= this.objectRestClient
						 .get()
						 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",expectedObject.getObjectId().getSystemID(),
								 expectedObject.getObjectId().getId(),endUser.getUserId().getSystemID(),endUser.getUserId().getEmail())
						 .retrieve()
						 .body(ObjectBoundary.class);
			
			    // THEN the retrieved object matches the expected object
			    assertThat(retrievedObject)
			        .usingRecursiveComparison()
			        .isEqualTo(expectedObject);
			 
			
		   }
			@Test
			@DisplayName("Test Get Specific Object With END_USER When The Active Is False")
			public void TestGetSpecificObjectWithEND_USERWhenTheActiveIsFalse() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);
				// AND contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates valid object
			    ObjectBoundary expectedObject = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),false,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the END_USER get the specific object
			    try {
			    	this.objectRestClient
			    
						 .get()
						 .uri("/{systemId}/{id}?userSystemID={userSystemId}&userEmail={email}",expectedObject.getObjectId().getSystemID(),
								 expectedObject.getObjectId().getId(),endUser.getUserId().getSystemID(),endUser.getUserId().getEmail())
						 .retrieve()
						 .body(ObjectBoundary.class);
			    
			    // THEN the retrieved object NOT FOUND (because the Active is False)
		        assertThat(false).as("The object was found despite being inactive (active is false)").isTrue();
		    } catch (HttpClientErrorException ex) {
		    	// THEN the server responds with a 404 Not Found
		        assertThat(ex.getStatusCode().value()).isEqualTo(404); // HTTP 404 Not Found
		        assertThat(ex.getStatusText()).isEqualTo("Not Found"); // Ensure status text matches
		    }
			
		   }
			@Test
			@DisplayName("Test Get list of All Objects With OPERATOR")
			public void TestGetlistofAllObjectsWithOPERATOR() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects , two with active True and one with False
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary Object1 = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    expectedObjects.add(Object1);
			    ObjectBoundary Object2 = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp2","room2","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    expectedObjects.add(Object2);
			    ObjectBoundary Object3 = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp3","room3","on",new Location(0.1,0.1),false,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    expectedObjects.add(Object3);
			// WHEN the OPERATOR get the list of all objects
			    ObjectBoundary[] retrivedObjects=  this.objectRestClient
						 .get()
						 .uri("?userSystemID={userSystemId}&userEmail={email}&page={page}&size={size}",operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail(),
								 0,10)
						 .retrieve()
						 .body(ObjectBoundary[].class);
			    
			    // THEN the retrieved list of objects need to match the expected Objects
			    assertThat(retrivedObjects)
				.hasSize(3)
				.usingRecursiveFieldByFieldElementComparator()
			    .containsExactlyInAnyOrderElementsOf(expectedObjects);   
			
		   }
			@Test
			@DisplayName("Test Get list of All Objects With END_USER")
			public void TestGetlistofAllObjectsWithEND_USER() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);
			 // AND the server contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects , two with active True and one with False
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary Object1 = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    expectedObjects.add(Object1);
			    ObjectBoundary Object2 = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp2","room2","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    expectedObjects.add(Object2);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 
			    ObjectBoundary Object3 = this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp3","room3","on",new Location(0.1,0.1),false,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    irrelevant.add(Object3);
			// WHEN the END_USER get the list of all objects
			    ObjectBoundary[] retrivedObjects=  this.objectRestClient
						 .get()
						 .uri("?userSystemID={userSystemId}&userEmail={email}&page={page}&size={size}",endUser.getUserId().getSystemID(),endUser.getUserId().getEmail(),
								 0,10)
						 .retrieve()
						 .body(ObjectBoundary[].class);
			    
			    // THEN the retrieved list of objects need to match the expected Objects (Only the objects with active= true)
			    assertThat(retrivedObjects)
				.hasSize(2)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects)
				.usingRecursiveFieldByFieldElementComparator()
				.doesNotContainAnyElementsOf(irrelevant); 
			
		   }
			@Test
			@DisplayName("Test Get list of All Objects With The Exact Alias with OPERATOR")
			public void TestGetlistofAllObjectsWithTheExactAliaswithOPERATOR() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the exact Alias and one with a different alias
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp1", "ExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp2", "ExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);

			    // Create an irrelevant object with a different alias
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp3", "NotExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);

			    // WHEN the OPERATOR retrieves the list of all objects with the exact alias "ExactAlias"
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byAlias/{alias}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactAlias", 
			                    operatorUser.getUserId().getSystemID(), 
			                    operatorUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact alias "ExactAlias"
			    assertThat(retrievedObjects)
			            .hasSize(2)
			            .usingRecursiveFieldByFieldElementComparator()
			            .containsExactlyInAnyOrderElementsOf(expectedObjects);

			    // The list should not contain any irrelevant objects
			    assertThat(retrievedObjects)
			            .usingRecursiveFieldByFieldElementComparator()
			            .doesNotContain(object3);
			}
			@Test
			@DisplayName("Test Get list of All Objects With The Exact Alias where Two Objects are Active (True) and One is Inactive (False) with OPERATOR")
			public void TestGetlistofAllObjectsWithTheExactAliaswhereTwoObjectsareActiveAndOneisInactiveWithOPERATOR() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the exact Alias and one with a different alias
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp1", "ExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp2", "ExactAlias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);

			    // Create an irrelevant object with a different alias
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp3", "NotExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);

			    // WHEN the OPERATOR retrieves the list of all objects with the exact alias "ExactAlias"
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byAlias/{alias}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactAlias", 
			                    operatorUser.getUserId().getSystemID(), 
			                    operatorUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact alias "ExactAlias"
			    assertThat(retrievedObjects)
			            .hasSize(2)
			            .usingRecursiveFieldByFieldElementComparator()
			            .containsExactlyInAnyOrderElementsOf(expectedObjects);

			    // The list should not contain any irrelevant objects
			    assertThat(retrievedObjects)
			            .usingRecursiveFieldByFieldElementComparator()
			            .doesNotContain(object3);
			}
			@Test
			@DisplayName("Test Get list of All Objects With The Exact Alias with END_USER")
			public void TestGetlistofAllObjectsWithTheExactAliasWithEND_USER() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			 // AND the server contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the exact Alias and one with a different alias
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp1", "ExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp2", "ExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);

			    // Create an irrelevant object with a different alias
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp3", "NotExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);

			    // WHEN the END_USER retrieves the list of all objects with the exact alias "ExactAlias"
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byAlias/{alias}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactAlias", 
			                    endUser.getUserId().getSystemID(), 
			                    endUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact alias "ExactAlias"
			    assertThat(retrievedObjects)
			            .hasSize(2)
			            .usingRecursiveFieldByFieldElementComparator()
			            .containsExactlyInAnyOrderElementsOf(expectedObjects);

			    // The list should not contain any irrelevant objects
			    assertThat(retrievedObjects)
			            .usingRecursiveFieldByFieldElementComparator()
			            .doesNotContain(object3);
			}
			@Test
			@DisplayName("Test Get list of All Objects With The Exact Alias where Two Objects are Active (True) and One is Inactive (False) with END_USER")
			public void TestGetlistofAllObjectsWithTheExactAliaswhereTwoObjectsareActiveAndOneisInactiveWithEND_USER() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			 // AND the server contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the exact Alias and one with a different alias
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp1", "ExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp2", "ExactAlias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object2);

			    // Create an irrelevant object with a different alias
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp3", "NotExactAlias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object3);

			    // WHEN the END_USER retrieves the list of all objects with the exact alias "ExactAlias" 
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byAlias/{alias}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactAlias", 
			                    endUser.getUserId().getSystemID(), 
			                    endUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact alias "ExactAlias" and active= true
			    assertThat(retrievedObjects)
				.hasSize(1)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects)
				.usingRecursiveFieldByFieldElementComparator()
				.doesNotContainAnyElementsOf(irrelevant);
			}
			@Test
			@DisplayName("Test Get All Objects By Alias Pattern With OPERATOR")
			public void TestGetAllObjectsByAliasPatternWithOPERATOR() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the Alias "alias" and one with Alias "ali"
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp1", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp2", "alias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);

			    // Create an object with a different alias , Alias "ali"
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp3", "ali", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object3);

			    // WHEN the OPERATOR retrieves the list of all objects 
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byAliasPattern/{pattern}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ali", 
			                    operatorUser.getUserId().getSystemID(), 
			                    operatorUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the alias pattern "ali"
			    assertThat(retrievedObjects)
				.hasSize(3)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects);
			}

			@Test
			@DisplayName("Test Get All Objects By Alias Pattern With END_USER")
			public void TestGetAllObjectsByAliasPatternWithEND_USER() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			 // AND the server contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the Alias "alias" and one with Alias "ali"
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp1", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp2", "alias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object2);

			    // Create an object with a different alias , Alias "ali"
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "lamp3", "ali", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object3);

			    // WHEN the END_USER retrieves the list of all objects with the alias pattern "ali"
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byAliasPattern/{pattern}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ali", 
			                    endUser.getUserId().getSystemID(), 
			                    endUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			 // THEN the retrieved list of objects should only include objects with the alias pattern "ali" and active= true
			    assertThat(retrievedObjects)
				.hasSize(2)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects);
			}
			@Test
			@DisplayName("Test Get All Objects By Exact Type With OPERATOR")
			public void TestGetAllObjectsByExactTypeWithOPERATOR() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the Type "ExactType" and one with Type "NotExactType"
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 
			    // Create an object with a different type , Type "NotExactType"
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "NotExactType", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object3);

			    // WHEN the OPERATOR retrieves the list of all objects 
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byType/{type}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactType", 
			                    operatorUser.getUserId().getSystemID(), 
			                    operatorUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact Type "ExactType"
			    assertThat(retrievedObjects)
				.hasSize(2)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects);
			}

			@Test
			@DisplayName("Test Get All Objects By Exact Type With END_USER")
			public void TestGetAllObjectsByExactTypeWithEND_USER() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			 // AND the server contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the Type "ExactType" and one with Type "NotExactType"
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object2);
			    
			    
			    // Create an object with a different type , Type "NotExactType"
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "NotExactType", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object3);

			    // WHEN the END_USER retrieves the list of all objects 
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byType/{type}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactType", 
			                    endUser.getUserId().getSystemID(), 
			                    endUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact Type "ExactType" and active= true
			    assertThat(retrievedObjects)
				.hasSize(1)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects);
			}
			
			@Test
			@DisplayName("Test Get All Objects By Exact Type And Status With OPERATOR")
			public void TestGetAllObjectsByExactTypeAndStatusWithOPERATOR() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the Type "ExactType" and Status "on"  
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 
			    // AND one with Type "ExactType" and Status "off"
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "off", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object3);

			    // WHEN the OPERATOR retrieves the list of all objects 
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byTypeAndStatus/{type}/{status}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactType","on", 
			                    operatorUser.getUserId().getSystemID(), 
			                    operatorUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact Type "ExactType" and Status "on"
			    assertThat(retrievedObjects)
				.hasSize(2)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects);
			}
			@Test
			@DisplayName("Test Get All Objects By Exact Type And Status With END_USER")
			public void TestGetAllObjectsByExactTypeAndStatusWithEND_USER() throws Exception {
			    // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			 // AND the server contains a User with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects, two with the Type "ExactType" and Status "on"  
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>();
			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "on", new Location(0.1, 0.1), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object2);
			     
			    // AND one with Type "ExactType" and Status "off"
			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "ExactType", "alias", "off", new Location(0.1, 0.1), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object3);

			    // WHEN the END_USER retrieves the list of all objects 
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byTypeAndStatus/{type}/{status}?userSystemID={systemID}&userEmail={email}&page={page}&size={size}", 
			                    "ExactType","on", 
			                    endUser.getUserId().getSystemID(), 
			                    endUser.getUserId().getEmail(),
			                    0, 
			                    10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved list of objects should only include objects with the exact Type "ExactType" and Status "on" and active=true
			    assertThat(retrievedObjects)
				.hasSize(1)
				.usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(expectedObjects);
			}
			@Test
			@DisplayName("Test Get List Of Objects By Location Within A Specific Distance With NEUTRAL Units And OPERATOR")
			public void TestGetListOfObjectsByLocationWithNeutralUnitsWithOPERATOR() throws Exception {
			    // GIVEN a user with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three objects, two within the distance range and one outside the range
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "object1", "alias1", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "object2", "alias2", "on", new Location(0.0002, 0.0002), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object2);

			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "object3", "alias3", "on", new Location(10.0, 10.0), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);

			    // WHEN the OPERATOR searches for objects within a specific distance
			    double lat = 0.0;
			    double lng = 0.0;
			    double distance = 0.01; // 0.01 radians (approximately 1.11 km)
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byLocation/{lat}/{lng}/{distance}?units={units}&userSystemID={systemID}&userEmail={email}&page={page}&size={size}",
			                    lat, lng, distance, "NEUTRAL", operatorUser.getUserId().getSystemID(), operatorUser.getUserId().getEmail(), 0, 10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved objects should match the expected objects within the range
			    assertThat(retrievedObjects)
			            .hasSize(2) // Two objects are within the distance
			            .usingRecursiveFieldByFieldElementComparator()
			            .containsExactlyInAnyOrderElementsOf(expectedObjects);

			    // Ensure the out-of-range object is not included
			    assertThat(retrievedObjects)
			            .usingRecursiveFieldByFieldElementComparator()
			            .doesNotContain(object3);
			}
			@Test
			@DisplayName("Test Get List Of Objects By Location Within A Specific Distance With NEUTRAL Units And END_USER")
			public void TestGetListOfObjectsByLocationWithNeutralUnitsWithEND_USER() throws Exception {
			    // GIVEN a user with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			    // AND a user with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);

			    // AND the OPERATOR creates three objects, two within the distance range and one outside the range
			    List<ObjectBoundary> expectedObjects = new ArrayList<>();
			    ObjectBoundary object1 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "object1", "alias1", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    expectedObjects.add(object1);
			    
			    List<ObjectBoundary> irrelevant = new ArrayList<>(); 

			    ObjectBoundary object2 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "object2", "alias2", "on", new Location(0.0002, 0.0002), false, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object2);

			    ObjectBoundary object3 = this.objectRestClient
			            .post()
			            .body(new ObjectBoundary(null, "object3", "alias3", "on", new Location(10.0, 10.0), true, new CreatedBy(operatorUser.getUserId()), null))
			            .retrieve()
			            .body(ObjectBoundary.class);
			    irrelevant.add(object3);

			    // WHEN the END_USER searches for objects within a specific distance
			    double lat = 0.0;
			    double lng = 0.0;
			    double distance = 0.01; // 0.01 radians (approximately 1.11 km)
			    ObjectBoundary[] retrievedObjects = this.objectRestClient
			            .get()
			            .uri("/search/byLocation/{lat}/{lng}/{distance}?units={units}&userSystemID={systemID}&userEmail={email}&page={page}&size={size}",
			                    lat, lng, distance, "NEUTRAL", endUser.getUserId().getSystemID(), endUser.getUserId().getEmail(), 0, 10)
			            .retrieve()
			            .body(ObjectBoundary[].class);

			    // THEN the retrieved objects should match the expected objects within the range and active= true
			    assertThat(retrievedObjects)
			            .hasSize(1) 
			            .usingRecursiveFieldByFieldElementComparator()
						.containsExactlyInAnyOrderElementsOf(expectedObjects)
						.usingRecursiveFieldByFieldElementComparator()
						.doesNotContainAnyElementsOf(irrelevant);

			    
			}
			@Test
			@DisplayName("Test Admin Deletes All Objects and Gets Empty List of Objects")
			public void TestAdminDeletesAllObjectsandGetsEmptyListofObjects() throws Exception {
			    // GIVEN the server contains an admin user
				 UserBoundary adminUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("admin@gmail.com", "admin", "admin-avatar", RoleEnum.ADMIN))
			        .retrieve()
			        .body(UserBoundary.class);

			 // AND a user with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			    //AND the OPERATOR creates three valid objects
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type1", "alias1", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type2", "alias2", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type3", "alias3", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

			    // WHEN the ADMIN deletes all objects
			    this.adminRestClient
			        .delete()
			        .uri("/objects?userSystemID={systemID}&userEmail={email}", 
			        		adminUser.getUserId().getSystemID(),adminUser.getUserId().getEmail())
			        .retrieve()
			        .body(Void.class);

			 // AND the OPERATOR get the list of all objects
			    ObjectBoundary[] retrivedObjects=  this.objectRestClient
						 .get()
						 .uri("?userSystemID={userSystemId}&userEmail={email}&page={page}&size={size}",operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail(),
								 0,10)
						 .retrieve()
						 .body(ObjectBoundary[].class);
			    
			 // THEN the retrieved objects should be an empty list
			    assertThat(retrivedObjects)
			            .hasSize(0);
			}
			@Test
			@DisplayName("Test OPERATOR Deletes All Objects")
			public void TestOPERATORDeletesAllObjects() throws Exception {
			 // GIVEN a user with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			    //AND the OPERATOR creates three valid objects
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type1", "alias1", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type2", "alias2", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type3", "alias3", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

			    // WHEN the OPERATOR try to delete all objects
			    try {
			    this.adminRestClient
			        .delete()
			        .uri("/objects?userSystemID={systemID}&userEmail={email}", 
			        		operatorUser.getUserId().getSystemID(), operatorUser.getUserId().getEmail())
			        .retrieve()
			        .body(Void.class);
			    
			      // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Non-admin user was able to delete all objects").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
		        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
		        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches  
			}
			}
			@Test
			@DisplayName("Test END_USER Deletes All Objects")
			public void TestEND_USERDeletesAllObjects() throws Exception {
			 // GIVEN a user with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			            .retrieve()
			            .body(UserBoundary.class);
			 // AND a user with email "endUser@gmail.com" and role END_USER
			    UserBoundary endUser = this.userRestClient
			            .post()
			            .body(new NewUserBoundary("endUser@gmail.com", "endUser", "0-0", RoleEnum.END_USER))
			            .retrieve()
			            .body(UserBoundary.class);
			    //AND the OPERATOR creates three valid objects
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type1", "alias1", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type2", "alias2", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);
			    this.objectRestClient
	            .post()
	            .body(new ObjectBoundary(null, "type3", "alias3", "on", new Location(0.0001, 0.0001), true, new CreatedBy(operatorUser.getUserId()), null))
	            .retrieve()
	            .body(ObjectBoundary.class);

			    // WHEN the END_USER try to delete all objects
			    try {
			    this.adminRestClient
			        .delete()
			        .uri("/objects?userSystemID={systemID}&userEmail={email}", 
			        		endUser.getUserId().getSystemID(), endUser.getUserId().getEmail())
			        .retrieve()
			        .body(Void.class);
			    
			      // THEN the operation should fail (this point should not be reached)
		        assertThat(false).as("Non-admin user was able to delete all objects").isTrue();
		    } catch (HttpClientErrorException ex) {
		        // THEN an HttpClientErrorException with status 401 UNAUTHORIZED is thrown
		        assertThat(ex.getStatusCode().value()).isEqualTo(401); // Ensure HTTP status is 401
		        assertThat(ex.getStatusText()).isEqualTo("Unauthorized"); // Ensure status text matches  
			}
			}
			@Test
			@DisplayName("Test Get list of All Objects In Page 1 And Size 2 With OPERATOR")
			public void TestGetlistofAllObjectsInPage1AndSize2WithOPERATOR() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects 
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp2","room2","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp3","room3","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR get the list of all objects In Page 1 And Size 2
			    ObjectBoundary[] retrivedObjects=  this.objectRestClient
						 .get()
						 .uri("?userSystemID={userSystemId}&userEmail={email}&page={page}&size={size}",operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail(),
								 1,2)
						 .retrieve()
						 .body(ObjectBoundary[].class);
			    
			    // THEN the retrieved list of objects need to be size 1
			    assertThat(retrivedObjects)
				.hasSize(1);   
			
		   }
			@Test
			@DisplayName("Test Get list of All Objects In Page 0 And Size 2 With OPERATOR")
			public void TestGetlistofAllObjectsInPage0AndSize2WithOPERATOR() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects 
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp2","room2","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp3","room3","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR get the list of all objects In Page 0 And Size 2
			    ObjectBoundary[] retrivedObjects=  this.objectRestClient
						 .get()
						 .uri("?userSystemID={userSystemId}&userEmail={email}&page={page}&size={size}",operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail(),
								 0,2)
						 .retrieve()
						 .body(ObjectBoundary[].class);
			    
			    // THEN the retrieved list of objects need to be size 2
			    assertThat(retrivedObjects)
				.hasSize(2);   
			
		   }
			@Test
			@DisplayName("Test Get list of All Objects In Page 0 And Size 5 With OPERATOR")
			public void TestGetlistofAllObjectsInPage0AndSize3WithOPERATOR() throws Exception {
				 // GIVEN the server contains a User with email "operator@gmail.com" and role OPERATOR
			    UserBoundary operatorUser = this.userRestClient
			        .post()
			        .body(new NewUserBoundary("operator@gmail.com", "operator", "0-0", RoleEnum.OPERATOR))
			        .retrieve()
			        .body(UserBoundary.class);

			    // AND the OPERATOR creates three valid objects 
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp1","room1","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp2","room2","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			    this.objectRestClient
					    .post()
					    .body(new ObjectBoundary(null,"lamp3","room3","on",new Location(0.1,0.1),true,new CreatedBy(operatorUser.getUserId()),null))
					    .retrieve()
					    .body(ObjectBoundary.class);
			// WHEN the OPERATOR get the list of all objects In Page 0 And Size 5
			    ObjectBoundary[] retrivedObjects=  this.objectRestClient
						 .get()
						 .uri("?userSystemID={userSystemId}&userEmail={email}&page={page}&size={size}",operatorUser.getUserId().getSystemID(),operatorUser.getUserId().getEmail(),
								 0,5)
						 .retrieve()
						 .body(ObjectBoundary[].class);
			    
			    // THEN the retrieved list of objects need to be size 3 , all the created objects
			    assertThat(retrivedObjects)
				.hasSize(3);   
			
		   }



}
