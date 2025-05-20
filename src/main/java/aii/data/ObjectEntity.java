package aii.data;
import java.util.Date;
import java.util.Map;

import aii.Utility.MapToStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "OBJECTS")
public class ObjectEntity {
	@Id
	private String objectId;
	
	private String type;
	
	@Column(name = "alias") 
	private String alias;
	
	private String status;
	
	private double lng;

	private double lat;
	
	private boolean active;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTimestamp;
	
	private String createdBy;
	
	@Lob
	@Convert(converter = MapToStringConverter.class)
	private Map<String, Object> objectDetails;
	
	public ObjectEntity() {
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
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

	public double getLocationlng() {
		return lng;
	}
	public double getLocationlat() {
		return lat;
	}

	public void setLocationlng(double locationlng) {
		this.lng = locationlng;
	}
	public void setLocationlat(double locationlat) {
		this.lat = locationlat;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}
	
}
