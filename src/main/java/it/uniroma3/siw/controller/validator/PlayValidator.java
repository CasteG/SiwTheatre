package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Play;
import it.uniroma3.siw.service.PlayService;

@Component
public class PlayValidator implements Validator {

	@Autowired
	private PlayService playService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Play.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.playService.alreadyExists((Play) target))
			errors.reject("duplicate.play");
	}
	
}
