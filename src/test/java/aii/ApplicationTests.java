package aii;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import aii.Boundary.NewUserBoundary;
import aii.Boundary.UserBoundary;
import aii.Boundary.UserId;
import aii.data.RoleEnum;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class ApplicationTests {
	protected int port;
	protected RestClient adminRestClient;
	protected RestClient userRestClient;
	protected RestClient objectRestClient;
	protected RestClient commandRestClient;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		String AdminUrl = "http://localhost:" + this.port + "/aii/admin";
		String usersUrl = "http://localhost:" + this.port + "/aii/users";
		String objectsUrl = "http://localhost:" + this.port + "/aii/objects";
		String commandsUrl = "http://localhost:" + this.port + "/aii/commands";
		
		this.adminRestClient = RestClient.create(AdminUrl);
		this.userRestClient = RestClient.create(usersUrl);
		this.objectRestClient = RestClient.create(objectsUrl);
		this.commandRestClient = RestClient.create(commandsUrl);	
	}
	
	@AfterEach
//	@BeforeEach
	public void tearDown() {
		 System.err.println("***** Tearing down the database *****");
		 this.userRestClient
			.post()
			.body(new NewUserBoundary("jane@gmail.com", "jane", "0-0",RoleEnum.ADMIN))
			.retrieve()
			.body(UserBoundary.class);

		 this.adminRestClient.delete().uri("/commands?userSystemID={systemID}&userEmail={email}", "2025a.integrative.nagar.yuval","jane@gmail.com")
		            .retrieve()
		            .body(Void.class);
		 this.adminRestClient.delete().uri("/objects?userSystemID={systemID}&userEmail={email}", "2025a.integrative.nagar.yuval","jane@gmail.com")
         .retrieve()
         .body(Void.class);
		 this.adminRestClient.delete().uri("/users?userSystemID={systemID}&userEmail={email}", "2025a.integrative.nagar.yuval","jane@gmail.com")
         .retrieve()
         .body(Void.class);

		  System.out.println();
	}
	
	@Test
	public void contextLoads() {
	}
}








