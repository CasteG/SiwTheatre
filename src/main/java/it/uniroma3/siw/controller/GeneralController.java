package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.service.ActorService;
import it.uniroma3.siw.service.PlayService;

@Controller
public class GeneralController {

	@Autowired
	private PlayService playService;
	
	@Autowired
	private ActorService actorService;

	@GetMapping("/")
	public String getPlaysAndActors(Model model) {
		model.addAttribute("actors", this.actorService.findLastActors());
		model.addAttribute("plays", this.playService.findLastPlays());
		return "index";
	}
	
	@GetMapping("/admin")
	public String get() {
		return "redirect:/admin/actors";
	}
}
