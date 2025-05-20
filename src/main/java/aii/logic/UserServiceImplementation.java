package aii.logic;


import aii.Boundary.UserBoundary;
import aii.Boundary.UserId;
import aii.Exceptions.InvalidInputException;
import aii.Exceptions.UnauthorizedAccessException;
import aii.Exceptions.UserNotFoundException;
import aii.Utility.UserConverter;
import aii.dal.UsersCrud;
import aii.data.RoleEnum;
import aii.data.UserEntity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImplementation implements EnhancedUsersLogic {

	private final UsersCrud usersCrud;
	private UserConverter userConverter;
	private String systemId;
	private Log logger = LogFactory.getLog(UserServiceImplementation.class);

	@Value("${spring.application.name:2025a.integrative.nagar.yuval}")
	public void setSystemId(String systemId) {
		this.systemId = systemId;
		this.logger.info("****** " + this.systemId);
	}

	public UserServiceImplementation(UsersCrud usersCrud, UserConverter userConverter) {
		this.usersCrud = usersCrud;
		this.userConverter = userConverter;
	}

	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		this.logger.trace("createUser(" + user + ")");
		
		if (user == null) {
			throw new InvalidInputException("User cannot be null");
		}
		
		if(user.getUsername() == null || user.getUsername().isEmpty()) {
			throw new InvalidInputException("Username cannot be null or empty");
		}
		
		user.setUserId(new UserId(this.systemId,user.getUserId().getEmail()));
		
		if(!validEmail(user.getUserId())) {
			throw new InvalidInputException("Invalid email address");
		}

		if (!isValidRole(user.getRole())) {
			throw new InvalidInputException("Invalid role value: " + user.getRole());
		}
		
		if(user.getAvatar() == null || user.getAvatar().isEmpty()) {
			throw new InvalidInputException("Avatar cannot be null or empty");
		}

// MAY BE NEEDED FOR FUTURE SPRINTS
//		if (usersCrud.existsById(user.getUserId().getEmail() + "@" + user.getUserId().getSystemId())) {
//			throw new InvalidInputException("User with the same ID already exists: " + user.getUserId());
//		}

		UserEntity entity = userConverter.toEntity(user);
		usersCrud.save(entity);
		return userConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserBoundary> login(String systemId, String userEmail) {
		this.logger.trace("login( "+"systemId: " + systemId +"userEmail: "+userEmail+")");
		
		if (systemId == null || systemId.isEmpty()) {
			throw new InvalidInputException("systemID cannot be null or empty");
		}
		if (!systemId.equals(this.systemId)) {
			throw new InvalidInputException("Incorrect systemID");
		}
		if (userEmail == null || userEmail.isEmpty()) {
			throw new InvalidInputException("User email cannot be null or empty");
		}
		
		return usersCrud.findById(userEmail + "@@" + systemId).map(userConverter::toBoundary).or(() -> {
			throw new UserNotFoundException("User not found with email: " + userEmail);
		});
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String systemId, String userEmail, UserBoundary update) {
		this.logger.trace("updateUser( "+"systemId: " + systemId +"userEmail: "+userEmail+" updateUser: "+update+")");
		Optional<UserEntity> entityOp = this.usersCrud
				.findById(userEmail + "@@" + systemId);
		if (!entityOp.isEmpty()){
			UserEntity updatedUser = entityOp.get();
			
			if (update.getUsername() != null && !update.getUsername().isEmpty()) {
				updatedUser.setUsername(update.getUsername());
			}
			
			if (update.getAvatar() != null && !update.getAvatar().isEmpty()) {
				updatedUser.setAvatar(update.getAvatar());
			}
			
			if (update.getRole() != null && isValidRole(update.getRole())) {
				updatedUser.setRole(update.getRole());
			}
			
			return this.userConverter.toBoundary( 
					this.usersCrud.save(updatedUser));
			
		}else {
			throw new UserNotFoundException ("Could not find user by email: " + userEmail);
		}
	}

	@Override
	@Transactional
	public void deleteAllUsers(String adminSystemID, String adminEmail) {
		this.logger.trace("deleteAllUsers( "+"adminSystemID: " + adminSystemID +"adminEmail: "+adminEmail+")");
		RoleEnum role = getUserRole(adminSystemID,adminEmail);
		if(role == RoleEnum.ADMIN) {
			this.usersCrud.deleteAll();
		}else {
			throw new UnauthorizedAccessException("Only the ADMIN is allowed to delete the users list");
		}
		
	}
	public RoleEnum getUserRole(String userSystemID, String userEmail) {
		if (userSystemID == null || userSystemID.isEmpty()) {
			throw new InvalidInputException("systemID cannot be null or empty");
		}
		if (!userSystemID.equals(this.systemId)) {
			throw new InvalidInputException("Incorrect systemID");
		}
		if (userEmail == null || userEmail.isEmpty()) {
			throw new InvalidInputException("User email cannot be null or empty");
		}
		Optional<UserEntity> user = this.usersCrud.findById(userEmail + "@@" + userSystemID);
		if (user == null || !user.isPresent()) {
			throw new UserNotFoundException("User not found with email: " + userEmail + "and systemID: " + userSystemID);
		}
		return user.get().getRole();
	}

	private boolean isValidRole(RoleEnum role) {
		for (RoleEnum validRole : RoleEnum.values()) {
			if (validRole.equals(role)) {
				return true;
			}
		}
		return false;
	}

	//@Override
	//@Transactional(readOnly = true)
	@Deprecated
	public List<UserBoundary> getAllUsers(String adminSystemID, String adminEmail){
		throw new RuntimeException("Deprecated operation - use getAllUser that uses pagination");	
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary>getAllUsers(String adminSystemID, String adminEmail,int page, int size) {
		this.logger.trace("getAllUsers( "+"adminSystemID: " + adminSystemID +"adminEmail: "+adminEmail+ "page: "+page+" size: "+size+")");
		RoleEnum role  = getUserRole(adminSystemID,adminEmail);
		if(role== RoleEnum.ADMIN) {
				return this.usersCrud
						.findAll(PageRequest.of(page, size, Direction.DESC, "username","userId"))
						.stream()
						.map(this.userConverter::toBoundary)
						.toList();
			}
		throw new UnauthorizedAccessException("Only the ADMIN is allowed to retrieve the users list");
		
		
	}
	
	 public boolean validEmail(UserId userId) {
		    // Get an instance of the EmailValidator
		    EmailValidator emailValidator = EmailValidator.getInstance();

		    // Validate the email
		    return emailValidator.isValid(userId.getEmail());
	    }

}
