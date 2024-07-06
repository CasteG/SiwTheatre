package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.validator.ArtistValidator;
import jakarta.validation.Valid;

@Controller
public class ArtistController {
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private ArtistValidator artistValidator;
	
	@Autowired
	private ImageRepository imageRepository;
	
	
	@GetMapping("/artist")
	public String showArtists(Model model) {
		model.addAttribute("artists", artistService.findAll());
		return "artists.html";
	}
	
	@GetMapping("/artists/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", artistService.findById(id));
		return "artist.html";
	}
	
	
	/********************************************************************/
	/************************** ADMIN ***********************************/
	/********************************************************************/

	
	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@PostMapping("/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, 
			BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile image) throws IOException {
		
		this.artistValidator.validate(artist, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			Image img = new Image(image.getBytes());
			this.imageRepository.save(img);
			artist.setImage(img);
			this.artistService.save(artist);
			model.addAttribute("artist", artist);
			return "redirect:artist/"+artist.getId();
		}
		else 
			model.addAttribute("artists", this.artistService.findAll());
			return "admin/formNewArtist.html";
		
	}
	
	@GetMapping("/admin/manageArtists")
	public String manageArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "admin/manageArtists.html";
	}
	
	@GetMapping("/admin/removeArtist/{id}")
	public String removeArtist(@PathVariable("id") Long id, Model model) {
		this.artistService.remove(this.artistService.findById(id));
		return "admin/successfulRemoval.html";
	}
	
}
