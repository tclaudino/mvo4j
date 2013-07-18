package br.com.module.client.m1.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.extjs.ExtJSJson;
import br.com.cd.scaleframework.core.resources.StaticResource;
import br.com.cd.scaleframework.core.resources.StaticResourceType;
import br.com.cd.scaleframework.module.DependentModule;
import br.com.cd.scaleframework.module.DependsOn;
import br.com.cd.scaleframework.module.Module;
import br.com.cd.scaleframework.module.PublishEventType;
import br.com.module.client.m1.model.ProfessorEntity;
import br.com.module.client.m1.service.ProfessorService;

@Module(id = "professorModule", name = "professor", version = "1.0", resourcesFolder = "/webapp", resources = {
		@StaticResource(resourceTypes = StaticResourceType.CSS, folders = { "css" }),
		@StaticResource(resourceTypes = StaticResourceType.JS, folders = { "js" }) })
@Resource
@Path("/professor")
public class ProfessorController {

	// private Controller<Model1DepEntity, Integer> controller;
	private ProfessorService professorService;
	// private ModuleContext context;
	private Result result;

	public ProfessorController(ProfessorService professorService, Result result) {

		this.professorService = professorService;
		this.result = result;
	}

	@DependsOn(onEventType = PublishEventType.READ, modules = { @DependentModule(id = "diciplinaModule", selector = "#tab2") })
	@Get("editar/{id}")
	public void editar(Integer id) {

		result.include("professor", professorService.read(id));
		// + "' + '" + context.getAttributes().get(M1Entity.class).getDate()

		result.use(ExtJSJson.class).from(professorService.read(id)).success()
				.serialize();
	}

	@DependsOn(onEventType = PublishEventType.SAVE, modules = { @DependentModule(id = "diciplinaModule", selector = "#tab2") })
	public void salvar(ProfessorEntity professor) {

		this.professorService.save(professor);
		result.include("message", "Professor Salvo!");
		// controller.save(context.getAttributes().get(Model1DepEntity.class,
		// "contentId"));
	}
}
