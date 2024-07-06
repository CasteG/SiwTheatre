package it.uniroma3.siw.coccolecapelli.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.model.Servizio;

public interface ServizioRepository extends CrudRepository<Servizio, Long> {
	
	public boolean existsByNomeAndDipendente(String nome, Dipendente dipendente);
	
	public List<Servizio> findByDipendente(Dipendente dipendente);
	
	@Query(value = "SELECT * FROM servizio order by id limit :limit", nativeQuery = true)
	public List<Servizio> findTopN(@Param("limit") int limit);
}
