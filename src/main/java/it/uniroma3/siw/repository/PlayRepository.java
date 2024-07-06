package it.uniroma3.siw.repository;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Play;

public interface PlayRepository extends CrudRepository<Play, Long> {
	
	
	public Play findByName(String name);

	public boolean existsByNameAndDate(String name, LocalDateTime date);
}
