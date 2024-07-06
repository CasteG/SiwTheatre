package it.uniroma3.siw.coccolecapelli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.coccolecapelli.service.ServizioService;
import it.uniroma3.siw.coccolecapelli.service.DipendenteService;

@Controller
public class GeneralController {

	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private DipendenteService dipendenteService;

	@GetMapping("/")
	public String getServiziAndDipendenti(Model model) {
		model.addAttribute("dipendenti", this.dipendenteService.findLastDipendenti());
		model.addAttribute("servizi", this.servizioService.findLastServizi());
		return "index";
	}
	
	@GetMapping("/admin")
	public String get() {
		return "redirect:/admin/dipendenti";
	}
}
