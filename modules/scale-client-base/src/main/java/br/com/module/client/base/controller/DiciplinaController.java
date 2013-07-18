package br.com.module.client.base.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.extjs.ExtJSJson;
import br.com.cd.scaleframework.core.resources.StaticResource;
import br.com.cd.scaleframework.core.resources.StaticResourceType;
import br.com.cd.scaleframework.module.Module;
import br.com.cd.scaleframework.module.PublishEvent;
import br.com.cd.scaleframework.module.PublishEventType;
import br.com.module.client.base.model.DiciplinaEntity;
import br.com.module.client.base.service.DiciplinaService;

@Module(id = "diciplinaModule", name = "diciplina", version = "1.0", resourcesFolder = "/webapp", resources = { @StaticResource(resourceTypes = {
		StaticResourceType.JS, StaticResourceType.CSS }, folders = { "diciplina" }) })
@Resource
@Path("/diciplina")
public class DiciplinaController {

	// private Controller<ClientModelEntity, Integer> controller;
	private DiciplinaService diciplinaService;
	private Result result;

	public DiciplinaController(DiciplinaService diciplinaService, Result result) {
		this.diciplinaService = diciplinaService;
		this.result = result;
	}

	@Get
	@Path(value = "lista.json")
	public void lista() {

		result.use(ExtJSJson.class).from(diciplinaService.list()).success()
				.serialize();
	}

	@Get("editar/{diciplina.id}")
	@PublishEvent(eventType = PublishEventType.READ)
	public void editar(Integer id) {

		result.include("diciplina", diciplinaService.read(id));
	}

	@Get("salvar/")
	@PublishEvent(eventType = PublishEventType.UPDATE)
	public void salvar(DiciplinaEntity diciplina) {

		diciplinaService.save(diciplina);

		result.include("message", "Dicliplina Salva!");
		result.redirectTo(this).lista();
	}
}
