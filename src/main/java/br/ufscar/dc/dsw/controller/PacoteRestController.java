package br.ufscar.dc.dsw.controller;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

import java.io.IOException;
import java.math.BigDecimal;

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

	@Autowired
	private IAgenciaService serviceAgencia;
	
    private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
 	}


	private void parse(Agencia agencia, JSONObject json) {

		Object id = json.get("id");
		if(id != null){
			if (id instanceof Integer) {
				agencia.setId(((Integer) id).longValue());
			} else {
				agencia.setId((Long) id);
			}
		}

		agencia.setName((String) json.get("name"));
		agencia.setUsername((String) json.get("username"));
		agencia.setCNPJ((String) json.get("CNPJ"));
		agencia.setDescricao((String) json.get("descricao"));
		agencia.setEnabled(true);
		agencia.setPassword((String) json.get("password"));
		agencia.setRole("ROLE_AGENCIA");
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
		Double value = (Double) json.get("valor");
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

 	
	@GetMapping(path = "/pacotes/agencias/{id}")
	public ResponseEntity<List<Pacote>> lista(@PathVariable("id") long id) {
		Agencia a = serviceAgencia.buscarPorId(id);

		try{
			List<Pacote> lista = service.buscarTodosPorAgencia(a);
			if (lista.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(lista);
		}catch(Exception e){
			System.out.println("\n\n\n\n\n\n");
			System.out.println(e.getMessage());
			System.out.println("\n\n\n\n\n\n");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}

	@GetMapping(path = "/pacotes/destinos/{nome}")
	public ResponseEntity<List<Pacote>> lista(@PathVariable("nome") String nome) {
		//Agencia a = serviceAgencia.buscarPorId(id);
		try{
			List<Pacote> lista = service.buscarTodosPorCidade(nome);
			if (lista.isEmpty()) {
				lista = service.buscarTodosPorEstado(nome);
				if(lista.isEmpty()){
					lista = service.buscarTodosPorPais(nome);
					if(lista.isEmpty()){
						return ResponseEntity.notFound().build();
					}
				}
			}
			return ResponseEntity.ok(lista);
		}catch(Exception e){
			System.out.println("\n\n\n\n\n\n");
			System.out.println(e.getMessage());
			System.out.println("\n\n\n\n\n\n");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	

	@PostMapping(path = "/pacotes/agencias/{id}")
	@ResponseBody
	public ResponseEntity<Pacote> cria(@PathVariable("id") long id, @RequestBody JSONObject json) {
		System.out.println(json);
		try {
			if (isJSONValid(json.toString())) {

				Pacote pacote = new Pacote();
				parse(pacote, json);
				Agencia a = serviceAgencia.buscarPorId(id);
				pacote.setAgencia(a);
				service.salvar(pacote);
				
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