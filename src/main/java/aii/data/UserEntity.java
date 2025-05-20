package aii.data;

import aii.Boundary.UserId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserEntity {
	@Id
	private String userId;
	private String username;
	private String avatar;
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

	public UserEntity() {
	}

	private String consturctId(String email, String systemID) {
		return email + "@@" + systemID;
	}

	public String getUserId() {
		return userId;
	}
	
	public void setUserId(UserId userId) {
		this.userId = consturctId(userId.getEmail(), userId.getSystemID());
	}
	
	public UserId generateUserId() {
		String[] parts = this.userId.split("@@");
	    return new UserId( parts[1], parts[0]);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}


}
