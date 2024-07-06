package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Actor {
	
	public static final String DIR_PAGES_ACTOR = "information/actor/";
	public static final String DIR_ADMIN_PAGES_ACTOR = "admin/actor/";
	public static final String DIR_FOLDER_IMG = "actor/profiles/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	private LocalDate dateOfBirth;
	
	@NotBlank
	private String role;
	
	private String img;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "actor")
	private List<Play> plays;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "actor")
	private List<Availability> availability;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "actor")
	private List<Booking> bookings; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Availability> getAvailability() {
		return this.availability;
	}

	public void setAvailability(List<Availability> availability) {
		this.availability = availability;
	}

	public List<Booking> getBookings() {
		return this.bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public List<Play> getPlays() {
		return this.plays;
	}

	public void setPlays(List<Play> plays) {
		this.plays = plays;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
}
