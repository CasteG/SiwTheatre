package it.uniroma3.siw.coccolecapelli.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.coccolecapelli.model.User;

public interface UtenteRepository extends CrudRepository<User, Long> {

	public Optional<User> findByUsername(String username);
	boolean existsByNomeAndCognome(String nome, String cognome);

}
