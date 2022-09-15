package br.ufscar.dc.dsw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import java.util.Map;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Agencia;
import br.ufscar.dc.dsw.domain.Compra;
import br.ufscar.dc.dsw.domain.Pacote;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.service.spec.IAgenciaService;
import br.ufscar.dc.dsw.service.spec.ICompraService;
import br.ufscar.dc.dsw.service.spec.IPacoteService;
import br.ufscar.dc.dsw.service.spec.IUsuarioService;

@CrossOrigin
@RestController
public class PacoteRestController {

	@Autowired
	private IPacoteService service;
	
	@Autowired
	private IUsuarioService serviceClient;

	@Autowired
	private ICompraService serviceCompra;
	
    private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
 	}

	@SuppressWarnings("unchecked")
	private void parse(Agencia agencia, JSONObject json) {

		Map<String, Object> map = (Map<String, Object>) json.get("agencia");

		Object id = map.get("id");
		if (id instanceof Integer) {
			agencia.setId(((Integer) id).longValue());
		} else {
			agencia.setId(((Long) id));
		}

		agencia.setCNPJ((String) map.get("cnpj"));
		agencia.setDescricao((String) map.get("descricao"));
	}

	private void parse(Pacote pacote, JSONObject json) {
		
		Object id = json.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				pacote.setId(((Integer) id).longValue());
			} else {
				pacote.setId((Long) id);
			}
 		}
		 System.out.println("fee");
		pacote.setCidade((String) json.get("cidade"));
		System.out.println("fefef");
		pacote.setData_partida((String) json.get("data_partida"));
		pacote.setDuracao((Integer) json.get("duracao"));
		pacote.setEstado((String) json.get("estado"));
		pacote.setPais((String) json.get("pais"));
		Double value = (Double) json.get("preco");
		pacote.setValor(BigDecimal.valueOf(value));
		pacote.setDescricao((String) json.get("descricao"));
		pacote.setAgencia((Agencia) json.get("agencia"));
		Agencia age = new Agencia();
		parse(age, json);
		pacote.setAgencia(age);
 }

	@GetMapping(path = "/pacotes")
	public ResponseEntity<List<Pacote>> lista() {
		List<Pacote> lista = service.buscarTodos();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
 }

	@GetMapping(path = "/pacotes/clientes/{id}")
	public ResponseEntity<List<Pacote>> lista(@PathVariable("id") long id) {
		Usuario cliente = serviceClient.buscarPorId(id);
		List<Compra> clientePacote = serviceCompra.buscarTodosPorUsuario(cliente);

		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}

		List<Pacote> pacotes = new ArrayList<>();
		for(int i = 0; i < clientePacote.size(); i++) {
			pacotes.set(i, clientePacote.get(i).getPacote());
		}
		return ResponseEntity.ok(pacotes);
 	}

	@PostMapping(path = "/pacotes")
	@ResponseBody
	public ResponseEntity<Pacote> cria(@RequestBody JSONObject json) {
		System.out.println(json);
		try {
			if (isJSONValid(json.toString())) {

				Pacote pacote = new Pacote();
				System.out.println("fe");
				parse(pacote, json);
				service.salvar(pacote);
				System.out.println("ent");
				return ResponseEntity.ok(pacote);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	@PutMapping(path = "/pacotes/{id}")
	public ResponseEntity<Pacote> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Pacote pacote = service.buscarPorId(id);
				if (pacote == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(pacote, json);
					service.salvar(pacote);
					return ResponseEntity.ok(pacote);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	@DeleteMapping(path = "/pacotes/{id}")
 public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Pacote pacote = service.buscarPorId(id);
		if (pacote == null) {
			return ResponseEntity.notFound().build();
		} else {
			service.excluir(id);
			return ResponseEntity.noContent().build();
		}
	}
}