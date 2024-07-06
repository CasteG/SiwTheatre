package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;
import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.repository.ActorRepository;
import jakarta.transaction.Transactional;

@Service
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;
	

	public Actor findById(Long id) {
		return actorRepository.findById(id).get();
	}

	@Transactional
	public void save(Actor actor) {
		actorRepository.save(actor);
	}
	
	public List<Actor> findAll() {
		return (List<Actor>) actorRepository.findAll();
	}
	
	@Transactional
	public void delete(Actor actor) {
		this.actorRepository.delete(actor);
	}
	
	@Transactional
	public void update(Actor actor, Long id) {
		Actor p = this.actorRepository.findById(id).get();
		p.setName(actor.getName());
		p.setSurname(actor.getSurname());
		p.setRole(actor.getRole());
		this.actorRepository.save(p);
	}
	
	@Transactional
	public void addPlay(Actor actor, Play play) {
		actor.getPlays().add(play);
		this.actorRepository.save(actor);
	}
	
	@Transactional
	public void addAvailability(Actor actor, Availability availability) {
		actor.getAvailability().add(availability);
		this.actorRepository.save(availability);
	}

	public List<Actor> findLastActors() {
		return this.actorRepository.findTopN(6);
	}

	public boolean alreadyExists(Actor target) {
		return this.actorRepository.existsById(target.getId());
	}


}
