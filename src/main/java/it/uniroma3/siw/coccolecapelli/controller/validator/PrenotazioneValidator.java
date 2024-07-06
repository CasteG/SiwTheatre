package it.uniroma3.siw.coccolecapelli.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.coccolecapelli.model.Prenotazione;
import it.uniroma3.siw.coccolecapelli.service.PrenotazioneService;

@Component
public class PrenotazioneValidator implements Validator {

	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Prenotazione.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.prenotazioneService.alreadyExists((Prenotazione) target))
			errors.reject("duplicate.prenotazione");
	}
}
