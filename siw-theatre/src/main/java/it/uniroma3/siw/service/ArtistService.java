package it.uniroma3.siw.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;

@Service
public class ArtistService {

	@Autowired
	ArtistRepository artistRepository;

	public Iterable<Artist> findAll() {
		return artistRepository.findAll();
	}

	public Artist findById(Long id) {
		return artistRepository.findById(id).get();
	}

	public void save(Artist artist) {
		artistRepository.save(artist);
	}

	/* restituisce solo gli attori che non sono presenti nella lista passata */
	public Set<Artist> findAvailableArtists(Set<Artist> actors) {
		Set<Artist> availableActors = new HashSet<>();
        
        for (Artist artist : this.artistRepository.findAll()) {
            if (!actors.contains(artist)) {
                availableActors.add(artist);
            }
        }
        return availableActors;
	}
}
