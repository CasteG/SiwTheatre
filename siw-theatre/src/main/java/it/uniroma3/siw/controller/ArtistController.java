package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;

@Controller
public class ArtistController {
	@Autowired ArtistService artistService;
	
	@GetMapping("/artist")
	public String showArtists(Model model) {
		model.addAttribute("artists", artistService.findAll());
		return "artists.html";
	}
	
	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", artistService.findById(id));
		return "artist.html";
	}
	
	@GetMapping("/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "formNewArtist";
	}
	
	@PostMapping("/artist")
	public String newArtist(@ModelAttribute("artist") Artist artist) {
		this.artistService.save(artist);
		return "redirect:artist/"+artist.getId();
	}
	
}
