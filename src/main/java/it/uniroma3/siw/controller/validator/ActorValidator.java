package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Actor;
import it.uniroma3.siw.service.ActorService;

@Component
public class ActorValidator implements Validator {

	@Autowired
	private ActorService actorService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Actor.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(actorService.alreadyExists((Actor) target))
			errors.reject("duplicate.actor");
	}
}
