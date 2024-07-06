package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.Booking.DIR_PAGES_PREN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.controller.validator.BookingValidator;
import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.model.Availability;
import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AvailabilityService;
import it.uniroma3.siw.service.BookingService;
import it.uniroma3.siw.service.PlayService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private PlayService playService;
	
	@Autowired
	private AvailabilityService availabilityService;
	
	@Autowired
	private BookingValidator bookingValidator;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile/bookings/{id}")
	public String getBookings(@PathVariable("id") Long id, Model model) {
		User user = this.userService.getUser(id);
		model.addAttribute("bookings", user.getBookings());
		return DIR_PAGES_PREN + "bookingsList";
	}
	
	@GetMapping("/profile/booking/add/{id}")
    public String addBooking(@PathVariable("id") Long id, Model model) {
        model.addAttribute("plays", this.playService.findAll());
        model.addAttribute("idUser", id);
        return DIR_PAGES_PREN + "BookingPlayslist";
    }
	
	@GetMapping("/profile/booking/availability/{idU}/{idP}")
	public String selectAvailability(@PathVariable("idU") Long idUser, 
									  @PathVariable("idP") Long idPlay, 
									  Model model) {
		model.addAttribute("idUser", idUser);
		model.addAttribute("idPlay", idPlay);
		model.addAttribute("booking", new Booking());
		
		Actor p = this.playService.findById(idPlay).getActor();
		
		model.addAttribute("availabilityList", this.availabilityService.findByActorAndActive(p));
		
		
		return DIR_PAGES_PREN + "bookingAvailabilityList";
	}
	
	@GetMapping("/profile/booking/add/{idU}/{idP}/{idA}")
	public String addBooking(@Valid @ModelAttribute("booking") Booking b,
								  BindingResult bindingResult,
								  @PathVariable("idU") Long idUser, 
								  @PathVariable("idP") Long idPlay,
								  @PathVariable("idA") Long idAvailability,
								  Model model) {
		
		User u = this.userService.getUser(idUser);
		Play p = this.playService.findById(idPlay);
		Availability a = this.availabilityService.findById(idAvailability);
		Actor actor = p.getActor();
		b.setActor(actor);
		b.setUser(u);
		b.setAvailability(a);
		b.setPlay(p); 
		a.setActive(false);
		
		this.bookingValidator.validate(b, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.userService.addBooking(u, b);			
			return "redirect:/profile/bookings/" + u.getId();
		}
		//da modellare in caso di errori
		return DIR_PAGES_PREN + "bookingOverview";
	}
	
	@GetMapping("/profile/delete/{id}")
	public String deleteBooking(@PathVariable("id") Long id, Model model) {
		Booking b = this.bookingService.findById(id);
		User u = b.getUser();
		Availability a = b.getAvailability();
		
		a.setActive(true);
		this.userService.deleteBooking(u, b);
		this.bookingService.delete(b);
		return "redirect:/profile/bookings/" + u.getId();
	}
}
