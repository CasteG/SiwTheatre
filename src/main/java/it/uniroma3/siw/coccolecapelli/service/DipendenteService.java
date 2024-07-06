package it.uniroma3.siw.coccolecapelli.service;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.coccolecapelli.model.Servizio;
import it.uniroma3.siw.coccolecapelli.model.Disponibilita;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.repository.DipendenteRepository;

@Service
public class DipendenteService {

	@Autowired
	private DipendenteRepository dipendenteRepository;
	
	public boolean alreadyExists(Dipendente target) {
		return dipendenteRepository.existsByPartitaIVA(target.getPartitaIVA());
	}

	public Dipendente findById(Long id) {
		return dipendenteRepository.findById(id).get();
	}

	@Transactional
	public void save(Dipendente dipendente) {
		dipendenteRepository.save(dipendente);
	}
	
	public List<Dipendente> findAll() {
		return (List<Dipendente>) dipendenteRepository.findAll();
	}
	
	@Transactional
	public void delete(Dipendente dipendente) {
		this.dipendenteRepository.delete(dipendente);
	}
	
	@Transactional
	public void update(Dipendente dipendente, Long id) {
		Dipendente p = this.dipendenteRepository.findById(id).get();
		p.setNome(dipendente.getNome());
		p.setCognome(dipendente.getCognome());
		p.setSpecializzazione(dipendente.getSpecializzazione());
		p.setPartitaIVA(dipendente.getPartitaIVA());
		this.dipendenteRepository.save(p);
	}
	
	@Transactional
	public void addServizio(Dipendente dipendente, Servizio servizio) {
		dipendente.getServizi().add(servizio);
		this.dipendenteRepository.save(dipendente);
	}
	
	@Transactional
	public void addDisponibilita(Dipendente dipendente, Disponibilita disponibilita) {
		dipendente.getDisponibilita().add(disponibilita);
		this.dipendenteRepository.save(dipendente);
	}

	public List<Dipendente> findLastDipendenti() {
		return this.dipendenteRepository.findTopN(6);
	}
}
