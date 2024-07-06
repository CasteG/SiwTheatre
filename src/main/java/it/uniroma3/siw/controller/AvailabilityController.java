package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.Availability.DIR_ADMIN_PAGES_DISP;
import static it.uniroma3.siw.model.Availability.DIR_PAGES_DISP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.AvailabilityValidator;
import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;
import it.uniroma3.siw.service.ActorService;
import it.uniroma3.siw.service.AvailabilityService;
import jakarta.validation.Valid;

@Controller
public class AvailabilityController {
	
	@Autowired
	private AvailabilityService availabilityService;
	
	@Autowired
	private AvailabilityValidator availabilityValidator;
	
	@Autowired
	private ActorService actorService;
	
	
	/* METHODS GENERIC_USER */	
	@GetMapping("/availability/{id}")
	public String getActorAvailability(@PathVariable("id") Long id, Model model) {
		model.addAttribute("availabilityList", this.availabilityService.findByActorAndActive(this.actorService.findById(id)));
		return DIR_PAGES_DISP + "availabilityList";
	}
	
	/* METHODS ADMIN */
	@GetMapping("/admin/availability/{id}")
	public String getAdminActorAvailability(@PathVariable("id") Long id, Model model) {
		model.addAttribute("availabilityList", this.availabilityService.findByActorAndActive(this.actorService.findById(id)));
		model.addAttribute("idActor", id);
		return DIR_ADMIN_PAGES_DISP + "adminAvailabilityList";
	}
	
	// --- INSERIMENTO
	@GetMapping("/admin/availability/add/{id}")
	public String addGetAvailability(@PathVariable("id") Long id, Model model) {
		model.addAttribute("idActor", id);
		model.addAttribute("availability", new Availability());
		return DIR_ADMIN_PAGES_DISP + "availabilityForm";
	}
	
	@PostMapping("/admin/availability/add/{id}")
	public String addAvailability(@Valid @ModelAttribute("availability") Availability availability, BindingResult bindingResult, 
									@PathVariable("id") Long id, Model model) {
		
		Actor actor = this.actorService.findById(id);
		availability.setActor(actor);
		availability.setActive(true);
		this.availabilityValidator.validate(availability, bindingResult);
		availability = availabilityService.newAvailability(availability);
		
		if(!bindingResult.hasErrors()) {
			this.actorService.addAvailability(actor, availability);
			return this.getAdminActorAvailability(id, model);
		}
		
		model.addAttribute("id", id);
		return DIR_ADMIN_PAGES_DISP + "availabilityForm";
	}
	
	// --- ELIMINAZIONE
	
	@GetMapping("/admin/availability/delete/{id}")
	public String deleteAvailability(@PathVariable("id") Long id, Model model) {
		Availability availability = this.availabilityService.findById(id);		
		Actor a = this.actorService.findById(availability.getActor().getId());
		
		a.getAvailability().remove(availability);
		this.availabilityService.delete(availability);
		this.actorService.save(a);	
		return "redirect:/admin/availability/" + a.getId();
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/availability/edit/{id}")
	public String editAvailability(@PathVariable("id") Long id, Model model) {
		model.addAttribute("availability", this.availabilityService.findById(id));
		
		return DIR_ADMIN_PAGES_DISP + "editAvailability";
	}
	
	@PostMapping("/admin/availability/edit/{id}")
	public String editAvailability(@Valid @ModelAttribute("availability") Availability availability, 
								   BindingResult bindingResult, 
								   @PathVariable("id") Long id, 
								   Model model) {
		
		Availability a = this.availabilityService.findById(id);
		availability.setActor(a.getActor());
		this.availabilityValidator.validate(availability, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.availabilityService.update(a, availability);
			return this.getAdminActorAvailability(a.getActor().getId(), model);
		}
		availability.setId(id);
		return DIR_ADMIN_PAGES_DISP + "editAvailability";
	}
	
}
