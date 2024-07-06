package it.uniroma3.siw.coccolecapelli.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.service.DipendenteService;

@Component
public class DipendenteValidator implements Validator {

	@Autowired
	private DipendenteService dipendenteService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Dipendente.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(dipendenteService.alreadyExists((Dipendente) target))
			errors.reject("duplicate.dipendente");
	}
}
