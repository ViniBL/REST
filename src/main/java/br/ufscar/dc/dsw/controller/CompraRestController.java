package br.ufscar.dc.dsw.controller;

import java.util.List;


import java.io.IOException;
import java.math.BigDecimal;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw.domain.Agencia;
import br.ufscar.dc.dsw.domain.Compra;
import br.ufscar.dc.dsw.domain.Pacote;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.service.spec.ICompraService;
import br.ufscar.dc.dsw.service.spec.IUsuarioService;

@CrossOrigin
@RestController
public class CompraRestController {

    @Autowired
    private ICompraService service;

    @Autowired
    private IUsuarioService serviceUsuario;

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
		Double value = (Double) json.get("preco");
		pacote.setValor(BigDecimal.valueOf(value));
		pacote.setDescricao((String) json.get("descricao"));
		pacote.setAgencia((Agencia) json.get("agencia"));
		Agencia age = new Agencia();
		parse(age, json);
		pacote.setAgencia(age);
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
		cliente.setRole("ROLE_USUARIO");
		cliente.setEnabled(true);
		cliente.setSexo((String) json.get("sexo"));
		cliente.setDataNascimento((String) json.get("dataNascimento"));
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

		compra.setStatus((String) json.get("status"));
        Pacote pacote = new Pacote();
        parse(pacote, json);
		compra.setPacote(pacote);
        Usuario usuario = new Usuario();
        parse(usuario, json);
	}

    @GetMapping(path = "/pacotes/clientes/{id}")
    public ResponseEntity<List<Compra>> lista(@PathVariable("id") long id){
        Usuario u = serviceUsuario.buscarPorId(id);
        List<Compra> lista = service.buscarTodosPorUsuario(u);
        try{
            if(lista.isEmpty()){
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

    
}
