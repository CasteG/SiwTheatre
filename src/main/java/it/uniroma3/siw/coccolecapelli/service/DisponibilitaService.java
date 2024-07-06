package it.uniroma3.siw.coccolecapelli.service;

import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.coccolecapelli.model.Disponibilita;
import it.uniroma3.siw.coccolecapelli.model.Dipendente;
import it.uniroma3.siw.coccolecapelli.repository.DisponibilitaRepository;

@Service
public class DisponibilitaService {

	@Autowired
	private DisponibilitaRepository disponibilitaRepository;
	
	public boolean alreadyExists(Disponibilita target) {
		return this.disponibilitaRepository.existsByDataAndOraInizioAndOraFineAndDipendente(target.getData(), target.getOraInizio(), target.getOraFine(), target.getDipendente());
	}

	public Disponibilita findById(Long idDisponibilita) {
		return this.disponibilitaRepository.findById(idDisponibilita).get();
	}
	
	public List<Disponibilita> findByDipendenteAndActive(Dipendente dipendente) {
		return this.disponibilitaRepository.findByDipendenteAndActiveTrueOrderByDataAscOraInizio(dipendente);
	}
	
	public List<Disponibilita> findByDipendente(Dipendente dipendente) {
		return this.disponibilitaRepository.findByDipendente(dipendente);
	}
	
	public Disponibilita newDisponibilita(Disponibilita d) {
		Disponibilita nd = new Disponibilita();
		nd.setActive(d.getActive());
		nd.setData(d.getData());
		nd.setDipendente(d.getDipendente());
		nd.setOraFine(d.getOraFine());
		nd.setOraInizio(d.getOraInizio());
		return this.disponibilitaRepository.save(nd);
	}
	
	@Transactional
	public void update(Disponibilita disponibilita, Disponibilita newDisponibilita) {
		disponibilita.setData(newDisponibilita.getData());
		disponibilita.setOraInizio(newDisponibilita.getOraInizio());
		disponibilita.setOraFine(newDisponibilita.getOraFine());
		this.disponibilitaRepository.save(disponibilita);
	}
	
	@Transactional
	public void delete(Disponibilita disponibilita) {
		this.disponibilitaRepository.delete(disponibilita);
	}
}
