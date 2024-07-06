package it.uniroma3.siw.coccolecapelli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.coccolecapelli.model.Prenotazione;
import it.uniroma3.siw.coccolecapelli.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {

	@Autowired
	private PrenotazioneRepository prenotazioneRepository;
	
	public boolean alreadyExists(Prenotazione target) {
		return this.prenotazioneRepository.existsByDipendenteAndServizioAndDisponibilitaAndCliente(target.getDipendente(), target.getServizio(), target.getDisponibilita(), target.getCliente());
	}
	
	public Prenotazione findById(Long id) {
		return this.prenotazioneRepository.findById(id).get();
	}

	public void delete(Prenotazione p) {
		this.prenotazioneRepository.delete(p);
	}
	

}
