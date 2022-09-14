package br.ufscar.dc.dsw.controller;

import java.util.List;

import javax.validation.Valid;

import java.io.IOException;
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
import br.ufscar.dc.dsw.domain.Agencia;
import br.ufscar.dc.dsw.service.spec.IAgenciaService;
import br.ufscar.dc.dsw.service.spec.IAgenciaService;

@CrossOrigin
@RestController
public class AgenciaRestController {

    private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
 }

	private void parse(Agencia agencia, JSONObject json) {
		
		Object id = json.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				agencia.setId(((Integer) id).longValue());
			} else {
				agencia.setId((Long) id);
			}
 	}

		agencia.setNome((String) json.get("nome"));
		agencia.setSigla((String) json.get("sigla"));
 }

	@GetMapping(path = "/agencias")
	public ResponseEntity<List<Agencia>> lista() {
		List<Agencia> lista = service.findAll();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
 }

	@GetMapping(path = "/agencias/{id}")
	public ResponseEntity<Agencia> lista(@PathVariable("id") long id) {
		Agencia agencia = service.findById(id);
		if (agencia == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(agencia);
 }

	@PostMapping(path = "/agencias")
	@ResponseBody
	public ResponseEntity<Agencia> cria(@RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Agencia agencia = new Agencia();
				parse(agencia, json);
				service.save(agencia);
				return ResponseEntity.ok(agencia);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	@PutMapping(path = "/agencias/{id}")
	public ResponseEntity<Agencia> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Agencia agencia = service.findById(id);
				if (agencia == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(agencia, json);
					service.save(agencia);
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

		Agencia agencia = service.findById(id);
		if (agencia == null) {
			return ResponseEntity.notFound().build();
		} else {
			service.delete(id);
			return ResponseEntity.noContent().build();
		}
	}
}