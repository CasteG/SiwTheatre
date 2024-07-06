package it.uniroma3.siw.controller;


import static it.uniroma3.siw.model.Actor.DIR_ADMIN_PAGES_ACTOR;
import static it.uniroma3.siw.model.Actor.DIR_FOLDER_IMG;
import static it.uniroma3.siw.model.Actor.DIR_PAGES_ACTOR;

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

import it.uniroma3.siw.controller.validator.ActorValidator;
import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.service.ActorService;
import it.uniroma3.siw.utility.FileStore;
import jakarta.validation.Valid;

@Controller
public class ActorController {
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ActorValidator actorValidator;
	
	
	/* METHODS GENERIC_USER */
	@GetMapping("/actor/{id}")
	public String getActor(@PathVariable("id") Long id, Model model) {
		Actor actor = actorService.findById(id);
		model.addAttribute("actor", actor);
		return DIR_PAGES_ACTOR + "actor";
	}
	
	@GetMapping("/actor")
	public String getActors(Model model) {
		model.addAttribute("actors", this.actorService.findAll());
		
		return DIR_PAGES_ACTOR + "actorList";
	}
	
	
	/* METHODS ADMIN */	
	@GetMapping("/admin/actor/{id}")
	public String getAdminActor(@PathVariable("id") Long id, Model model) {
		Actor actor = actorService.findById(id);
		model.addAttribute("actor", actor);		
		return DIR_ADMIN_PAGES_ACTOR + "adminActor";
	}
	
	@GetMapping("/admin/actors")
	public String getAdminActors(Model model) {
		model.addAttribute("actors", this.actorService.findAll());		
		return DIR_ADMIN_PAGES_ACTOR + "adminActorList";
	}
	
	// --- INSERIMENTO
	@GetMapping("/admin/actor/add")
	public String addActor(Model model) {
		model.addAttribute("actor", new Actor());
		return DIR_ADMIN_PAGES_ACTOR + "actorForm";
	}
	
	@PostMapping("/admin/actor/add")
	public String addNewActor(@Valid @ModelAttribute("actor") Actor actor, 
									   BindingResult bindingResult, 
									   @RequestParam("file") MultipartFile file,
									   Model model) {
		
		this.actorValidator.validate(actor, bindingResult);
		if(!bindingResult.hasErrors()) {
			actor.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.actorService.save(actor);
			return this.getAdminActors(model);
		}
		return DIR_ADMIN_PAGES_ACTOR + "actorForm";
	}	
	
	
	// --- ELIMINAZIONE
	
	/* root permesso / entita / azione / parametri */
	@GetMapping("/admin/actor/delete/{id}")
	public String deleteActor(@PathVariable("id") Long id, Model model) {
		
		Actor actor = actorService.findById(id);
		FileStore.removeImg(DIR_FOLDER_IMG, actor.getImg());
		
		//eliminazione immagini a cascata
		actor.getPlays().stream().forEach(play -> play.deleteImage());
	    
		this.actorService.delete(actor);
		return this.getAdminActors(model);
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/actor/edit/{id}")
	public String getEditActor(@PathVariable("id") Long id, Model model) {
		model.addAttribute("actor",this.actorService.findById(id));	
		return DIR_ADMIN_PAGES_ACTOR + "editActor";
	}
	
	@PostMapping("/admin/actor/edit/{id}")
	public String editActor(@Valid @ModelAttribute("actor") Actor actor, 
							   BindingResult bindingResult, 
							   @PathVariable("id") Long id, 
							   Model model) {
		
		Actor actor1 = this.actorService.findById(id);
		this.actorValidator.validate(actor1, bindingResult);
		actor1.setId(id);
		if(!bindingResult.hasErrors()) {
			this.actorService.update(actor1, actor1.getId());
			return this.getAdminActors(model);
		}
		actor1.setImg(actor1.getImg());
		return  DIR_ADMIN_PAGES_ACTOR + "editActor";
	}
	
	@PostMapping("/admin/actor/changeImg/{idActor}")
	public String changeImgParr(@PathVariable("idActor") Long idActor,
			   					@RequestParam("file") MultipartFile file, 
			   					Model model) {
		
		Actor d = this.actorService.findById(idActor);
		if(!d.getImg().equals("profiles")) {
			FileStore.removeImg(DIR_FOLDER_IMG, d.getImg());
		}
		d.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		this.actorService.save(d);
		return this.getEditActor(idActor, model);
	}
}
