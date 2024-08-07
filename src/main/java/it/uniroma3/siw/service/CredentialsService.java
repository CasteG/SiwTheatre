package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials save(Credentials credentials) {
		return credentialsRepository.save(credentials);
	}
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		if (credentials.getRole() == null) {
			credentials.setRole(Credentials.DEFAULT_ROLE);
		}
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return credentialsRepository.save(credentials);
	}

	public Optional<Credentials> findByUsername(String username) {
        return credentialsRepository.findByUsername(username); 
    }
}
