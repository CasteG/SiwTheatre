package it.uniroma3.siw.coccolecapelli.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.coccolecapelli.model.Servizio;
import it.uniroma3.siw.coccolecapelli.service.ServizioService;

@Component
public class ServizioValidator implements Validator {

	@Autowired
	private ServizioService servizioService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Servizio.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.servizioService.alreadyExists((Servizio) target))
			errors.reject("duplicate.servizio");
	}
	
}
