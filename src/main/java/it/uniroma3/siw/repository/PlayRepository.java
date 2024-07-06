package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Play;

public interface PlayRepository extends CrudRepository<Play, Long> {
	
	public boolean existsByNameAndActor(String name, Actor actor);
	
	public List<Play> findByActor(Actor actor);
	
	@Query(value = "SELECT * FROM play order by id limit :limit", nativeQuery = true)
	public List<Play> findTopN(@Param("limit") int limit);
}
