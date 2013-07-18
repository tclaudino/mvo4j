package br.com.module.client.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class HomeController {

	private Result result;

	public HomeController(Result result) {
		this.result = result;
	}

	@Get
	@Path("/home")
	public void index() {

	}

}
