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
import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.PlayService;
import it.uniroma3.siw.validator.PlayValidator;
import jakarta.validation.Valid;

@Controller
public class PlayController {

	@Autowired
	private PlayService playService;

	@Autowired
	private PlayValidator playValidator;

	@Autowired
	private ArtistService artistService;

	@Autowired
	private ImageRepository imageRepository;


	@GetMapping("/plays/{id}")
	public String getPlay(@PathVariable("id") Long id, Model model) {
		Play play = this.playService.findById(id);
		model.addAttribute("play", play);
		model.addAttribute("booking", new Booking());
		return "play.html";
	}

	@GetMapping("/plays")
	public String showPlays(Model model) {
		model.addAttribute("plays", this.playService.findAll());
		return "plays.html";
	}

	@GetMapping("/formSearchPlay")
	public String formSearchPlays() {
		return "formSearchPlay.html";
	}

	@PostMapping("/searchPlay")
	public String searchPlays(Model model, @RequestParam String name) {
		model.addAttribute("plays", this.playService.findByName(name));
		return "plays.html";
	}


	/*********************************************************************************************/
	/**************************************** ADMIN **********************************************/
	/**********^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^**********************************************/

	@PostMapping("/plays")
	public String newPlay(@Valid @ModelAttribute("play") Play play, 
			BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile image) throws IOException {

		this.playValidator.validate(play, bindingResult);

		if(bindingResult.hasErrors()) {
			model.addAttribute("play", play);
			model.addAttribute("artists", this.artistService.findAll());
			return "admin/formNewPlay.html";
		}

		else {
			Image img = new Image(image.getBytes());
			this.imageRepository.save(img);
			play.setImage(img);
			this.playService.save(play); 
			model.addAttribute("play", play);
			return "redirect:plays/"+play.getId();	//dico al client fammi una richiesta all'url recipe/{id} 
		}

	}

	@GetMapping("/admin/formNewPlay")
	public String formNewPlay(Model model) {
		model.addAttribute("play", new Play());
		model.addAttribute("artists", this.artistService.findAll());
		return "admin/formNewPlay.html";
	}

	@GetMapping("/admin/managePlays")
	public String managePlays(Model model) {
		model.addAttribute("plays", this.playService.findAll());
		return "admin/managePlays.html";
	}

	@GetMapping("/admin/formUpdatePlay/{id}")
	public String getUpdatePlay(@PathVariable("id") Long id, Model model) {
		model.addAttribute("play", this.playService.findById(id));
		return "admin/formUpdatePlay.html";
	}

	@PostMapping("/admin/formUpdatePlay/{id}")
	public String updatePlay(@Valid @ModelAttribute("play") Play play, Model model,
			@RequestParam("file") MultipartFile image, @PathVariable("id") Long id) throws IOException {
		//TODO: implement binding and validation (excluding duplicate check)
		Image img = new Image(image.getBytes());
		//this.imageRepository.delete(play.getImage());
		this.imageRepository.save(img);
		play.setImage(img);  // Set new image to existing play
		this.playService.save(play);  // Save the updated play
		model.addAttribute("play", play);
		return "redirect:/plays/" + play.getId();  // Redirect to the updated play
	}


	@GetMapping("/admin/removePlay/{id}")
	public String removePlay(@PathVariable("id") Long id, Model model) {
		this.playService.remove(this.playService.findById(id));
		return "admin/successfulRemoval.html";
	}

	@GetMapping("/admin/updatePlayArtists/{idPlay}")
	public String updatePlayArtists(Model model, @PathVariable("idPlay") Long id) {
		Play play = this.playService.findById(id);
		model.addAttribute("play", play);
		model.addAttribute("artists", this.artistService.findAvailableArtists(play.getArtists()));
		return "admin/updatePlayArtists.html";
	}

	@GetMapping("/admin/setArtistToPlay/{idArtist}/{idPlay}")
	public String setArtistToPlay(Model model, @PathVariable("idArtist") Long idArtist, @PathVariable("idPlay") Long idPlay) {
	    Play play = this.playService.findById(idPlay);
	    Artist artist = this.artistService.findById(idArtist);
	    
	    play.getArtists().add(artist);
	    artist.getPlays().add(play);
	    this.playService.save(play);
	    this.artistService.save(artist); 
	    
	    model.addAttribute("play", play);
	    model.addAttribute("artists", this.artistService.findAvailableArtists(play.getArtists()));
	    return "admin/updatePlayArtists";
	}
	
	@GetMapping("/admin/removeArtistFromPlay/{idArt}/{idPlay}")
	public String removeArtistFromPlay(Model model, @PathVariable("idArt") Long idArt, @PathVariable("idPlay") Long idPlay) {
	    Play play = this.playService.findById(idPlay);
	    Artist artist = this.artistService.findById(idArt);
	    
	    play.getArtists().remove(artist);
	    artist.getPlays().remove(play);
	    this.playService.save(play);
	    this.artistService.save(artist); 
	    
	    model.addAttribute("play", play);
	    model.addAttribute("artists", this.artistService.findAvailableArtists(play.getArtists()));
	    return "admin/updatePlayArtists";
	}


}
