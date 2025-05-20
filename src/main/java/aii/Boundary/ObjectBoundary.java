package aii.Boundary;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectBoundary {
	private ObjectId objectId;
	private String type;
	private String alias;
	private String status;
	private Location location;
	private Boolean active;
	private Date creationTimestamp;
	private CreatedBy createdBy;
	private Map<String, Object> objectDetails;

	public ObjectBoundary() {
	}

	public ObjectBoundary(ObjectId objectId, String type, String alias, String status, Location location,
			Boolean active, CreatedBy createdBy, Map<String, Object> objectDetails) {
		this.objectId=objectId;
		//this.objectId = new ObjectId(objectId.getSystemID(), objectId.getId());

		this.type = type;
		this.alias = alias;
		this.status = status;

		this.location = new Location(location.getLat(), location.getLng());

		this.active = active;
		this.creationTimestamp = new Date();

		this.createdBy = new CreatedBy(createdBy.getUserId());

		this.objectDetails = new HashMap<>();

	}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimeStamp) {
		this.creationTimestamp = creationTimeStamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

}
