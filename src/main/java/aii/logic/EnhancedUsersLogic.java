package aii.logic;
import java.util.List;
import aii.Boundary.UserBoundary;

public interface EnhancedUsersLogic extends UserService {
	public List<UserBoundary> getAllUsers(String adminSystemID, String adminEmail,int page, int size);

}
