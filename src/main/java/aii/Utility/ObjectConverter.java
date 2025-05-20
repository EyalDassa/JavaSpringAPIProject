package aii.Utility;
import org.springframework.stereotype.Component;

import aii.Boundary.CreatedBy;
import aii.Boundary.Location;
import aii.Boundary.ObjectBoundary;
import aii.Boundary.ObjectId;
import aii.Boundary.UserId;
import aii.data.ObjectEntity;

@Component
public class ObjectConverter {
	
	public ObjectEntity toEntity (ObjectBoundary boundary) {
		ObjectEntity entity = new ObjectEntity();
		entity.setObjectId(boundary.getObjectId().getId()+ "@@"+ boundary.getObjectId().getSystemID());
		entity.setType(boundary.getType());
		entity.setAlias(boundary.getAlias());
		entity.setStatus(boundary.getStatus());
		if(boundary.getLocation()!= null)
		{
			entity.setLocationlat(boundary.getLocation().getLat());
			entity.setLocationlng(boundary.getLocation().getLng());
		}
		if (boundary.getActive() != null) {
			entity.setActive(boundary.getActive());
		}else {
			entity.setActive(false);
		}
		entity.setCreationTimestamp(boundary.getCreationTimestamp());
		entity.setCreatedBy(boundary.getCreatedBy().getUserId().getEmail()+"@@"+boundary.getCreatedBy().
				getUserId()
				.getSystemID());
		entity.setObjectDetails(boundary.getObjectDetails());
		return entity;	
	}
	
	public ObjectBoundary toBoundary (ObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		
		String[] partsID = entity.getObjectId().split("@@");
		boundary.setObjectId(new ObjectId(partsID[1],
				partsID[0]));
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setStatus(entity.getStatus());
		boundary.setLocation(new Location(entity.getLocationlat(),entity.getLocationlng()));
		boundary.setActive(entity.getActive());
		
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		String[] partsCreatedBy = entity.getCreatedBy().split("@@");
		boundary.setCreatedBy(new CreatedBy(new UserId(
				partsCreatedBy[1],partsCreatedBy[0])));
		
		boundary.setObjectDetails(entity.getObjectDetails());
		
		return boundary;
	}

}

