package it.uniroma3.siw.coccolecapelli.controller;

import static it.uniroma3.siw.coccolecapelli.model.Prenotazione.DIR_PAGES_PREN;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.coccolecapelli.controller.validator.PrenotazioneValidator;
import it.uniroma3.siw.coccolecapelli.model.Disponibilita;
import it.uniroma3.siw.coccolecapelli.model.Prenotazione;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.model.Servizio;
import it.uniroma3.siw.coccolecapelli.model.User;
import it.uniroma3.siw.coccolecapelli.service.DisponibilitaService;
import it.uniroma3.siw.coccolecapelli.service.PrenotazioneService;
import it.uniroma3.siw.coccolecapelli.service.ServizioService;
import it.uniroma3.siw.coccolecapelli.service.UtenteService;

@Controller
public class PrenotazioneController {
	
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private DisponibilitaService disponibilitaService;
	
	@Autowired
	private PrenotazioneValidator prenotazioneValidator;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping("/profile/prenotazioni/{id}")
	public String getPrenotazioni(@PathVariable("id") Long id, Model model) {
		User utente = this.utenteService.getUser(id);
		model.addAttribute("prenotazioni", utente.getPrenotazioni());
		return DIR_PAGES_PREN + "elencoPrenotazioni";
	}
	
	@GetMapping("/profile/prenotazione/add/{id}")
    public String addPrenotazione(@PathVariable("id") Long id, Model model) {
        model.addAttribute("servizi", this.servizioService.findAll());
        model.addAttribute("idUtente", id);
        return DIR_PAGES_PREN + "elencoServiziPrenotazione";
    }
	
	@GetMapping("/profile/prenotazione/disponibilita/{idU}/{idS}")
	public String selectDisponibilita(@PathVariable("idU") Long idUtente, 
									  @PathVariable("idS") Long idServizio, 
									  Model model) {
		model.addAttribute("idUtente", idUtente);
		model.addAttribute("idServizio", idServizio);
		model.addAttribute("prenotazione", new Prenotazione());
		
		Dipendente p = this.servizioService.findById(idServizio).getDipendente();
		
		model.addAttribute("disponibilitaList", this.disponibilitaService.findByDipendenteAndActive(p));
		
		
		return DIR_PAGES_PREN + "elencoDisponibilitaPrenotazione";
	}
	
	@GetMapping("/profile/prenotazione/add/{idU}/{idS}/{idD}")
	public String addPrenotazione(@Valid @ModelAttribute("prenotazione") Prenotazione p,
								  BindingResult bindingResult,
								  @PathVariable("idU") Long idUtente, 
								  @PathVariable("idS") Long idServizio,
								  @PathVariable("idD") Long idDisponibilita,
								  Model model) {
		
		User u = this.utenteService.getUser(idUtente);
		Servizio s = this.servizioService.findById(idServizio);
		Disponibilita d = this.disponibilitaService.findById(idDisponibilita);
		Dipendente dip = s.getDipendente();
		p.setDipendente(dip);
		p.setCliente(u);
		p.setDisponibilita(d);
		p.setServizio(s); 
		d.setActive(false);
		
		this.prenotazioneValidator.validate(p, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.utenteService.addPrenotazione(u, p);			
			return "redirect:/profile/prenotazioni/" + u.getId();
		}
		//da modellare in caso di errori
		return DIR_PAGES_PREN + "riepilogoPrenotazione";
	}
	
	@GetMapping("/profile/delete/{id}")
	public String deletePrenotazione(@PathVariable("id") Long id, Model model) {
		Prenotazione p = this.prenotazioneService.findById(id);
		User u = p.getCliente();
		Disponibilita d = p.getDisponibilita();
		//Parrucchiere parr = p.getParrucchiere();
		
		d.setActive(true);
		this.utenteService.deletePrenotazione(u, p);
		this.prenotazioneService.delete(p);
		return "redirect:/profile/prenotazioni/" + u.getId();
	}
}
