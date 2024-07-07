package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
public class Artist {
	
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
	
	@NotNull
	@Past
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate dateOfBirth;
	
	@NotBlank
	private String role;

	@OneToOne
	private Image image;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Play> plays;

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

	public List<Play> getPlays() {
		return this.plays;
	}

	public void setPlays(List<Play> plays) {
		this.plays = plays;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image img) {
		this.image = img;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return name +" "+ surname + " (" + role + ")";
	}
	
}
