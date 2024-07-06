package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.model.User;

public interface BookingRepository extends CrudRepository<Booking, Long>{

	public Iterable<Booking> findByUser(User user);

	public boolean existsByUserAndPlay(User user, Play play);

}
