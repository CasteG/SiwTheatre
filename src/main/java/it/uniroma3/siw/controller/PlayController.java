package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.Play.DIR_ADMIN_PAGES_PLAY;
import static it.uniroma3.siw.model.Play.DIR_FOLDER_IMG;
import static it.uniroma3.siw.model.Play.DIR_PAGES_PLAY;

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

import it.uniroma3.siw.controller.validator.PlayValidator;
import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.service.ActorService;
import it.uniroma3.siw.service.PlayService;
import it.uniroma3.siw.utility.FileStore;
import jakarta.validation.Valid;

@Controller
public class PlayController {
	
	@Autowired
	private PlayService playService;
	
	@Autowired
	private PlayValidator playValidator;
	
	@Autowired
	private ActorService actorService;
	
	/* METHODS GENERIC_USER */
	
	@GetMapping("/play/{id}")
	public String getPlay(@PathVariable("id") Long id, Model model) {
		Play play = this.playService.findById(id);
		model.addAttribute("play", play);
		
		return DIR_PAGES_PLAY + "play";
	}
	
	@GetMapping("/plays")
	public String getPlays(Model model) {
		model.addAttribute("plays", this.playService.findAll());
		return DIR_PAGES_PLAY + "playsList";
	}
	
	/* METHODS ADMIN */
	
	@GetMapping("/admin/plays/{id}")
	public String getActorPlays(@PathVariable("id") Long id, Model model) {
		model.addAttribute("plays", this.actorService.findById(id).getPlays());
		model.addAttribute("idActor", id);
		return DIR_ADMIN_PAGES_PLAY + "adminPlaysList";
	}
	
	// --- INSERIMENTO
	
	@GetMapping("/admin/play/add/{id}")
	public String selectPlay(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("play", new Play());
		
		return DIR_ADMIN_PAGES_PLAY + "playForm";
	}
	
	@PostMapping("/admin/play/add/{id}")
	public String addPlay(@Valid @ModelAttribute("play") Play play, 
							  BindingResult bindingResult, 
							  @PathVariable("id") Long id,
							  @RequestParam("file") MultipartFile file,
							  Model model) {
		
		Actor actor = actorService.findById(id);
		play.setActor(actor);
		this.playValidator.validate(play, bindingResult);
		play = playService.newPlay(play);
		if(!bindingResult.hasErrors()) {
			play.setImg(FileStore.store(file,DIR_FOLDER_IMG));
			this.actorService.addPlay(actor, play);
			
			return "redirect:/admin/plays/" + id;
		}
		
		model.addAttribute("id", id);
		return DIR_ADMIN_PAGES_PLAY + "playForm";
	}
	
	// --- ELIMINAZIONE
	
	@GetMapping("/admin/play/delete/{id}")
	public String deletePlay(@PathVariable("id") Long id, Model model) {
		Play play = this.playService.findById(id);
		Actor actor = this.actorService.findById(play.getActor().getId());
		actor.getPlays().remove(play);
		this.playService.delete(play);
		this.actorService.save(actor);
		
		return "redirect:/admin/plays/" + actor.getId();
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/play/edit/{id}")
	public String getEditPlay(@PathVariable("id") Long id, Model model) {
		Play play = this.playService.findById(id);
		model.addAttribute("play", play);
		
		return DIR_ADMIN_PAGES_PLAY + "editPlay";
	}
	
	@PostMapping("/admin/play/edit/{id}")
	public String editPlay(@Valid @ModelAttribute("play") Play play, 
							   BindingResult bindingResult, @PathVariable("id") Long id, 
							   Model model) {
		
		Play p = this.playService.findById(id);
		play.setActor(p.getActor());
		
		if(play.getName().equals(p.getName())) {
			play.setName("namePlayDef");
			this.playValidator.validate(play, bindingResult);
			play.setName(p.getName());
		}else {
			this.playValidator.validate(play, bindingResult);
		}
		
		play.setId(id);
		if(!bindingResult.hasErrors()) {
			this.playService.update(p, play);
			
			return "redirect:/admin/plays/" + play.getActor().getId();
		}
		play.setImg(p.getImg());
		return DIR_ADMIN_PAGES_PLAY + "editPlay";
	}
	
	@PostMapping("/admin/play/changeImg/{idP}")
	public String changeImgChef(@PathVariable("idP") Long idP,
			   					@RequestParam("file") MultipartFile file, 
			   					Model model) {
		
		Play p = this.playService.findById(idP);
		if(!p.getImg().equals("profiles")) {
			FileStore.removeImg(DIR_FOLDER_IMG, p.getImg());
		}

		p.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		this.playService.save(p);
		return this.getEditPlay(idP, model);
	}
	
}
