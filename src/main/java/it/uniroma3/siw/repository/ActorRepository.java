package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;

public interface ActorRepository extends CrudRepository<Actor, Long> {

	@Query(value = "SELECT * FROM actor order by id limit :limit", nativeQuery = true)
	public List<Actor> findTopN(@Param("limit") int limit);

	public void save(Availability availability);
	

}