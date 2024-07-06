

package it.uniroma3.siw.coccolecapelli.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import it.uniroma3.siw.coccolecapelli.oauth.AuthenticationProvider;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User{

	public static final String DIR_FOLDER_IMG = "user/profilo";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	private String username;
	
	@NotBlank
	private String cognome;
	
	private LocalDate dataDiNascita;
	
	private String img;
	
	@OneToOne (mappedBy="utente")
	private Credentials credentials;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "auth_provider")
	private AuthenticationProvider oAuthProvider;
	
	@CreationTimestamp
	private LocalDateTime creationTimestamp;

	@UpdateTimestamp
	private LocalDateTime lastUpdateTimestamp;
	
	@OneToMany (cascade = CascadeType.ALL)
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
	
	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public List<Prenotazione> getPrenotazioni() {
		return prenotazioni;
	}

	public void setPrenotazioni(List<Prenotazione> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}

	public AuthenticationProvider getoAuthProvider() {
		return oAuthProvider;
	}

	public void setoAuthProvider(AuthenticationProvider oAuthProvider) {
		this.oAuthProvider = oAuthProvider;
	}

	public LocalDateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(LocalDateTime creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public LocalDateTime getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(LocalDateTime lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
