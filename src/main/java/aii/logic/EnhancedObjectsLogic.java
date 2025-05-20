package aii.logic;

import java.util.List;

import aii.Boundary.ObjectBoundary;

public interface EnhancedObjectsLogic extends ObjectsService{
	public List<ObjectBoundary> getAll(String userSystemID, String userEmail,int page , int size);
	
	public List<ObjectBoundary> getAllObjectsByExactAlias(String alias, String userSystemID, String userEmail,int page , int size);
	public List<ObjectBoundary> getAllObjectsByAliasPattern(String alias, String userSystemID, String userEmail,int page , int size);

	public List<ObjectBoundary> getAllObjectsByType(String type, String systemID, String email, int page, int size);

	public List<ObjectBoundary> getAllObjectsByTypeAndStatus(String type, String status, String systemID, String email,
			int page, int size);

	public List<ObjectBoundary> getObjectByLocation(double lat, double lng, double distance, String units,
			String systemID, String email, int page, int size);


}
