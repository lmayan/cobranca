package com.condominio.cobranca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@RequestMapping()
	public String pesquisa(RedirectAttributes attributes) {
		return "redirect:/titulos";
	}

}
