package br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IPacoteDAO;
import br.ufscar.dc.dsw.domain.Agencia;
import br.ufscar.dc.dsw.domain.Pacote;
import br.ufscar.dc.dsw.service.spec.IPacoteService;

@Service
@Transactional(readOnly = false)
public class PacoteService implements IPacoteService {

	@Autowired
	IPacoteDAO dao;
	
	public void salvar(Pacote pacote) {
		dao.save(pacote);
	}

	public void excluir(Long id) {
		dao.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Pacote buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
	public List<Pacote> buscarTodos() {
		return dao.findAll();
	}

	@Transactional(readOnly = true)
	public List<Pacote> buscarTodosPorAgencia(Agencia a) {
		return dao.findAllByAgencia(a);
	}

	@Transactional(readOnly = true)
	public List<Pacote> buscarTodosPorCidade(String a) {
		return dao.findAllByCidade(a);
	}

	@Transactional(readOnly = true)
	public List<Pacote> buscarTodosPorEstado(String a) {
		return dao.findAllByEstado(a);
	}

	@Transactional(readOnly = true)
	public List<Pacote> buscarTodosPorPais(String a) {
		return dao.findAllByPais(a);
	}
}
