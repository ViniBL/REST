package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.service.spec.IUsuarioService;

@RestController
public class UsuarioRestController {
	
	@Autowired
	private IUsuarioService service;

	private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
	}

	private void parse(Usuario cliente, JSONObject json) {
		Object id = json.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				cliente.setId(((Integer) id).longValue());
			} else {
				cliente.setId((Long) id);
			}
		}

		cliente.setCPF((String) json.get("cpf"));
		cliente.setName((String) json.get("nome"));
		cliente.setUsername((String) json.get("username"));
		cliente.setTelefone((String) json.get("telefone"));
		cliente.setPassword((String) json.get("password"));
		cliente.setRole((String) json.get("role"));
		cliente.setEnabled(true);
		cliente.setSexo((String) json.get("sexo"));
		cliente.setDataNascimento((String) json.get("dataNascimento"));
	}

	@PostMapping(path = "/clientes")
	@ResponseBody
	public ResponseEntity<Usuario> cria(@RequestBody JSONObject json, BCryptPasswordEncoder encoder) {
		try {
			if (isJSONValid(json.toString())) {
				Usuario cliente = new Usuario();
				parse(cliente, json);
				cliente.setPassword(encoder.encode(cliente.getPassword()));
				service.salvar(cliente);
				return ResponseEntity.ok(cliente);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	
	@GetMapping(path = "/clientes")
	public ResponseEntity<List<Usuario>> lista() {
		List<Usuario> lista = service.buscarTodos();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
	}
	
    
	@GetMapping(path = "/clientes/{id}")
	public ResponseEntity<Usuario> listaPorId(@PathVariable("id") Long id) {
		Usuario cliente = service.buscarPorId(id);
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cliente);
	}
	
	@PutMapping(path = "/clientes/{id}")
	public ResponseEntity<Usuario> atualiza(@PathVariable("id") Long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Usuario cliente = service.buscarPorId(id);
				if (cliente == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(cliente, json);
					service.salvar(cliente);
					return ResponseEntity.ok(cliente);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}


	@DeleteMapping(path = "/clientes/{id}")
	public ResponseEntity<Boolean> remove(@PathVariable("id") Long id) {
		Usuario cliente = service.buscarPorId(id);
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		} else {
			service.excluir(id);
			return ResponseEntity.ok(true);
		}
	}
}