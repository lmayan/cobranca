package com.condominio.cobranca.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.condominio.cobranca.model.StatusTitulo;
import com.condominio.cobranca.model.Titulo;
import com.condominio.cobranca.repository.filter.TituloFilter;
import com.condominio.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos") // Define a raiz do controller evitando repeticao de codigo 
public class TituloController {
	
	@Autowired //Anotação usada para fazer o instanciamento do objeto de forma automatica.
	private CadastroTituloService cadastroTituloService; //Evita o nullpointer.
	
	@RequestMapping() //Define o acesso a pagina Raiz, ou seja ja abre pesquisando um titulo 
	public ModelAndView pesquisa(@ModelAttribute("filtro") TituloFilter filtro) {
		//@ModelAttribute("filtro") = informa ao SPRING o nome do objeto relacionado na VIEW
		List<Titulo> lst= cadastroTituloService.pesquisar(filtro);
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("listaTitulos", lst);
		return mv;
	}
	
	@RequestMapping(value="{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String atualizarStatus(@PathVariable Long codigo) {
		// System.out.println(">>> RECEBENDO: "+codigo);
		// cadastroTituloService.receber(codigo);
		return cadastroTituloService.receber(codigo);
		//@ResponseBody informa ao Spring que queremos retornar apenas uma RESPOSTA sem uma VIEW
	}
	
	@RequestMapping(value="{codigo}") //Define que ira receber um PARAMETRO no PATH: (/titulos/{codigo})
	public ModelAndView edicao(@PathVariable Long codigo) { //Define o parametro que sera enviado.
		//System.out.println("Teste EDICAO: "+">>>> "+codigo);
		Optional<Titulo> titulo = cadastroTituloService.editar(codigo);
		ModelAndView mv = new ModelAndView("CadastroTitulo");
		mv.addObject("titulo", titulo);
		return mv;
	}
	
	@RequestMapping(value="{codigo}/deletar")
	public String excluir( @PathVariable Long codigo, RedirectAttributes attributes) {
		cadastroTituloService.deletar(codigo);
		attributes.addFlashAttribute("mensagem", "Titulo Excluido com sucesso!");
		return "redirect:/titulos";
		//RedirectAttributes attributes = cria um objeto de redirecionamento mais LEVE
		//attributes.addFlashAttribute(); SEMELHANTE ao mv.addObject();
		// Com a diferenca que nao precisamos criar um objeto ModelAndView passamos info por atributos
	}
	

	@RequestMapping("/novo") // Define o caminho requisicao, sem a definicao anterior ficaria ("/titulos/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroTitulo");
		mv.addObject(new Titulo()); // Cria um novo objeto titulo e o deixa disponivel
		//Setando lista de status no objeto 'statusTitulo' 
		// mv.addObject("statusTitulo", StatusTitulo.values());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView salvar(@Validated Titulo titulo, Errors errors) { 
		//@Validated comando validacao SPRING
		//Errors objeto criado pelo SPRING de forma automatica caso haja erro na validacao
		//System.out.println("Teste SALVAR: "+">>>> "+titulo.toString());
		ModelAndView mv = new ModelAndView("CadastroTitulo"); //Criando objeto que retorna dados
		if(errors.hasErrors()) { //Caso tenha erro ja retorna pra pagina
			return mv;
		}
		//System.out.println("ERRORS: "+errors);
		try {
			cadastroTituloService.salvar(titulo); // Salvando objeto
			mv.addObject("mensagem", "Titulo Salvo com Sucesso!");
			mv.addObject(new Titulo()); //Seta um novo objeto depois de SALVAR sem usar o Redirect.
			return mv;
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage()); //objeto-errorCode-String(message)
			return mv;
		}
	}
	
	// Carrega a lista do box ao carregar o model(titulo) na view
	// Usado para evitar a repeticao do codigo: mv.addObject("statusTitulo", StatusTitulo.values());
	// toda vez que precisar carregar o combo box, depois de salvar por exemplo.
	@ModelAttribute("statusTitulo")
	public List<StatusTitulo> statusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}

}
