package aii.Boundary;

import aii.data.RoleEnum;

public class NewUserBoundary {
private String email;
private RoleEnum role;
private String username;
private String avatar;

public NewUserBoundary() {
	
}

public NewUserBoundary(String email, String username, String avatar, RoleEnum role) {
	
	this.email = email;
	this.role = role;
	this.username = username;
	this.avatar = avatar;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public RoleEnum getRole() {
	return role;
}

public void setRole(RoleEnum role) {
	this.role = role;
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

@Override
public String toString() {
	return "NewUserBoundary [email=" + email + ", role=" + role + ", username=" + username + ", avatar=" + avatar + "]";
}




}
