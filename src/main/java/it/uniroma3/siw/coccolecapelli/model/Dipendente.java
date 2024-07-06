package it.uniroma3.siw.coccolecapelli.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Dipendente {
	
	public static final String DIR_PAGES_DIP = "information/dipendente/";
	public static final String DIR_ADMIN_PAGES_DIP = "admin/dipendente/";
	public static final String DIR_FOLDER_IMG = "dipendente/profili/";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;
	
	@NotBlank
	private String specializzazione;
	
	private String img;
	
	@NotBlank
	private String partitaIVA;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dipendente")
	private List<Servizio> servizi;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dipendente")
	private List<Disponibilita> disponibilita;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dipendente")
	private List<Prenotazione> prenotazioni; 

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
	
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getSpecializzazione() {
		return specializzazione;
	}

	public void setSpecializzazione(String specializzazione) {
		this.specializzazione = specializzazione;
	}

	public List<Disponibilita> getDisponibilità() {
		return disponibilita;
	}

	public void setDisponibilità(List<Disponibilita> disponibilità) {
		this.disponibilita = disponibilità;
	}
	
	public List<Disponibilita> getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(List<Disponibilita> disponibilita) {
		this.disponibilita = disponibilita;
	}

	public List<Prenotazione> getPrenotazione() {
		return prenotazioni;
	}

	public void setPrenotazione(List<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}
	
	public List<Servizio> getServizi() {
		return servizi;
	}

	public void setServizi(List<Servizio> servizi) {
		this.servizi = servizi;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public String getPartitaIVA() {
		return partitaIVA;
	}

	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
}
