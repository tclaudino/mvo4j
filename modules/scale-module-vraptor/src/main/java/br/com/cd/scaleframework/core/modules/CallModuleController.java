package br.com.cd.scaleframework.core.modules;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.cd.scaleframework.core.modules.service.ModuleFactory;

@Resource
@Path("/modules")
public class CallModuleController {

	private Result result;
	private ModuleFactory modulesManager;

	public CallModuleController(Result result, ModuleFactory modulesManager) {
		this.result = result;
		this.modulesManager = modulesManager;
	}

	@Get
	@Path(value = "{moduleName}")
	public void index(String moduleName) {

		System.out.println("CallModuleController.index, moduleName: "
				+ moduleName);

		// ViewModule<ExtJSJson> module = modulesManager.getModule(
		// ExtJSJson.class, moduleName);
		//
		// ExtJSJson view = this.result.use(ExtJSJson.class);
		//
		// module.doRequest(view);
	}

}
