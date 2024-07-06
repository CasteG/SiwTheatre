package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	
	public boolean alreadyExists(Booking target) {
		return this.bookingRepository.existsByActorAndPlayAndAvailabilityAndUser(target.getActor(), target.getPlay(), target.getAvailability(), target.getUser());
	}
	
	public Booking findById(Long id) {
		return this.bookingRepository.findById(id).get();
	}

	public void delete(Booking b) {
		this.bookingRepository.delete(b);
	}
	

}
