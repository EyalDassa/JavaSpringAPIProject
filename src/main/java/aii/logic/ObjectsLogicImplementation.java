package aii.logic;

import java.util.Date;
import java.util.HashMap;
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

import aii.Boundary.CreatedBy;
import aii.Boundary.ObjectBoundary;
import aii.Boundary.ObjectId;
import aii.Boundary.UserId;
import aii.Exceptions.InvalidInputException;
import aii.Exceptions.ObjectNotFoundException;
import aii.Exceptions.UnauthorizedAccessException;
import aii.Exceptions.UserNotFoundException;
import aii.Utility.ObjectConverter;
import aii.dal.ObjectsCrud;
import aii.dal.UsersCrud;
import aii.data.ObjectEntity;
import aii.data.RoleEnum;
import aii.data.UserEntity;

@Service
public class ObjectsLogicImplementation implements EnhancedObjectsLogic {
	private ObjectsCrud objects;
	private UsersCrud users;
	private String systemId;
	private ObjectConverter converter;
	private Log logger = LogFactory.getLog(ObjectsLogicImplementation.class);

	public ObjectsLogicImplementation(ObjectsCrud objects, ObjectConverter converter, UsersCrud users) {
		this.objects = objects;
		this.converter = converter;
		this.users = users;

	}

	@Value("${spring.application.name:2025a.integrative.nagar.yuval}")
	public void setSystemId(String systemId) {
		this.systemId = systemId;
		this.logger.info("****** " + this.systemId);
	}

	@Override
	@Transactional(/* readOnly = false */)
	public ObjectBoundary create(String userSystemID, String userEmail, ObjectBoundary newObject) {
		this.logger.trace("createObject( userSystemID: " + userSystemID + " userEmail: " + userEmail + " newObject: "
				+ newObject + ")");
		if (!userSystemID.equals(this.systemId)) {
			userSystemID = this.systemId;
		}

		if (!validEmail(userEmail)) {
			throw new InvalidInputException("Invalid email address");
		}

		RoleEnum role = getUserRole(userSystemID, userEmail);
		if (role == RoleEnum.OPERATOR) {
			if (newObject.getObjectId() == null) {
				// throw new InvalidInputException("Object Id can not be null");
				newObject.setObjectId(new ObjectId(this.systemId, UUID.randomUUID().toString()));
			} else {
				newObject.getObjectId().setId(UUID.randomUUID().toString());
				newObject.getObjectId().setSystemID(this.systemId);
			}
			newObject.setCreationTimestamp(new Date());

			if (newObject.getType() == null || newObject.getType().isEmpty()) {
				throw new InvalidInputException("Type can not be null or empty");
			}
			if (newObject.getAlias() == null || newObject.getAlias().isEmpty()) {
				throw new InvalidInputException("Alias can not be null or empty");
			}
			if (newObject.getStatus() == null || newObject.getStatus().isEmpty()) {
				throw new InvalidInputException("Status can not be null or empty");
			}
			if (newObject.getLocation() == null) {
				throw new InvalidInputException("Location can not be null");
			}

			if (newObject.getActive() == null) {
				throw new InvalidInputException("Active can not be null or empty, need to be true/false");
			}

			newObject.setCreatedBy(new CreatedBy(new UserId(userSystemID, userEmail)));

			if (newObject.getObjectDetails() == null) {
				newObject.setObjectDetails(new HashMap<>());
			}

			return this.converter.toBoundary(this.objects.save(this.converter.toEntity(newObject)));
		} else {
			throw new UnauthorizedAccessException("Creating objects can be done by OPERATOR only");
		}

	}

	@Override
	@Transactional // (readOnly = false)
	public ObjectBoundary update(String userSystemID, String userEmail, String objectSystemID, String objectId,
			ObjectBoundary update) {
		this.logger.trace("updateObject( userSystemID: " + userSystemID + " userEmail: " + userEmail
				+ " objectSystemID: " + objectSystemID + " objectId: " + objectId + " updateObject: " + update + ")");
		RoleEnum role = getUserRole(userSystemID, userEmail);

		Optional<ObjectEntity> entityOp = this.objects.findById(objectId + "@@" + objectSystemID);

		if (!entityOp.isEmpty()) {
			ObjectEntity updatedObject = entityOp.get();
			boolean dirty = false;
			if (role == RoleEnum.OPERATOR) {

				if (update.getType() != null && !update.getType().isEmpty()) {
					updatedObject.setType(update.getType());
					dirty = true;
				}

				if (update.getAlias() != null && !update.getAlias().isEmpty()) {
					updatedObject.setAlias(update.getAlias());
					dirty = true;

				}

				if (update.getStatus() != null && !update.getStatus().isEmpty()) {
					updatedObject.setStatus(update.getStatus());
					dirty = true;
				}

				if (update.getLocation() != null) {
					if (update.getLocation().getLat() != updatedObject.getLocationlat()) {
						updatedObject.setLocationlat(update.getLocation().getLat());
						dirty = true;
					}
					if (update.getLocation().getLng() != updatedObject.getLocationlng()) {
						updatedObject.setLocationlng(update.getLocation().getLng());
						dirty = true;
					}
				}

				if (update.getActive() != null) {
					updatedObject.setActive(update.getActive());
					dirty = true;

				}

				// ignore creationTimeStamp

				// ignore createdBy

				if (update.getObjectDetails() != null) {
					updatedObject.setObjectDetails(update.getObjectDetails());
					dirty = true;

				}
				if (dirty) {
					this.objects.save(updatedObject);
				}
			} else {
				throw new UnauthorizedAccessException("Updating objects can be done by OPERATOR only");
			}

			return this.converter.toBoundary(updatedObject);
		}

		else {
			throw new ObjectNotFoundException("Could not find object by id: " + objectId);
		}

	}

	// @Override
	// @Transactional(readOnly = true)
	@Deprecated
	public List<ObjectBoundary> getAll(String userSystemID, String userEmail) {
		throw new RuntimeException("Deprecated operation - use getAll that uses pagination");
		// return this.objects
		// .findAll()
		// .stream()
		// .map(this.converter::toBoundary)
		// .toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAll(String userSystemID, String userEmail, int page, int size) {
		this.logger.trace("getAllObjects( userSystemID: " + userSystemID + " userEmail: " + userEmail + " page: " + page
				+ " size: " + size + ")");
		RoleEnum role = getUserRole(userSystemID, userEmail);
		if (role == RoleEnum.OPERATOR) {
			return this.objects.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();

		}
		if (role == RoleEnum.END_USER) {
			return this.objects
					.findAllByActive(true,PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		}
		throw new UnauthorizedAccessException(
				"Access to the objects list is restricted to users with the roles of OPERATOR or END_USER only");
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificObject(String userSystemID, String userEmail, String objectSystemID,
			String objectId) {
		this.logger.trace("getSpecificObject( userSystemID: " + userSystemID + " userEmail: " + userEmail
				+ " objectSystemID: " + objectSystemID + " objectId: " + objectId + ")");
		RoleEnum role = getUserRole(userSystemID, userEmail);
		if (role == RoleEnum.OPERATOR) {
			return this.objects.findById(objectId + "@@" + objectSystemID).map(this.converter::toBoundary);
		}
		if (role == RoleEnum.END_USER) {
			return this.objects.findByObjectIdAndActive(objectId + "@@" + objectSystemID,true).map(this.converter::toBoundary);
		}
		throw new UnauthorizedAccessException(
				"Access to a specific object is restricted to users with the roles of OPERATOR or END_USER only");

	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllObjects(String adminSystemID, String adminEmail) {
		this.logger.trace("deleteAllObjects( adminSystemID: " + adminSystemID + " adminEmail: " + adminEmail + ")");
		RoleEnum role = getUserRole(adminSystemID, adminEmail);
		if (role == RoleEnum.ADMIN) {
			this.objects.deleteAll();
		} else {
			throw new UnauthorizedAccessException("Only the ADMIN is allowed to delete the objects list");
		}

	}

	public boolean validEmail(String email) {
		// Get an instance of the EmailValidator
		EmailValidator emailValidator = EmailValidator.getInstance();

		// Validate the email
		return emailValidator.isValid(email);
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
			throw new UserNotFoundException(
					"User not found with email: " + userEmail + "and systemID: " + userSystemID);
		}
		return user.get().getRole();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByExactAlias(String alias, String userSystemID, String userEmail, int page,
			int size) {
		this.logger.trace("getAllObjectsByExactAlias( alias: " + alias + " userSystemID: " + userSystemID
				+ " userEmail: " + userEmail + " page: " + page + " size: " + size + ")");
		RoleEnum role = getUserRole(userSystemID, userEmail);
		if (role == RoleEnum.OPERATOR) {
			return this.objects
					.findAllByAlias(alias, PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else if (role == RoleEnum.END_USER) {
			return this.objects
					.findAllByAliasAndActive(alias,true,
							PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else {
			throw new UnauthorizedAccessException(
					"Access to a specific object is restricted to users with the roles of OPERATOR or END_USER only");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByAliasPattern(String pattern, String userSystemID, String userEmail,
			int page, int size) {
		this.logger.trace("getAllObjectsByAliasPattern( pattern: " + pattern + " userSystemID: " + userSystemID
				+ " userEmail: " + userEmail + " page: " + page + " size: " + size + ")");
		RoleEnum role = getUserRole(userSystemID, userEmail);
		if (role == RoleEnum.OPERATOR) {
			return this.objects
					.findAllByAliasLike("%" + pattern + "%",
							PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else if (role == RoleEnum.END_USER) {
			return this.objects
					.findAllByAliasLikeAndActive("%" + pattern + "%",true,
							PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else {
			throw new UnauthorizedAccessException(
					"Access to a specific object is restricted to users with the roles of OPERATOR or END_USER only");

		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByType(String type, String systemID, String email, int page, int size) {
		this.logger.trace("getAllObjectsByType( type: " + type + " systemID: " + systemID + " email: " + email
				+ " page: " + page + " size: " + size + ")");
		RoleEnum role = getUserRole(systemID, email);
		if (role == RoleEnum.OPERATOR) {
			return this.objects
					.findAllByType(type, PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else if (role == RoleEnum.END_USER) {
			return this.objects
					.findAllByTypeAndActive(type,true, PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else {
			throw new UnauthorizedAccessException(
					"Access to a specific object is restricted to users with the roles of OPERATOR or END_USER only");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByTypeAndStatus(String type, String status, String systemID, String email,
			int page, int size) {
		this.logger.trace("getAllObjectsByTypeAndStatus( type: " + type + " status: " + status + " systemID: "
				+ systemID + " email: " + email + " page: " + page + " size: " + size + ")");
		RoleEnum role = getUserRole(systemID, email);
		if (role == RoleEnum.OPERATOR) {
			return this.objects
					.findAllByTypeAndStatus(type, status,
							PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else if (role == RoleEnum.END_USER) {
			return this.objects
					.findAllByTypeAndStatusAndActive(type, status,true,
							PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
					.stream().map(this.converter::toBoundary).toList();
		} else {
			throw new UnauthorizedAccessException(
					"Access to a specific object is restricted to users with the roles of OPERATOR or END_USER only");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getObjectByLocation(double lat, double lng, double distance, String units,
			String systemID, String email, int page, int size) {
		this.logger.trace("getObjectByLocation( lat: " + lat + " lng: " + lng + " distance: " + distance + " units: "
				+ units + " systemID: " + systemID + " email: " + email + " page: " + page + " size: " + size + ")");
		RoleEnum role = getUserRole(systemID, email);
		PageRequest pageRequest = PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId");
		if (role == RoleEnum.OPERATOR) {
			return this.objects.findWithinCircle(lat, lng, distance, units, pageRequest).stream()
					.map(this.converter::toBoundary).toList();
		} else if (role == RoleEnum.END_USER) {
			return this.objects.findWithinCircleAndActive(lat, lng, distance, units, pageRequest).stream()
					.map(this.converter::toBoundary).toList();
		} else {
			throw new UnauthorizedAccessException(
					"Access to a specific object is restricted to users with the roles of OPERATOR or END_USER only");
		}
	}

}
