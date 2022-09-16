package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Agencia;
import br.ufscar.dc.dsw.domain.Pacote;

@SuppressWarnings("unchecked")
public interface IPacoteDAO extends CrudRepository<Pacote, Long>{

	Pacote findById(long id);

	List<Pacote> findAll();
	
	Pacote save(Pacote pacote);

	void deleteById(Long id);

	List<Pacote> findAllByAgencia(Agencia a);

	List<Pacote> findAllByCidade(String a);

	List<Pacote> findAllByEstado(String a);

	List<Pacote> findAllByPais(String a);
}