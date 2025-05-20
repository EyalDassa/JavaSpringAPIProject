package aii.Boundary;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "systemID", "id" }) 
public class ObjectId {
	private String systemID;
	private String id;

	public ObjectId() {
	}

	public ObjectId(String systemId, String id) {
		this.systemID = systemId;
		this.id = id;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemId) {
		this.systemID = systemId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectId that = (ObjectId) o;
        return Objects.equals(systemID, that.systemID) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, id);
    }

	@Override
	public String toString() {
		return "ObjectId [systemId=" + systemID + ", id=" + id + "]";
	}

}
