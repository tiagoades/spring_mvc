package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.Titulos;
import com.algaworks.cobranca.repository.filter.TituloFilter;
import com.algaworks.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	public static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	CadastroTituloService cadastroTituloService;

	@RequestMapping("/novo")
	public ModelAndView novo() {

		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);

		mv.addObject(new Titulo());

		return mv;

	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {

		if (errors.hasErrors()) {

			return CADASTRO_VIEW;

		}

		try {

			// titulos.save(titulo);

			cadastroTituloService.salvar(titulo);

			attributes.addFlashAttribute("mensagem", "Titulo Salvo com Sucesso...");

			return "redirect:/titulos/novo";

		} catch (IllegalArgumentException e) {

			errors.rejectValue("dataVencimento", null, e.getMessage());

			return CADASTRO_VIEW;

		}
	}

	@RequestMapping
	// public ModelAndView pesquisar(@RequestParam(defaultValue = "") String
	// descricao) {

	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {

		List<Titulo> todosTitulos = cadastroTituloService.filtrar(filtro);

		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		return mv;

	}

	/*
	 * @RequestMapping public ModelAndView pesquisar(String descricao) {
	 * 
	 * List<Titulo> todosTitulos;
	 * 
	 * todosTitulos = titulos.findAll();
	 * 
	 * for (Titulo titulo : todosTitulos) {
	 * 
	 * System.out.println(">>>>>>>>>>>>>>>>>>> " + titulo.getDescricao() +
	 * " <<<<<<<<<<<<<<<<<<<<");
	 * 
	 * }
	 * 
	 * ModelAndView mv = new ModelAndView("PesquisaTitulos");
	 * mv.addObject("titulos", todosTitulos); return mv;
	 * 
	 * }
	 */

	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {

		// Optional<Titulo> titulo = titulos.findById(codigo);

		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);

		mv.addObject(titulo);

		return mv;
	}

	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {

		cadastroTituloService.excluir(codigo);

		attributes.addFlashAttribute("mensagem", "Titulo Excluido com Sucesso...");

		return "redirect:/titulos";

	}

	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {

		return cadastroTituloService.receber(codigo);

	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {

		return Arrays.asList(StatusTitulo.values());

	}

}
