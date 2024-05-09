package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	public Movie findById(Long id) {
		return movieRepository.findById(id).get();
	}
	
	public Iterable<Movie> findAll() {
		return movieRepository.findAll();
	}

	public void save(Movie movie) {
		movieRepository.save(movie);
	}

	public List<Movie> findByYear(Integer year) {
		return movieRepository.findByYear(year);
	}


}
