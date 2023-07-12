package com.siw.resell.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siw.resell.model.Prodotto;
import com.siw.resell.repository.ProdottoRepository;

@Service
public class ProdottoService {

	@Autowired
	private ProdottoRepository prodottoRepository;

	@Transactional
	public Prodotto save(Prodotto prodotto) {
		return prodottoRepository.save(prodotto); // dopo aver salvato, ritorna il prodotto salvato (avrebbe risolto un
													// sacco di problemi su Catering, avrei potuto omettere il pulsante
													// indietro)
	}

	@Transactional
	public void delete(Prodotto prodotto) {
		prodottoRepository.delete(prodotto);
	}

	@Transactional
	public void deleteById(Long id) {
		prodottoRepository.deleteById(id);
	}

	public Prodotto findById(Long id) {
		return prodottoRepository.findById(id).get();
	}

	public List<Prodotto> findAll() {
		List<Prodotto> prodottos = prodottoRepository.findAllByOrderByIdDesc(); //findAllByOrderByIdDesc
		// ritorna una lista, non un iterable, quindi non c'è più bisogno del foreach

		return prodottos;
	}

	public List<Prodotto> findFirstN(int n) {
		List<Prodotto> prodottos = prodottoRepository.findAllByOrderByIdDesc();

		if (prodottos.size() <= n) {
			return prodottos;
		}

		List<Prodotto> nProdottos = new ArrayList<Prodotto>();
		;

		for (int i = 0; i < n; i++) {
			nProdottos.add(prodottos.get(i));
		}

		return nProdottos;
	}

	public List<Prodotto> findByKeyword(String keyword) {
		return prodottoRepository.findByKeyword(keyword);
	}

	public boolean alreadyExists(Prodotto prodotto) {
		return prodottoRepository.existsByNomeAndDescrizioneAndFotoAndUserAndCategoria(
				prodotto.getNome(), prodotto.getDescrizione(), prodotto.getFoto(), prodotto.getUser(),
				prodotto.getCategoria());
	}

}
