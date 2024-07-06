package it.uniroma3.siw.coccolecapelli.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.coccolecapelli.model.Servizio;
import it.uniroma3.siw.coccolecapelli.model.Disponibilita;
import it.uniroma3.siw.coccolecapelli.model.Prenotazione;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.model.User;

public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long>{

	public boolean existsByDipendenteAndServizioAndDisponibilitaAndCliente(Dipendente dipendente, Servizio servizio, Disponibilita disponibilita, User cliente);

}
