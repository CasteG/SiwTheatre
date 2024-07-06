package it.uniroma3.siw.coccolecapelli.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.coccolecapelli.model.Disponibilita;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;

public interface DisponibilitaRepository extends CrudRepository<Disponibilita, Long> {

	public boolean existsByDataAndOraInizioAndOraFineAndDipendente(String data, String oraInizio, String oraFine, Dipendente dipendente);

	public List<Disponibilita> findByDipendente(Dipendente dipendente);
	
	public List<Disponibilita> findByDipendenteAndActiveTrueOrderByDataAscOraInizio(Dipendente dipendente);

}
