package it.uniroma3.siw.coccolecapelli.model;

import it.uniroma3.siw.coccolecapelli.utility.FileStore;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Servizio {
	
	public static final String DIR_PAGES_SERVIZIO = "information/servizio/";
	public static final String DIR_ADMIN_PAGES_SERVIZIO = "admin/servizio/";
	public static final String DIR_FOLDER_IMG = "servizio/profili";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String descrizione;
	
	/*@NotBlank*/
	private Float prezzo;
	
	private String img;
	
	@ManyToOne
	private Dipendente dipendente;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}

	public Dipendente getDipendente() {
		return dipendente;
	}

	public void setDipendente(Dipendente dipendente) {
		this.dipendente = dipendente;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public void eliminaImmagine() {
		FileStore.removeImg(DIR_FOLDER_IMG, this.getImg());
	}
}
