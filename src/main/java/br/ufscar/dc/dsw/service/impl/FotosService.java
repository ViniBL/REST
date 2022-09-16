package main.java.br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.br.ufscar.dc.dsw.domain.Fotos;
import br.ufscar.dc.dsw.domain.Pacote;
import br.ufscar.dc.dsw.dao.IFotosDAO;
import br.ufscar.dc.dsw.dao.IPacoteDAO;
import br.ufscar.dc.dsw.service.spec.IFotosService;

@Service
@Transactional(readOnly = false)
public class FotosService implements IFotosService {
    @Autowired
	IFotosDAO dao;
    IPacoteDAO pacoteDAO;
	
	public Fotos salvar(Fotos foto, Long id) {
		return uploadFile(file, id);
	}

	public void excluir(Long id) {
		dao.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Fotos buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
	public List<Fotos> buscarTodos() {
		return dao.findAll();
	}

    @Transactional(readOnly = true)
	public List<Fotos> buscarTodosPorPacote(Pacote p) {
		return dao.findAllByPacote(p);
	}

    @Transactional(readOnly = true)
	public Fotos uploadFile(MultipartFile file, Long id) {
		byte[] data = file.getBytes();
        String filename = file.getOriginalFileName();
        String type = file.getContentType();
        Pacote pacote = pacoteDAO.findById(id);

        Fotos foto = new Fotos(filename,data, type, pacote);

        pacote.addFoto(foto);

        return foto;
	}
}   
