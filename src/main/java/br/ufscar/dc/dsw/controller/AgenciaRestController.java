package br.ufscar.dc.dsw.controller;

import java.util.List;


import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import br.ufscar.dc.dsw.service.spec.IAgenciaService;

@CrossOrigin
@RestController
public class AgenciaRestController {

	@Autowired
	private IAgenciaService service;

    private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
 }

 	//@SuppressWarnings("unchecked")
	private void parse(Agencia agencia, JSONObject json) {
		//Map<String, Object> map = (Map<String, Object>) json.get("agencia");

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

	@GetMapping(path = "/agencias")
	
	public ResponseEntity<List<Agencia>> lista() {
		List<Agencia> lista = service.buscarTodos();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
 }

	@GetMapping(path = "/agencias/{id}")
	public ResponseEntity<Agencia> lista(@PathVariable("id") long id) {
		Agencia agencia = service.buscarPorId(id);
		if (agencia == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(agencia);
 }

	@PostMapping(path = "/agencias")
	@ResponseBody
	public ResponseEntity<Agencia> cria(@RequestBody JSONObject json, BCryptPasswordEncoder encoder) {
		try {
			if (isJSONValid(json.toString())) {
				Agencia agencia = new Agencia();
				parse(agencia, json);
				agencia.setPassword(encoder.encode(agencia.getPassword()));
				service.salvar(agencia);
				return ResponseEntity.ok(agencia);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			System.out.println("\n\n\n\n\n\n");
			System.out.println(e.getMessage());
			System.out.println("\n\n\n\n\n\n");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	@PutMapping(path = "/agencias/{id}")
	public ResponseEntity<Agencia> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Agencia agencia = service.buscarPorId(id);
				if (agencia == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(agencia, json);
					service.salvar(agencia);
					return ResponseEntity.ok(agencia);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	@DeleteMapping(path = "/agencias/{id}")
 public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Agencia agencia = service.buscarPorId(id);
		if (agencia == null) {
			return ResponseEntity.notFound().build();
		} else if(!agencia.getPacotes().isEmpty()){
			return ResponseEntity.ok(false);
		}
		else{
			service.excluir(id);
			return ResponseEntity.ok(true);
		}
	}
}