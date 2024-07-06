package it.uniroma3.siw.coccolecapelli.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.coccolecapelli.model.Dipendente;

public interface DipendenteRepository extends CrudRepository<Dipendente, Long> {

	boolean existsByPartitaIVA(String partitaIVA);

	@Query(value = "SELECT * FROM dipendente order by id limit :limit", nativeQuery = true)
	public List<Dipendente> findTopN(@Param("limit") int limit);
	

}