package it.uniroma3.siw.controller;

import java.util.Optional;

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

import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Play;
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
	

	@PostMapping("/bookings")
	public String newBooking(@Valid @ModelAttribute("booking") Booking booking, 
	        BindingResult bindingResult, Model model) {

	    this.bookingValidator.validate(booking, bindingResult);

	    /* controllo chi è che sta aggiungendo una prenotazione: default o admin */
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    // Ottieni il Principal e fai il cast a UserDetails
	    Object principal = authentication.getPrincipal();
	    UserDetails userDetails = (UserDetails) principal;
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

	    /* AGGIUNTA PRENOTAZIONE DA PARTE DI UN ADMIN */
	    if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {

	        if (bindingResult.hasErrors()) {
	            model.addAttribute("booking", booking);
	            model.addAttribute("plays", this.playService.findAll());
	            model.addAttribute("users", this.userService.findAll());
	            return "admin/formNewBooking.html";
	        } else {
	            Play play = booking.getPlay();
	            int numTickets = booking.getNumTickets();
	            
	            if (play.getAvailableTickets() < numTickets) {
	                bindingResult.rejectValue("numTickets", "error.booking", "Non ci sono abbastanza biglietti disponibili.");
	                model.addAttribute("booking", booking);
	                model.addAttribute("plays", this.playService.findAll());
	                model.addAttribute("users", this.userService.findAll());
	                return "admin/formNewBooking.html";
	            }

	            play.setAvailableTickets(play.getAvailableTickets() - numTickets);
	            this.playService.save(play);  // Salva le modifiche all'oggetto Play
	            this.bookingService.save(booking);
	            model.addAttribute("booking", booking);
	            return "redirect:admin/bookings/" + booking.getId();  // dico al client fammi una richiesta all'url booking/{id} 
	        }
	    }
	    /* AGGIUNTA PRENOTAZIONE DA PARTE DI UN UTENTE */
	    else {

	        if (bindingResult.hasErrors()) {
	            model.addAttribute("booking", booking);
	            model.addAttribute("plays", this.playService.findAll());
	            return "user/formNewBooking.html";
	        } else {
	            /* cerco lo user per poterlo settare come cliente della prenotazione */
	            User user = this.userService.findById(credentials.getUser().getId());
	            booking.setUser(user);

	            Play play = booking.getPlay();
	            int numTickets = booking.getNumTickets();

	            if (play.getAvailableTickets() < numTickets) {
	                bindingResult.rejectValue("numTickets", "error.booking", "Non ci sono abbastanza biglietti disponibili.");
	                model.addAttribute("booking", booking);
	                model.addAttribute("plays", this.playService.findAll());
	                return "user/formNewBooking.html";
	            }

	            play.setAvailableTickets(play.getAvailableTickets() - numTickets);
	            this.playService.save(play);  // Salva le modifiche all'oggetto Play
	            this.bookingService.save(booking);
	            model.addAttribute("booking", booking);
	            return "redirect:bookings/" + booking.getId();  // dico al client fammi una richiesta all'url booking/{id} 
	        }
	    }
	}

	
	
	/*********************************************************************************************/
	/**************************************** ADMIN ***********************************************/
	/**********^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^**********************************************/
	

	@GetMapping("admin/bookings/{id}")
	public String getBooking(@PathVariable("id") Long id, Model model) {
		Booking booking = this.bookingService.findById(id);
		/* calcolo il prezzo totale della prenotazione */
		float totalPrice = booking.getNumTickets() * booking.getPlay().getPrice();
		
		model.addAttribute("booking", booking);
		model.addAttribute("totalPrice", totalPrice);
		return "admin/booking.html";
	}
	
	@GetMapping("admin/bookings")
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
		/* cerco l'utente nel database e se non esiste restituisco un messaggio di errore */
	    Optional<Credentials> credentialsOptional = credentialsService.findByUsername(username);
    	//l'utente non esiste	
	    if (credentialsOptional.isEmpty()) {
	        model.addAttribute("errorMessage", "L'username inserito non esiste.");
	        return "admin/formSearchBooking.html";
	    }
	    //l'utente esiste
	    User user = credentialsOptional.get().getUser();
	    Iterable<Booking> userBookings = this.bookingService.findByUser(user);
	    model.addAttribute("bookings", userBookings);
	    return "admin/bookings.html";
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
	
	@GetMapping("/admin/formUpdateBookingTickets/{id}")
	public String formUpdateBooking(@PathVariable("id") Long id, Model model) {
		model.addAttribute("booking", this.bookingService.findById(id));
		return "admin/formUpdateBookingTickets.html";
	}
	
	@PostMapping("/admin/updateBookingTickets/{id}")
	public String updateBookingTickets(@PathVariable("id") Long id, @RequestParam("numTickets") int numTickets, Model model) {
	    // Trova la prenotazione esistente
	    Booking booking = this.bookingService.findById(id);
	    
	    if (booking != null) {
	        int oldNumTickets = booking.getNumTickets();
	        int difference = numTickets - oldNumTickets;
	        Play play = booking.getPlay();

	        // Controlla se sono rimasti abbastanza biglietti
	        if (play.getAvailableTickets() - difference < 0) {
	            model.addAttribute("errorMessage", "Non sono rimasti biglietti sufficienti");
	            model.addAttribute("booking", booking);
	            return "admin/formUpdateBookingTickets.html";
	        } else {
	            // Aggiorna il numero di biglietti disponibili per lo spettacolo
	            play.setAvailableTickets(play.getAvailableTickets() - difference);

	            // Aggiorna il numero di biglietti nella prenotazione
	            booking.setNumTickets(numTickets);

	            // Salva le modifiche
	            this.playService.save(play);
	            this.bookingService.save(booking);

	            // Calcola il nuovo prezzo totale
	            float totalPrice = booking.getNumTickets() * play.getPrice();
	            model.addAttribute("booking", booking);
	            model.addAttribute("totalPrice", totalPrice);

	            // Reindirizza alla pagina di dettaglio della prenotazione aggiornata
	            return "redirect:/admin/bookings/" + booking.getId();
	        }
	    } else {
	        // Gestisci il caso in cui la prenotazione non viene trovata 
	        model.addAttribute("errorMessage", "Prenotazione non trovata.");
	        return "admin/formUpdateBookingTickets.html";
	    }
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
		Booking booking = this.bookingService.findById(id);
		/* prima di eliminare la prenotazione riaggiungo allo spettacolo il numero di biglietti prenotati */
		int bookingTickets = booking.getNumTickets();
		booking.getPlay().setAvailableTickets(booking.getPlay().getAvailableTickets() + bookingTickets);
		
		this.playService.save(booking.getPlay());
		this.bookingService.remove(booking);
		return "admin/successfulRemoval.html";
	}
	
	
	/*********************************************************************************************/
	/**************************************** USER ***********************************************/
	/**********^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^**********************************************/

	
	@GetMapping("bookings")
	public String showBookingsUser(Model model) {

		/* prendo l'utente che sta facendo la richiesta per mostrargli solo le sue prenotazioni */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Ottieni il Principal e fai il cast a UserDetails
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		model.addAttribute("bookings", this.bookingService.findByUser(credentials.getUser()));
		return "bookings.html";
	}

	
	@GetMapping("bookings/{id}")
	public String getBookingUser(@PathVariable("id") Long id, Model model) {
		Booking booking = this.bookingService.findById(id);
		/* calcolo il prezzo totale della prenotazione */
		float totalPrice = booking.getNumTickets() * booking.getPlay().getPrice();
		
		model.addAttribute("booking", booking);
		model.addAttribute("totalPrice", totalPrice);
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
	
	@GetMapping("/user/updateBookingTickets/{id}")
	public String formUpdateBookingUser(@PathVariable("id") Long id, Model model) {
		model.addAttribute("booking", this.bookingService.findById(id));
		return "user/formUpdateBookingTickets.html";
	}
	
	@PostMapping("/user/updateBookingTickets/{id}")
	public String updateBookingTicketsUser(@PathVariable("id") Long id, @RequestParam("numTickets") int numTickets, Model model) {
	    // Trova la prenotazione esistente
	    Booking booking = this.bookingService.findById(id);
	    
	    if (booking != null) {
	        int oldNumTickets = booking.getNumTickets();
	        int difference = numTickets - oldNumTickets;
	        Play play = booking.getPlay();

	        // Controlla se sono rimasti abbastanza biglietti
	        if (play.getAvailableTickets() - difference < 0) {
	            model.addAttribute("errorMessage", "Non sono rimasti biglietti sufficienti");
	            model.addAttribute("booking", booking);
	            return "user/formUpdateBookingTickets.html";
	        } else {
	            // Aggiorna il numero di biglietti disponibili per lo spettacolo
	            play.setAvailableTickets(play.getAvailableTickets() - difference);

	            // Aggiorna il numero di biglietti nella prenotazione
	            booking.setNumTickets(numTickets);

	            // Salva le modifiche
	            this.playService.save(play);
	            this.bookingService.save(booking);

	            // Calcola il nuovo prezzo totale
	            float totalPrice = booking.getNumTickets() * play.getPrice();
	            model.addAttribute("booking", booking);
	            model.addAttribute("totalPrice", totalPrice);

	            // Reindirizza alla pagina di dettaglio della prenotazione aggiornata
	            return "redirect:/bookings/" + booking.getId();
	        }
	    } else {
	        // Gestisci il caso in cui la prenotazione non viene trovata 
	        model.addAttribute("errorMessage", "Prenotazione non trovata.");
	        return "user/formUpdateBookingTickets.html";
	    }
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
		Booking booking = this.bookingService.findById(id);
		/* prima di eliminare la prenotazione riaggiungo allo spettacolo il numero di biglietti prenotati */
		int bookingTickets = booking.getNumTickets();
		booking.getPlay().setAvailableTickets(booking.getPlay().getAvailableTickets() + bookingTickets);
		
		this.playService.save(booking.getPlay());
		this.bookingService.remove(booking);
		return "user/successfulRemoval.html";
	}
	

}
