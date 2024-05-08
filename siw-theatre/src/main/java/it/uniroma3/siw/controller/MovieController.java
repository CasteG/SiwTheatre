package it.uniroma3.siw.controller;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;

@Controller
public class MovieController {
	@Autowired MovieService movieService;
	@Autowired ArtistService artistService;
	
	@GetMapping("/indexMovie")
	public String indexMovie(Model model) {
		return "indexMovie.html";
	}
	
	@GetMapping("/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "manageMovies.html";
	}
	
	@GetMapping("/formUpdateMovie/{id}")
	public String updateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "formUpdateMovie.html";
	}

	@GetMapping("/addDirector/{idMovie}")
	public String addDirector(@PathVariable("idMovie") Long id, Model model) {
		model.addAttribute("movie",this.movieService.findById(id));
		model.addAttribute("directors", this.artistService.findAll());
		return "addDirector.html";
	}

	@GetMapping("/addActors/{idMovie}")
	public String addActors(Model model, @PathVariable("idMovie") Long id) {
		model.addAttribute("movie", this.movieService.findById(id));
		model.addAttribute("actors", this.artistService.findAll());
		return "addActors.html";
	}
	
	@GetMapping("/setDirectorToMovie/{idDirector}/{idMovie}")
	public String setDirectorToMovie(Model model, @PathVariable("idDirector") Long idDir, @PathVariable("idMovie") Long idMov) {
		Movie movie = this.movieService.findById(idMov);
		movie.setDirector(this.artistService.findById(idDir));
		this.movieService.save(movie);
		model.addAttribute("movie", movie);		//devo passare movie alla vista formUpdateMovie
		return "formUpdateMovie.html";
	}
	
	@GetMapping("/setActorToMovie/{idActor}/{idMovie}")
	public String setActorToMovie(Model model, @PathVariable("idActor") Long idAct, @PathVariable("idMovie") Long idMov) {
		Movie movie = this.movieService.findById(idMov);
		Artist actor = this.artistService.findById(idAct);
		movie.getActors().add(actor);
		this.movieService.save(movie);
		model.addAttribute("movie", movie);
		model.addAttribute("actors", this.artistService.findAvailableArtists(movie.getActors()));
		return "addActors.html";
	}
	
	@GetMapping("/removeActorFromMovie/{idActor}/{idMovie}")
	public String removeActorFromMovie(Model model,@PathVariable("idActor") Long idAct, @PathVariable("idMovie") Long idMov) {
		Movie movie = this.movieService.findById(idMov);
		movie.getActors().remove(this.artistService.findById(idAct));
		this.movieService.save(movie);
		model.addAttribute("movie", movie);
		model.addAttribute("actors", this.artistService.findAvailableArtists(movie.getActors()));
		return "addActors.html";
	}
	
	@GetMapping("/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "formNewMovie.html";
	}
	
	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findById(id));
		return "movie.html";
	}
	
	@GetMapping("/movie")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAll());
		return "movies.html";
	}

	@PostMapping("/movie")
	public String newMovie(@ModelAttribute("movie") Movie movie) {
		this.movieService.save(movie); 
		return "redirect:movie/"+ movie.getId();	//dico al client fammi una richiesta all'url movie/{id} 
	}
	
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer year) {
		model.addAttribute("movies", this.movieService.findByYear(year));
		return "movies.html";
	}
	

}
