package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;
import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.model.User;

public interface BookingRepository extends CrudRepository<Booking, Long>{

	public boolean existsByActorAndPlayAndAvailabilityAndUser(Actor actor, Play play, Availability availability, User user);

}
