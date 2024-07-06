package it.uniroma3.siw.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import jakarta.transaction.Transactional;

@Service
public class ArtistService {

	@Autowired
	private ArtistRepository artistRepository;
	

	public Artist findById(Long id) {
		return artistRepository.findById(id).get();
	}

	@Transactional
	public void save(Artist artist) {
		artistRepository.save(artist);
	}
	
	public List<Artist> findAll() {
		return (List<Artist>) artistRepository.findAll();
	}
	
	@Transactional
	public void delete(Artist artist) {
		this.artistRepository.delete(artist);
	}
	
	public void remove(Artist artist) {
		this.artistRepository.delete(artist);
	}

	/* restituisce solo gli artisti che non sono presenti nella lista passata */
	public Set<Artist> findAvailableArtists(Set<Artist> playArtists) {
		
		Set<Artist> availableArtists = new HashSet<>();
		
		/* per ogni artista trovato nel sistema, se non è contenuto nella 
		 * lista di artisti dello spettacolo allora aggiungilo alla lista 
		 * di artisti disponibili  */
		for (Artist artist : this.artistRepository.findAll()) {
			if (!playArtists.contains(artist)) {
				availableArtists.add(artist);
			}
		}
		
        return availableArtists;
	}

	public boolean alreadyExists(Artist target) {
		return this.artistRepository.existsByNameAndSurname(target.getName(), target.getSurname());
	}

}
