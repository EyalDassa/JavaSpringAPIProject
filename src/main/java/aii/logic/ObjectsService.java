package aii.logic;

import java.util.List;
import java.util.Optional;

import aii.Boundary.ObjectBoundary;

public interface ObjectsService {
	   // Create a new object
    public ObjectBoundary create(String userSystemID, String userEmail, ObjectBoundary object);
    
    // Update an existing object
    public ObjectBoundary update(String userSystemID, String userEmail, String objectSystemID, String objectId, ObjectBoundary update);
    
    // Get all objects for a user
    @Deprecated
    public List<ObjectBoundary> getAll(String userSystemID, String userEmail);
    
    // Get a specific object by ID
    public Optional<ObjectBoundary> getSpecificObject(String userSystemID, String userEmail, String objectSystemID, String objectId);
    
    // Delete all objects (admin operation)
    public void deleteAllObjects(String adminSystemID, String adminEmail);
}
