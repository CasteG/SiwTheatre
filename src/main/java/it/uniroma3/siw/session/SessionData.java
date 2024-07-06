package it.uniroma3.siw.session;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.oauth.CustomOAuth2User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UserRepository;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {

	private User user;
	
	private Credentials credentials;
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public Credentials getLoggedCredentials() {
		this.update();
		return this.credentials;
	}
	
	public User getLoggedUser() {
		try {
			this.update();
		}
		catch (ClassCastException e) {
			this.oauth2Update();
		}
		return this.user;
	}
	
	private void update() {
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUserDetails = (UserDetails) object;
		this.credentials = this.credentialsRepository.findByUsername(loggedUserDetails.getUsername()).get();
        this.user = this.credentials.getUser();
	}
	
	private void   oauth2Update() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomOAuth2User loggedOAuth2User = (CustomOAuth2User) object;
        try {
            this.user = userRepository.findByUsername(loggedOAuth2User.getLogin());
        }
        catch( NoSuchElementException e ){
            this.user = userRepository.findByUsername(loggedOAuth2User.getFullName());
        }
    }
}
