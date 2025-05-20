package aii.Boundary;

public class CreatedBy {
	private UserId userId;
	
	public CreatedBy() {
	}

	public CreatedBy(UserId userId) {
		this.userId = new UserId(userId.getSystemID(), userId.getEmail());
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CreatedBy [userId=" + userId + "]";
	}
	
	
}
