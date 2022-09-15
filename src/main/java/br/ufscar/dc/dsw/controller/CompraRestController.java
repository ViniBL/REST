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
import br.ufscar.dc.dsw.service.spec.IPacoteService;
import br.ufscar.dc.dsw.service.spec.IUsuarioService;
import br.ufscar.dc.dsw.domain.Compra;
import br.ufscar.dc.dsw.domain.Compra;
import br.ufscar.dc.dsw.service.spec.ICompraService;
import br.ufscar.dc.dsw.service.spec.ICompraService;

@CrossOrigin
@RestController
public class CompraRestController {

    @Autowired
	private ICompraService service;
	
	@Autowired
	private IPacoteService pacoteService;

	@Autowired 
	private IUsuarioService usuarioService;

    private boolean isJSONValid(String jsonInString) {
		try {
			return new ObjectMapper().readTree(jsonInString) != null;
		} catch (IOException e) {
			return false;
		}
 }

	private void parse(Compra compra, JSONObject json) {
		
		Object id = json.get("id");
		if (id != null) {
			if (id instanceof Integer) {
				compra.setId(((Integer) id).longValue());
			} else {
				compra.setId((Long) id);
			}
 	}

		/*compra.setNome((String) json.get("nome"));
		compra.setSigla((String) json.get("sigla"));*/
 }

	/*@GetMapping(path = "/compras")
	public ResponseEntity<List<Compra>> lista() {
		List<Compra> lista = service.bus();
		if (lista.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lista);
 }*/

	@GetMapping(path = "/compras/{id}")
	public ResponseEntity<Compra> lista(@PathVariable("id") long id) {
		Compra compra = service.buscarPorId(id);
		if (compra == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(compra);
 }

	@PostMapping(path = "/compras")
	@ResponseBody
	public ResponseEntity<Compra> cria(@RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Compra compra = new Compra();
				parse(compra, json);
				service.salvar(compra);
				return ResponseEntity.ok(compra);
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	@PutMapping(path = "/compras/{id}")
	public ResponseEntity<Compra> atualiza(@PathVariable("id") long id, @RequestBody JSONObject json) {
		try {
			if (isJSONValid(json.toString())) {
				Compra compra = service.buscarPorId(id);
				if (compra == null) {
					return ResponseEntity.notFound().build();
				} else {
					parse(compra, json);
					service.salvar(compra);
					return ResponseEntity.ok(compra);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
 }

	/*@DeleteMapping(path = "/compras/{id}")
 public ResponseEntity<Boolean> remove(@PathVariable("id") long id) {

		Compra compra = service.buscarPorId(id);
		if (compra == null) {
			return ResponseEntity.notFound().build();
		} else {
			service.excl(id);
			return ResponseEntity.noContent().build();
		}
	}*/
}