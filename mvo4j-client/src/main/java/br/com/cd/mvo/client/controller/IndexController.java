package br.com.cd.mvo.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT })
	public String redirect() {
		return "redirect:/home";
	}

	@RequestMapping("/home")
	public ModelAndView welcome() {
		return new ModelAndView("welcome");
	}
}
