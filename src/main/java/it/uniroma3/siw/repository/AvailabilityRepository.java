package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;

public interface AvailabilityRepository extends CrudRepository<Availability, Long> {

	public List<Availability> findByActor(Actor actor);
	
	//public List<Availability> findByActorAndActiveTrueOrderByDateAndStartTime(Actor actor);

	public boolean existsByDateAndStartTimeAndEndTimeAndActor(String date, String startTime, String endTime, Actor actor);

}
