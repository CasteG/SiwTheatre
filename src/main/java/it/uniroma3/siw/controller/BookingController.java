package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookingService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.PlayService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validator.BookingValidator;
import jakarta.validation.Valid;

@Controller
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private PlayService playService;
	
	@Autowired
	private BookingValidator bookingValidator;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CredentialsService credentialsService;
	

	@PostMapping("/booking")
	public String newBooking(@Valid @ModelAttribute("booking") Booking booking, 
			BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile image) throws IOException {
		
		this.bookingValidator.validate(booking, bindingResult);
		
		/* controllo chi è che sta aggiungendo una prenotazione: default o admin */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Ottieni il Principal e fai il cast a UserDetails
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
         
	     /* AGGIUNTA PRENOTAZIONE DA PARTE DI UN ADMIN */
	     if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	    	 
			if(bindingResult.hasErrors()) {
				model.addAttribute("booking", booking);
				model.addAttribute("plays", this.playService.findAll());
				model.addAttribute("users", this.userService.findAll());
				return "admin/formNewBooking.html";
			}
			
			else {
				this.bookingService.save(booking); 
				model.addAttribute("booking", booking);
				return "redirect:booking/"+booking.getId();	//dico al client fammi una richiesta all'url booking/{id} 
			}
		}
         /* AGGIUNTA PRENOTAZIONE DA PARTE DI UN UTENTE */
         else {
        	 
        	if(bindingResult.hasErrors()) {
     			model.addAttribute("booking", booking);
     			model.addAttribute("plays", this.playService.findAll());
     			return "user/formNewBooking.html";
     		}
        	 
        	else {
		         /* cerco lo user per poterlo settare come cliente della prenotazione */
		         User user = this.userService.findById(credentials.getUser().getId());
				 booking.setUser(user);
		     	
				this.bookingService.save(booking); 
				model.addAttribute("booking", booking);
				return "redirect:booking/"+booking.getId();	//dico al client fammi una richiesta all'url booking/{id} 
     		}
         }
	}
	
	
	/*********************************************************************************************/
	/**************************************** ADMIN ***********************************************/
	/**********^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^**********************************************/
	

	@GetMapping("admin/booking/{id}")
	public String getBooking(@PathVariable("id") Long id, Model model) {
		Booking booking = this.bookingService.findById(id);
		model.addAttribute("booking", booking);
		return "admin/booking.html";
	}
	
	@GetMapping("admin/booking")
	public String showBookings(Model model) {
		model.addAttribute("bookings", this.bookingService.findAll());
		return "admin/bookings.html";
	}

	@GetMapping("admin/formSearchBooking")
	public String formSearchBookings() {
		return "admin/formSearchBooking.html";
	}

	@PostMapping("admin/searchBooking")
	public String searchBookings(Model model, @RequestParam String username) {
		/* cerco lo User in base allo username e poi prendo solo le ricette dello User in questione */
		model.addAttribute("bookings", this.bookingService.findByUser(credentialsService.findByUsername(username)));
		return "bookings.html";
	}
	
	@GetMapping("/admin/formNewBooking")
	public String formNewBooking(Model model) {
		model.addAttribute("booking", new Booking());
		model.addAttribute("plays", this.playService.findAll());
		model.addAttribute("users", this.userService.findAll());
		return "admin/formNewBooking.html";
	}
	
	@GetMapping("/admin/manageBookings")
	public String manageBookings(Model model) {
		model.addAttribute("bookings", this.bookingService.findAll());
		return "admin/manageBookings.html";
	}
	
	@GetMapping("/admin/formUpdateBooking/{id}")
	public String formUpdateBooking(@PathVariable("id") Long id, Model model) {
		model.addAttribute("booking", this.bookingService.findById(id));
		model.addAttribute("plays", this.playService.findAll());
		return "admin/formUpdateBooking.html";
	}
	
	@GetMapping("/admin/updatePlay/{idBooking}")
	public String updatePlay(Model model, @PathVariable("idBooking") Long id) {
		Booking booking = this.bookingService.findById(id);
		model.addAttribute("booking", booking);
		model.addAttribute("plays", this.playService.findAll());
		return "admin/updatePlayBooking.html";
	}
	
	@GetMapping("/admin/removeBooking/{id}")
	public String removeBooking(@PathVariable("id") Long id, Model model) {
		this.bookingService.remove(this.bookingService.findById(id));
		return "admin/successfulRemoval.html";
	}
	
	
	/*********************************************************************************************/
	/**************************************** USER ***********************************************/
	/**********^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^**********************************************/

	
	@GetMapping("user/booking")
	public String showBookingsUser(Model model) {

		/* prendo l'utente che sta facendo la richiesta per mostrargli solo le sue prenotazioni */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Ottieni il Principal e fai il cast a UserDetails
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		model.addAttribute("bookings", this.bookingService.findByUser(credentials.getUser()));
		return "user/bookings.html";
	}

	
	@GetMapping("user/booking/{id}")
	public String getBookingUser(@PathVariable("id") Long id, Model model) {
		Booking booking = this.bookingService.findById(id);
		model.addAttribute("booking", booking);
		return "user/booking.html";
	}
	
	@GetMapping("/user/formNewBooking")
	public String formNewBookingUser(Model model) {
		model.addAttribute("booking", new Booking());
		model.addAttribute("plays", this.playService.findAll());
		return "user/formNewBooking.html";
	}
	
	@GetMapping("/user/manageBookings")
	public String manageBookingsUser(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Ottieni il Principal e fai il cast a UserDetails
        Object principal = authentication.getPrincipal();
        // se può accedere a questa risorsa vuol dire che è per forza utente
        UserDetails userDetails = (UserDetails) principal;
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	//è user default, puà modificare solo le sue ricette
    	Iterable<Booking> userBookings= this.bookingService.findByUser(credentials.getUser());
    	model.addAttribute("bookings", userBookings);
    	return "user/manageBookings.html";
	}
	
	@GetMapping("/user/formUpdateBooking/{id}")
	public String formUpdateBookingUser(@PathVariable("id") Long id, Model model) {
		model.addAttribute("booking", this.bookingService.findById(id));
		model.addAttribute("plays", this.playService.findAll());
		return "user/formUpdateBooking.html";
	}
	
	@GetMapping("/user/updatePlay/{idBooking}")
	public String updatePlayUser(Model model, @PathVariable("idBooking") Long id) {
		Booking booking = this.bookingService.findById(id);
		model.addAttribute("booking", booking);
		model.addAttribute("plays", this.playService.findAll());
		return "user/updatePlayBooking.html";
	}
	
	@GetMapping("/user/removeBooking/{id}")
	public String removeBookingUser(@PathVariable("id") Long id, Model model) {
		this.bookingService.remove(this.bookingService.findById(id));
		return "user/successfulRemoval.html";
	}
	

}
