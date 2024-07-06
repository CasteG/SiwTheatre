package it.uniroma3.siw.coccolecapelli.controller;


import static it.uniroma3.siw.coccolecapelli.model.Dipendente.DIR_ADMIN_PAGES_DIP;
import static it.uniroma3.siw.coccolecapelli.model.Dipendente.DIR_FOLDER_IMG;
import static it.uniroma3.siw.coccolecapelli.model.Dipendente.DIR_PAGES_DIP;
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


import it.uniroma3.siw.coccolecapelli.controller.validator.DipendenteValidator;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.service.DipendenteService;
import it.uniroma3.siw.coccolecapelli.utility.FileStore;

@Controller
public class DipendenteController {
	
	@Autowired
	private DipendenteService dipendenteService;
	
	@Autowired
	private DipendenteValidator dipendenteValidator;
	
	
	/* METHODS GENERIC_USER */
	@GetMapping("/dipendente/{id}")
	public String getDipendente(@PathVariable("id") Long id, Model model) {
		Dipendente dipendente = dipendenteService.findById(id);
		model.addAttribute("dipendente", dipendente);
		return DIR_PAGES_DIP + "dipendente";
	}
	
	@GetMapping("/dipendenti")
	public String getDipendenti(Model model) {
		model.addAttribute("dipendenti", this.dipendenteService.findAll());
		
		return DIR_PAGES_DIP + "elencoDipendenti";
	}
	
	
	/* METHODS ADMIN */	
	@GetMapping("/admin/dipendente/{id}")
	public String getAdminDipendente(@PathVariable("id") Long id, Model model) {
		Dipendente dipendente = dipendenteService.findById(id);
		model.addAttribute("dipendente", dipendente);		
		return DIR_ADMIN_PAGES_DIP + "adminDipendente";
	}
	
	@GetMapping("/admin/dipendenti")
	public String getAdminDipendenti(Model model) {
		model.addAttribute("dipendenti", this.dipendenteService.findAll());		
		return DIR_ADMIN_PAGES_DIP + "adminElencoDipendenti";
	}
	
	
	
	
	// --- INSERIMENTO
	@GetMapping("/admin/dipendente/add")
	public String addDipendente(Model model) {
		model.addAttribute("dipendente", new Dipendente());
		return DIR_ADMIN_PAGES_DIP + "dipendenteForm";
	}
	
	@PostMapping("/admin/dipendente/add")
	public String addNewDipendente(@Valid @ModelAttribute("dipendente") Dipendente dipendente, 
									   BindingResult bindingResult, 
									   @RequestParam("file") MultipartFile file,
									   Model model) {
		
		this.dipendenteValidator.validate(dipendente, bindingResult);
		if(!bindingResult.hasErrors()) {
			dipendente.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.dipendenteService.save(dipendente);
			return this.getAdminDipendenti(model);
		}
		return DIR_ADMIN_PAGES_DIP + "dipendenteForm";
	}	
	
	
	// --- ELIMINAZIONE
	
	/* root permesso / entita / azione / parametri */
	@GetMapping("/admin/dipendente/delete/{id}")
	public String deleteDipendente(@PathVariable("id") Long id, Model model) {
		
		Dipendente dipendente = dipendenteService.findById(id);
		FileStore.removeImg(DIR_FOLDER_IMG, dipendente.getImg());
		
		//eliminazione immagini a cascata
	    dipendente.getServizi().stream().forEach(servizio -> servizio.eliminaImmagine());
	    
		this.dipendenteService.delete(dipendente);
		return this.getAdminDipendenti(model);
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/dipendente/edit/{id}")
	public String getEditDipendente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("dipendente",this.dipendenteService.findById(id));	
		return DIR_ADMIN_PAGES_DIP + "editDipendente";
	}
	
	@PostMapping("/admin/dipendente/edit/{id}")
	public String modificaDipendente(@Valid @ModelAttribute("dipendente") Dipendente dipendente, 
							   BindingResult bindingResult, 
							   @PathVariable("id") Long id, 
							   Model model) {
		
		Dipendente d = this.dipendenteService.findById(id);
		if(dipendente.getPartitaIVA().equals(d.getPartitaIVA())) {
			dipendente.setPartitaIVA("DefPartIVA");
			this.dipendenteValidator.validate(dipendente, bindingResult);
			dipendente.setPartitaIVA(d.getPartitaIVA());
		}else {
			this.dipendenteValidator.validate(dipendente, bindingResult);
		}
		dipendente.setId(id);
		if(!bindingResult.hasErrors()) {
			this.dipendenteService.update(dipendente, dipendente.getId());
			return this.getAdminDipendenti(model);
		}
		dipendente.setImg(d.getImg());
		return  DIR_ADMIN_PAGES_DIP + "editDipendente";
	}
	
	@PostMapping("/admin/dipendente/changeImg/{idDip}")
	public String changeImgParr(@PathVariable("idDip") Long idDip,
			   					@RequestParam("file") MultipartFile file, 
			   					Model model) {
		
		Dipendente d = this.dipendenteService.findById(idDip);
		if(!d.getImg().equals("profili")) {
			FileStore.removeImg(DIR_FOLDER_IMG, d.getImg());
		}
		d.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		this.dipendenteService.save(d);
		return this.getEditDipendente(idDip, model);
	}
}
