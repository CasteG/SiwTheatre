package it.uniroma3.siw.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Play;

public interface PlayRepository extends CrudRepository<Play, Long> {
	
	
	public Play findByName(String name);

	public boolean existsByNameAndDateAndCity(String name, LocalDate date, String city);
}
