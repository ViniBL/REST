package main.java.br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.java.br.ufscar.dc.dsw.service.spec.IFotosService;


@Controller
@RequestMapping("/fotos")
public class FotosController {
	
	@Autowired
	private IFotosService service;

	
	@PostMapping("/uploadFile/{id}")
	public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(name = "id") String id ) {
		Fotos foto = service.salvar(foto, id);
        return "pacote/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
					
		model.addAttribute("fotos",service.buscarTodosPorUsuario(this.getPacote()));
		
		return "fotos/lista";
	}
	
	@PostMapping("/salvar/{id}")
	public String salvar(@Valid Fotos foto, BindingResult result, RedirectAttributes attr, @PathVariable(name = "id") String id) {
		
		if (result.hasErrors()) {
			return "pacote/cadastro";
		}
		
		service.salvar(foto, id);
		attr.addFlashAttribute("sucess", "Foto inserida com sucesso.");
		return "redirect:/fotos/listar";
	}	
		
}
