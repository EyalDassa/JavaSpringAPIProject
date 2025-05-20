package aii.logic;

import java.util.List;
import java.util.Optional;

import aii.Boundary.UserBoundary;

public interface UserService {

	public UserBoundary createUser(UserBoundary newUser);

	public Optional<UserBoundary> login(String systemId, String userEmail);

	public UserBoundary updateUser(String systemId, String userEmail, UserBoundary update);
	@Deprecated
	public List<UserBoundary> getAllUsers(String adminSystemID, String adminEmail);

	public void deleteAllUsers(String adminSystemID, String adminEmail);

}
