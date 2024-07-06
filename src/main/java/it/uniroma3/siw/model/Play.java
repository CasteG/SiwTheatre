package it.uniroma3.siw.model;

import it.uniroma3.siw.utility.FileStore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Play {
	
	public static final String DIR_PAGES_PLAY = "information/play/";
	public static final String DIR_ADMIN_PAGES_PLAY = "admin/play/";
	public static final String DIR_FOLDER_IMG = "play/profiles";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	private String description;
	
	/*@NotBlank*/
	private Float price;
	
	private String img;
	
	@ManyToOne
	private Actor actor;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public void deleteImage() {
		FileStore.removeImg(DIR_FOLDER_IMG, this.getImg());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
}
