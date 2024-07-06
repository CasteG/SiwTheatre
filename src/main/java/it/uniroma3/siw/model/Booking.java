package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	
	public static final String DIR_PAGES_PREN = "information/booking/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Actor actor;
	
	@OneToOne
	private Availability availability;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Play play;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Actor getActor() {
		return this.actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	
	public Play getPlay() {
		return this.play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}


	public Availability getAvailability() {
		return this.availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
