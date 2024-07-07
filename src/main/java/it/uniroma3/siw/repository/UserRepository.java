package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findByUsername(String username);

	boolean existsByNameAndSurname(String name, String surname);


}
