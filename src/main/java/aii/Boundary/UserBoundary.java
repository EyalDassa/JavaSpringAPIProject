package aii.Boundary;

import aii.data.RoleEnum;

public class UserBoundary {
	private UserId userId;
	private RoleEnum role;
	private String username;
	private String avatar;

	public UserBoundary() {

	}

	public UserBoundary(UserId userId, NewUserBoundary newuser) {
		this.userId = userId;
		this.role = newuser.getRole();
		this.username = newuser.getUsername();
		this.avatar = newuser.getAvatar();

	}
	//For tests
	public UserBoundary(UserId userId, RoleEnum role,String username,String avatar) {
		this.userId = userId;
		this.role = role;
		this.username = username;
		this.avatar = avatar;

	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
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
		return "UserBoundary [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}

}
