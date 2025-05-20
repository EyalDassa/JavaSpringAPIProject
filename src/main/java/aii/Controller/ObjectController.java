package aii.Controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aii.Boundary.ObjectBoundary;
import aii.Exceptions.ObjectNotFoundException;
import aii.logic.EnhancedObjectsLogic;

@RestController
@RequestMapping(path = { "aii/objects" })
public class ObjectController {
	// private ObjectsService objects;
	private EnhancedObjectsLogic objects;

	public ObjectController(EnhancedObjectsLogic objects) {
		this.objects = objects;

	}

	public void deleteAllObjects(String adminSystemID, String adminEmail) {
		this.objects.deleteAllObjects(adminSystemID, adminEmail);
	}

	// TODO change empty strings to createdBy values

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary createObject(@RequestBody ObjectBoundary newObject) {
		return this.objects.create(newObject.getCreatedBy().getUserId().getSystemID(),
				newObject.getCreatedBy().getUserId().getEmail(), newObject);
	}

	@PutMapping(path = { "/{systemId}/{id}" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void updateObject(@RequestBody ObjectBoundary update, @PathVariable("systemId") String systemID,
			@PathVariable("id") String ID, 
			@RequestParam(name = "userSystemID", required = true) String userSystemId,
			@RequestParam(name = "userEmail", required = true) String email) {

		this.objects.update(userSystemId, email, systemID, ID, update);

	}

	@GetMapping(path = { "/{systemId}/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary getObject(@PathVariable("systemId") String systemID, @PathVariable("id") String ID,
			@RequestParam(name = "userSystemID", required = true) String userSystemId,
			@RequestParam(name = "userEmail", required = true) String email) {
		return this.objects.getSpecificObject(userSystemId, email, systemID, ID)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find object by id: " + ID));
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjects(@RequestParam(name = "userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.objects.getAll(systemID, email, page, size).toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "search/byAlias/{alias}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectsByExactAlias(@PathVariable("alias") String alias,
			@RequestParam(name = "userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.objects.getAllObjectsByExactAlias(alias, systemID, email, page, size)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "search/byAliasPattern/{pattern}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectsByAlias(@PathVariable("pattern") String pattern,
			@RequestParam(name = "userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.objects.getAllObjectsByAliasPattern(pattern, systemID, email, page, size)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "search/byType/{type}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectByType(@PathVariable("type") String type,
			@RequestParam(name = "userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.objects.getAllObjectsByType(type, systemID, email, page, size).toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "search/byTypeAndStatus/{type}/{status}" }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectByTypeAndStatus(@PathVariable("type") String type,
			@PathVariable("status") String status,
			@RequestParam(name = "userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.objects.getAllObjectsByTypeAndStatus(type, status, systemID, email, page, size)
				.toArray(new ObjectBoundary[0]);
	}
	@GetMapping(path = { "search/byLocation/{lat}/{lng}/{distance}" }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ObjectBoundary[] getObjectByLocation(@PathVariable("lat") double lat,
			@PathVariable("lng") double lng,@PathVariable("distance") double distance,
			@RequestParam(name = "units", required = false, defaultValue = "NEUTRAL") String units,
			@RequestParam(name = "userSystemID", required = true) String systemID,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		return this.objects.getObjectByLocation(lat,lng,distance,units, systemID, email, page, size)
				.toArray(new ObjectBoundary[0]);
	}

}
