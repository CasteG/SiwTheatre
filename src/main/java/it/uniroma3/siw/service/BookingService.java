package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	
	public Booking findById(Long id) {
		return this.bookingRepository.findById(id).get();
	}

	public Iterable<Booking> findAll() {
		return this.bookingRepository.findAll();
	}
	
	public Iterable<Booking> findByUser(User user) {
		return this.bookingRepository.findByUser(user);
	}

	public void save(Booking booking) {
		this.bookingRepository.save(booking);
	}

	public void remove(Booking booking) {
		this.bookingRepository.delete(booking);
	}

	public boolean alreadyExists(Booking target) {
		return this.bookingRepository.existsByUserAndPlay(target.getUser(), target.getPlay());
	}
	

}
