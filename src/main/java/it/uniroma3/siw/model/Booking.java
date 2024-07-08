package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {
	
	public static final String DIR_PAGES_BOOK = "information/booking/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private User user;
	
	@ManyToOne
	private Play play;
	
	private int numTickets;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Play getPlay() {
		return this.play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getNumTickets() {
		return numTickets;
	}

	public void setNumTickets(int numTickets) {
		this.numTickets = numTickets;
	}

	@Override
	public String toString() {
		if(numTickets==1)
			return play.getName() + " (" + numTickets + " biglietto)";
		else
			return play.getName() + " (" + numTickets + " biglietti)";
	}
}
