package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Availability;
import it.uniroma3.siw.service.AvailabilityService;

@Component
public class AvailabilityValidator implements Validator {

	@Autowired
	private AvailabilityService availabilityService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Availability.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(LocalDate.parse(((Availability)target).getDate()).isBefore(LocalDate.now()))
			errors.reject("date.availability");
		
		if(LocalTime.parse(((Availability)target).getEndTime()).isBefore(LocalTime.parse(((Availability)target).getStartTime())))
			errors.reject("hour.availability");
		
		if(this.availabilityService.alreadyExists((Availability) target))
			errors.reject("duplicate.availability");
	}
}
