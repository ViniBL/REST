package main.java.br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.Fotos;
import br.ufscar.dc.dsw.domain.Pacote;


public interface IFotosDAO extends CrudRepository<Fotos, Long> {
    
    Fotos findById(long id);

	List<Fotos> findAll();

    List<Fotos> findAllByPacote(Pacote p);
	
	Fotos save(Fotos foto, Long id);

	void deleteById(Long id);
}
