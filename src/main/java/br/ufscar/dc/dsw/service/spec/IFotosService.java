package main.java.br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Fotos;

public interface IFotosService {

	Fotos buscarPorId(Long id);
	
	List<Fotos> buscarTodos();

    List<Fotos> buscarTodosPorPacote(Pacote p);
	
	void salvar(Fotos foto, Long id);
	
	void excluir(Long id);

    void uploadFile(MultipartFile file, Long id);
	
}
