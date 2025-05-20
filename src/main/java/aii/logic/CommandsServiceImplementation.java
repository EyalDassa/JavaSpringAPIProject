package aii.logic;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aii.Boundary.CommandBoundary;
import aii.Boundary.CommandId;
import aii.Boundary.UserId;
import aii.Exceptions.InvalidInputException;
import aii.Exceptions.ObjectNotFoundException;
import aii.Exceptions.UnauthorizedAccessException;
import aii.Exceptions.UserNotFoundException;
import aii.Utility.CommandConverter;
import aii.dal.CommandsCrud;
import aii.dal.ObjectsCrud;
import aii.dal.UsersCrud;
import aii.data.ObjectEntity;
import aii.data.RoleEnum;
import aii.data.UserEntity;

@Service
public class CommandsServiceImplementation implements EnhancedCommandsLogic{
	private CommandsCrud commands;
	private UsersCrud users;
	private ObjectsCrud objects;
	private CommandConverter converter;
	private String systemId;
	private Log logger = LogFactory.getLog(CommandsServiceImplementation.class);
	
	public CommandsServiceImplementation(CommandsCrud commands, CommandConverter converter,UsersCrud users,ObjectsCrud objects) {
		this.commands = commands;
		this.converter = converter;
		this.users= users;
		this.objects=objects;
	}

	@Value("${spring.application.name:2025a.integrative.nagar.yuval}")
	public void setSpringApplicationName(String systemId) {
		this.systemId = systemId;
		this.logger.info("****** " + this.systemId);
	}
	
	@Override
	@Transactional(readOnly = false)
	public List<Object> invokeCommand(CommandBoundary command) {
		this.logger.trace("invokeCommand(" + command + ")");
		// Validate required fields
		if(!validEmail(command.getInvokedBy().getUserId())) {
        	throw new InvalidInputException("Email is not a valid address");
        }
		RoleEnum role=getUserRole(command.getInvokedBy().getUserId().getSystemID(), command.getInvokedBy().getUserId().getEmail());
		if(role == RoleEnum.END_USER) {
		
		command.setCommandId(new CommandId(this.systemId, UUID.randomUUID().toString()));
		
		if(command.getCommand() == null || command.getCommand().isBlank()) {
			throw new InvalidInputException("Cannot accept empty or null commands");
		}
		
		if(command.getTargetObject() == null) {
			throw new InvalidInputException("Target Object cannot be null");
		}
		
		if(command.getTargetObject().getObjectId() == null) {
			throw new InvalidInputException("Object Id cannot be null");
		}
		
		if(command.getTargetObject().getObjectId().getId() == null || command.getTargetObject().getObjectId().getId().isBlank()) {
			throw new InvalidInputException("Cannot accept empty or null ObjectId.id");
		}
		
		if(command.getTargetObject().getObjectId().getSystemID() == null || command.getTargetObject().getObjectId().getSystemID().isBlank()) {
			throw new InvalidInputException("Cannot accept empty or null ObjectId.systemID");
		}
		
		//
		Optional<ObjectEntity> object=this.objects.findById(command.getTargetObject().getObjectId().getId()+"@@"+command.getTargetObject().getObjectId().getSystemID());
		if(object == null || !object.isPresent() || !object.get().getActive()) {
			throw new ObjectNotFoundException("The object with id "+command.getTargetObject().getObjectId().getId()+" not found in the database");	
		}
		 command.setInvocationTimestamp(new Date());
		
		 
       /* if (command.getInvokedBy() == null) {
        	throw new InvalidInputException("InvokedBy cannot be null");
        }
        if (command.getInvokedBy().getUserId() == null) {
            throw new InvalidInputException("UserId cannot be null");
        }

        if(command.getInvokedBy().getUserId().getSystemID() == null || command.getInvokedBy().getUserId().getSystemID().isBlank()) {
        	throw new InvalidInputException("SystemId cannot be null or empty");
        }
        
        if(command.getInvokedBy().getUserId().getEmail() == null || command.getInvokedBy().getUserId().getEmail().isBlank()) {
        	throw new InvalidInputException("Email cannot be null or empty");
        }*/
        
        if (command.getCommandAttributes() == null || command.getCommandAttributes().isEmpty()) {
            throw new InvalidInputException("commandAttributes must contain at least one entry.");
        }
        
        CommandBoundary rv = this.converter.toBoundary( 
				this.commands.save(
						this.converter.toEntity(command)));
		
        
        List<Object> list = Arrays.asList(rv);
        return list;
		}else {
			throw new UnauthorizedAccessException("Only the END_USER is allowed to invoke command");
		}
	}

	//@Override
	//@Transactional(readOnly = true)
	@Deprecated
	public List<CommandBoundary> getAllCommands(String adminSystemID, String adminEmail) {
		throw new RuntimeException("Deprecated operation - use getAllCommands that uses pagination");
		//return this.commands
				//.findAll() 
				//.stream() 
				//.map(this.converter::toBoundary)
				//.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommandBoundary>getAllCommands(String adminSystemID, String adminEmail,int page, int size) {
		this.logger.trace("getAllCommands( adminSystemID: "+ adminSystemID +" adminEmail: "+adminEmail+" page: "+page+" size: "+size+ ")");
			RoleEnum role= getUserRole(adminSystemID, adminEmail);
			if(role == RoleEnum.ADMIN) {
				return this.commands
						.findAll(PageRequest.of(page, size, Direction.DESC,"invocationTimestamp", "id"))
						.stream()
						.map(this.converter::toBoundary)
						.toList();
			}else {
				throw new UnauthorizedAccessException("Only the ADMIN is allowed to retrieve the commands list");
			}	
	}
	@Override
	@Transactional
	public void deleteAllCommands(String adminSystemID, String adminEmail) {
		this.logger.trace("deleteAllCommands( adminSystemID: "+ adminSystemID +" adminEmail: "+adminEmail+")");
		RoleEnum role= getUserRole(adminSystemID, adminEmail);
		if(role == RoleEnum.ADMIN) {
				this.commands.deleteAll();
		}else {
			throw new UnauthorizedAccessException("Only the ADMIN is allowed to delete the commands list");	
		}
		
	}
	
	 public boolean validEmail(UserId userId) {
		    // Get an instance of the EmailValidator
		    EmailValidator emailValidator = EmailValidator.getInstance();

		    // Validate the email
		    return emailValidator.isValid(userId.getEmail());
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
			Optional<UserEntity> user = this.users.findById(userEmail + "@@" + userSystemID);
			if (user == null || !user.isPresent()) {
				throw new UserNotFoundException("User not found with email: " + userEmail + "and systemID: " + userSystemID);
			}
			return user.get().getRole();
		}


}
