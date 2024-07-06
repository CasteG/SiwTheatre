package it.uniroma3.siw.coccolecapelli.controller;

import static it.uniroma3.siw.coccolecapelli.model.Servizio.DIR_FOLDER_IMG;
import static it.uniroma3.siw.coccolecapelli.model.Servizio.DIR_ADMIN_PAGES_SERVIZIO;
import static it.uniroma3.siw.coccolecapelli.model.Servizio.DIR_PAGES_SERVIZIO;
import jakarta.validation.Valid;
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
import it.uniroma3.siw.coccolecapelli.utility.*;
import it.uniroma3.siw.coccolecapelli.controller.validator.ServizioValidator;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.model.Servizio;
import it.uniroma3.siw.coccolecapelli.service.DipendenteService;
import it.uniroma3.siw.coccolecapelli.service.ServizioService;

@Controller
public class ServizioController {
	
	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private ServizioValidator servizioValidator;
	
	@Autowired
	private DipendenteService dipendenteService;
	
	/* METHODS GENERIC_USER */
	
	@GetMapping("/servizio/{id}")
	public String getServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		model.addAttribute("servizio", servizio);
		
		return DIR_PAGES_SERVIZIO + "servizio";
	}
	
	@GetMapping("/servizi")
	public String getServizi(Model model) {
		model.addAttribute("servizi", this.servizioService.findAll());
		return DIR_PAGES_SERVIZIO + "elencoServizi";
	}
	
	/* METHODS ADMIN */
	
	@GetMapping("/admin/servizi/{id}")
	public String getServiziOfProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("servizi", this.dipendenteService.findById(id).getServizi());
		model.addAttribute("idDipendente", id);
		return DIR_ADMIN_PAGES_SERVIZIO + "adminElencoServizi";
	}
	
	// --- INSERIMENTO
	
	@GetMapping("/admin/servizio/add/{id}")
	public String selezionaServizio(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("servizio", new Servizio());
		
		return DIR_ADMIN_PAGES_SERVIZIO + "servizioForm";
	}
	
	@PostMapping("/admin/servizio/add/{id}")
	public String addServizio(@Valid @ModelAttribute("servizio") Servizio servizio, 
							  BindingResult bindingResult, 
							  @PathVariable("id") Long id,
							  @RequestParam("file") MultipartFile file,
							  Model model) {
		
		Dipendente dipendente = dipendenteService.findById(id);
		servizio.setDipendente(dipendente);
		this.servizioValidator.validate(servizio, bindingResult);
		servizio = servizioService.newServizio(servizio);
		if(!bindingResult.hasErrors()) {
			servizio.setImg(FileStore.store(file,DIR_FOLDER_IMG));
			this.dipendenteService.addServizio(dipendente, servizio);
			
			return "redirect:/admin/servizi/" + id;
		}
		
		model.addAttribute("id", id);
		return DIR_ADMIN_PAGES_SERVIZIO + "servizioForm";
	}
	
	// --- ELIMINAZIONE
	
	@GetMapping("/admin/servizio/delete/{id}")
	public String deleteServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		Dipendente dipendente = this.dipendenteService.findById(servizio.getDipendente().getId());
		dipendente.getServizi().remove(servizio);
		this.servizioService.delete(servizio);
		this.dipendenteService.save(dipendente);
		
		return "redirect:/admin/servizi/" + dipendente.getId();
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/servizio/edit/{id}")
	public String getEditServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		model.addAttribute("servizio", servizio);
		
		return DIR_ADMIN_PAGES_SERVIZIO + "editServizio";
	}
	
	@PostMapping("/admin/servizio/edit/{id}")
	public String editServizio(@Valid @ModelAttribute("servizio") Servizio servizio, 
							   BindingResult bindingResult, @PathVariable("id") Long id, 
							   Model model) {
		
		Servizio s = this.servizioService.findById(id);
		servizio.setDipendente(s.getDipendente());
		
		if(servizio.getNome().equals(s.getNome())) {
			servizio.setNome("nomeSerDef");
			this.servizioValidator.validate(servizio, bindingResult);
			servizio.setNome(s.getNome());
		}else {
			this.servizioValidator.validate(servizio, bindingResult);
		}
		
		servizio.setId(id);
		if(!bindingResult.hasErrors()) {
			this.servizioService.update(s, servizio);
			
			return "redirect:/admin/servizi/" + servizio.getDipendente().getId();
		}
		servizio.setImg(s.getImg());
		return DIR_ADMIN_PAGES_SERVIZIO + "editServizio";
	}
	
	@PostMapping("/admin/servizio/changeImg/{idS}")
	public String changeImgChef(@PathVariable("idS") Long idS,
			   					@RequestParam("file") MultipartFile file, 
			   					Model model) {
		
		Servizio s = this.servizioService.findById(idS);
		if(!s.getImg().equals("profili")) {
			FileStore.removeImg(DIR_FOLDER_IMG, s.getImg());
		}

		s.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		this.servizioService.save(s);
		return this.getEditServizio(idS, model);
	}
	
}
