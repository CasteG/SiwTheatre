package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.repository.PlayRepository;
import jakarta.transaction.Transactional;

@Service
public class PlayService {

	@Autowired
	private PlayRepository playRepository;
	
	@Transactional
	public void save(Play play) {
		this.playRepository.save(play);
	}
	
	@Transactional
	public void delete(Play play) {
		this.playRepository.delete(play);
	}
	

	public Play findById(Long id) {
		return this.playRepository.findById(id).get();
	}
	
	
	public List<Play> findAll() {
		return (List<Play>) this.playRepository.findAll();
	}
	
	
	public Play findByName(String name) {
		return this.playRepository.findByName(name);
	}

	public void remove(Play play) {
		this.playRepository.delete(play);
	}

	public boolean alreadyExists(Play target) {
		return this.playRepository.existsByNameAndDateAndCity(target.getName(), target.getDate(), target.getCity());
	}

}
