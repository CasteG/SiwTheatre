package it.uniroma3.siw.coccolecapelli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.coccolecapelli.model.Credentials;
import it.uniroma3.siw.coccolecapelli.model.User;
import it.uniroma3.siw.coccolecapelli.service.CredentialsService;
import it.uniroma3.siw.coccolecapelli.session.SessionData;

@ControllerAdvice
public class GlobalController {

	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private SessionData sessionData;

	@ModelAttribute("userDetails")
	public User getUser(){
		try {
			return this.sessionData.getLoggedUser();
		}
		catch(ClassCastException e){
			return null;
		}
	}

	@ModelAttribute("role")
	public String getUserRole(){
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			return credentials.getRole();
		}catch(ClassCastException e){
			return null;
		}
	}
}
