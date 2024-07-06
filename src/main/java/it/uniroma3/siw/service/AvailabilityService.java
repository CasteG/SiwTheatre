package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;
import it.uniroma3.siw.repository.AvailabilityRepository;
import jakarta.transaction.Transactional;

@Service
public class AvailabilityService {

	@Autowired
	private AvailabilityRepository availabilityRepository;
	
	public boolean alreadyExists(Availability target) {
		return this.availabilityRepository.existsByDateAndStartTimeAndEndTimeAndActor(target.getDate(), target.getStartTime(), target.getEndTime(), target.getActor());
	}

	public Availability findById(Long idAvailability) {
		return this.availabilityRepository.findById(idAvailability).get();
	}
	
//	public List<Availability> findByActorAndActive(Actor actor) {
//		return this.availabilityRepository.findByActorAndActiveTrueOrderByDateAndStartTime(actor);
//	}
	
	public List<Availability> findByActor(Actor actor) {
		return this.availabilityRepository.findByActor(actor);
	}
	
	public Availability newAvailability(Availability a) {
		Availability na = new Availability();
		na.setActive(a.getActive());
		na.setDate(a.getDate());
		na.setActor(a.getActor());
		na.setEndTime(a.getEndTime());
		na.setStartTime(a.getStartTime());
		return this.availabilityRepository.save(na);
	}
	
	@Transactional
	public void update(Availability availability, Availability newAvailability) {
		availability.setDate(newAvailability.getDate());
		availability.setStartTime(newAvailability.getStartTime());
		availability.setEndTime(newAvailability.getEndTime());
		this.availabilityRepository.save(availability);
	}
	
	@Transactional
	public void delete(Availability availability) {
		this.availabilityRepository.delete(availability);
	}

}
