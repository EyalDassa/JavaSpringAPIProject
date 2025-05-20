package aii.Boundary;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "systemID", "email" }) // Define the desired order
public class UserId {
	private String systemID;
	private String email;
	
	public UserId() {
	}

	public UserId(String systemId, String email) {
		this.systemID = systemId;
		this.email = email;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemId) {
		this.systemID = systemId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId that = (UserId) o;
        return Objects.equals(systemID, that.systemID) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, email);
    }

	@Override
	public String toString() {
		return "UserId [systemId=" + systemID + ", email=" + email + "]";
	}
	
}

