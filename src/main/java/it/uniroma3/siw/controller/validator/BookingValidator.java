package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Booking;
import it.uniroma3.siw.service.BookingService;

@Component
public class BookingValidator implements Validator {

	@Autowired
	private BookingService bookingService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.bookingService.alreadyExists((Booking) target))
			errors.reject("duplicate.booking");
	}
}
