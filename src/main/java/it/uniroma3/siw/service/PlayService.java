package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Actor;
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
	
	public boolean alreadyExists(Play target) {
		return this.playRepository.existsByNameAndActor(target.getName(), target.getActor());
	}
	
	@Transactional
	public void delete(Play play) {
		this.playRepository.delete(play);
	}
	
	@Transactional
	public void update(Play play, Play newPlay) {
		play.setName(newPlay.getName());
		play.setDescription(newPlay.getDescription());
		play.setPrice(newPlay.getPrice());
		this.playRepository.save(play);
	}
	
	public Play newPlay(Play p) {
		Play np = new Play();
		np.setDescription(p.getDescription());
		np.setActor(p.getActor());
		np.setImg(p.getImg());
		np.setName(p.getName());
		np.setPrice(p.getPrice());
		return this.playRepository.save(np);
	}

	public Play findById(Long id) {
		return this.playRepository.findById(id).get();
	}
	
	public List<Play> findByActor(Actor actor) {
		return this.playRepository.findByActor(actor);
	}

	public List<Play> findAll() {
		return (List<Play>) this.playRepository.findAll();
	}
	
	public List<Play> findLastPlays() {
		return this.playRepository.findTopN(6);
	}

}
