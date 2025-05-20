package aii.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import aii.data.ObjectEntity;

public interface ObjectsCrud extends JpaRepository<ObjectEntity, String> {

	public List<ObjectEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByAliasLike(@Param("alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByType(@Param("type") String type, Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndStatus(@Param("type") String type, @Param("status") String status,
			Pageable pageable);

	@Query("SELECT o FROM ObjectEntity o " + "WHERE (:unit = 'KILOMETERS' AND "
			+ "       6371 * acos(cos(radians(:lat)) * cos(radians(o.lat)) * "
			+ "       cos(radians(o.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.lat))) <= :distance) "
			+ "   OR (:unit = 'MILES' AND " + "       3958.8 * acos(cos(radians(:lat)) * cos(radians(o.lat)) * "
			+ "       cos(radians(o.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.lat))) <= :distance) "
			+ "   OR (:unit = 'NEUTRAL' AND " + "       acos(cos(radians(:lat)) * cos(radians(o.lat)) * "
			+ "       cos(radians(o.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.lat))) <= :distance)")
	List<ObjectEntity> findWithinCircle(@Param("lat") double lat, @Param("lng") double lng,
			@Param("distance") double distance, @Param("unit") String unit, Pageable pageable);

	public List<ObjectEntity> findAllByActive(@Param("active") boolean active, Pageable pageable);

	public Optional<ObjectEntity> findByObjectIdAndActive(@Param("Id") String id, @Param("active") boolean active);

	public List<ObjectEntity> findAllByAliasAndActive(@Param("alias") String alias, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByAliasLikeAndActive(@Param("alias") String alias, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndActive(@Param("type") String type, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndStatusAndActive(@Param("type") String type,
			@Param("status") String status, @Param("active") boolean active, Pageable pageable);

	@Query("SELECT o FROM ObjectEntity o " + "WHERE o.active = true AND ((:unit = 'KILOMETERS' AND "
			+ "       6371 * acos(cos(radians(:lat)) * cos(radians(o.lat)) * "
			+ "       cos(radians(o.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.lat))) <= :distance) "
			+ "   OR (:unit = 'MILES' AND " + "       3958.8 * acos(cos(radians(:lat)) * cos(radians(o.lat)) * "
			+ "       cos(radians(o.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.lat))) <= :distance) "
			+ "   OR (:unit = 'NEUTRAL' AND " + "       acos(cos(radians(:lat)) * cos(radians(o.lat)) * "
			+ "       cos(radians(o.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(o.lat))) <= :distance))")
	List<ObjectEntity> findWithinCircleAndActive(@Param("lat") double lat, @Param("lng") double lng,
			@Param("distance") double distance, @Param("unit") String unit, Pageable pageable);
}
