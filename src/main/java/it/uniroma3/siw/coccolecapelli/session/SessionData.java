package it.uniroma3.siw.coccolecapelli.session;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.coccolecapelli.model.Credentials;
import it.uniroma3.siw.coccolecapelli.model.User;
import it.uniroma3.siw.coccolecapelli.oauth.CustomOAuth2User;
import it.uniroma3.siw.coccolecapelli.repository.CredentialsRepository;
import it.uniroma3.siw.coccolecapelli.repository.UtenteRepository;

@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {

	private User user;
	
	private Credentials credentials;
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	
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
        this.user = this.credentials.getUtente();
	}
	
	private void   oauth2Update() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomOAuth2User loggedOAuth2User = (CustomOAuth2User) object;
        try {
            this.user = utenteRepository.findByUsername(loggedOAuth2User.getLogin()).get();
        }
        catch( NoSuchElementException e ){
            this.user = utenteRepository.findByUsername(loggedOAuth2User.getFullName()).get();
        }
    }
}
