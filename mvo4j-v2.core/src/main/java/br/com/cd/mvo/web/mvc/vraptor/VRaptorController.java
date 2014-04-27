package br.com.cd.mvo.web.mvc.vraptor;

import java.io.Serializable;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.cd.mvo.Controller;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.mvc.DynamicController;

@Resource
public class VRaptorController extends DynamicController {

	private Result result;

	public VRaptorController(Container container, Result result) {
		super(container);
		this.result = result;
	}

	private void setAttributes(Controller<?> controller) {

		result.include(controller.getBeanMetaData().name(), controller);
		result.include(VIEW_CURRENT_BEAN_VARIABLE_NAME, controller);

		result.include("translator", controller.getTranslator());
		result.include("i18n", controller.getTranslator());
		result.include("application", controller.getApplication());
		result.include("msg", controller.getApplication());
	}

	@Get("/{viewName}/list")
	public void list(@PathVariable("viewName") String viewName) {

		this.list(viewName, -1, -1);
	}

	@SuppressWarnings("rawtypes")
	@Get("/{viewName}/list/{pageNumber}/{pageSize}")
	public void list(@PathVariable("viewName") String viewName, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {

		System.out.println("VRaptorController.list, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		System.out.println("found controller: " + controller);

		pageNumber = pageNumber != null && pageNumber > 0 ? pageNumber : 1;
		pageSize = pageSize != null && pageSize > 0 ? pageSize : controller.getInitialPageSize();
		controller.setPageNumber(pageNumber);
		controller.setPageSize(pageSize);
		controller.refreshPage();
		controller.toViewMode();

		setAttributes(controller);

		result.forwardTo(VIEW_PREFIX + viewName + controller.getListView() + VIEW_SUFFIX);
	}

	@Get("/{viewName}/list.json")
	public void listJson(@PathVariable("viewName") String viewName) {

		this.listJson(viewName, -1, -1);
	}

	@SuppressWarnings("rawtypes")
	@Get("/{viewName}/list.json/{pageNumber}/{pageSize}")
	public void listJson(@PathVariable("viewName") String viewName, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {

		System.out.println("VRaptorController.listJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		System.out.println("found controller: " + controller);

		pageNumber = pageNumber != null && pageNumber > 0 ? pageNumber : 1;
		pageSize = pageSize != null && pageSize > 0 ? pageSize : controller.getInitialPageSize();
		controller.setPageNumber(pageNumber);
		controller.setPageSize(pageSize);
		controller.refreshPage();
		controller.toViewMode();

		setAttributes(controller);

		result.use(Results.json()).from(controller.getEntityList()).serialize();
	}

	@SuppressWarnings({ "rawtypes" })
	@Get("/{viewName}/edit/{id}")
	public void editFromId(@PathVariable("viewName") String viewName, @PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entityId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(entityId);
			controller.toEditMode();

			setAttributes(controller);

			result.forwardTo(VIEW_PREFIX + viewName + controller.getEditView() + VIEW_SUFFIX);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Get("/{viewName}/edit/{id}")
	public void edit(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(entity);
			controller.toEditMode();

			setAttributes(controller);

			result.forwardTo(VIEW_PREFIX + viewName + controller.getEditView() + VIEW_SUFFIX);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	@Get("/{viewName}/{id}")
	public void viewFromId(@PathVariable("viewName") String viewName, @PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entityId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(entityId);
			controller.toViewMode();

			setAttributes(controller);

			result.forwardTo(VIEW_PREFIX + viewName + controller.getEditView() + VIEW_SUFFIX);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Get("/{viewName}/{id}")
	public void view(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(entity);
			controller.toViewMode();

			setAttributes(controller);

			result.forwardTo(VIEW_PREFIX + viewName + controller.getEditView() + VIEW_SUFFIX);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	@Get("/{viewName}.json/{id}")
	public void viewJsonFromId(@PathVariable("viewName") String viewName, @PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.editJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entityId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(entityId);
			controller.toViewMode();

			setAttributes(controller);

			result.use(Results.json()).from(controller.getCurrentEntity()).serialize();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Get("/{viewName}/{id}")
	public void viewJson(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(entity);
			controller.toViewMode();

			setAttributes(controller);

			result.use(Results.json()).from(controller.getCurrentEntity()).serialize();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Post("/{viewName}/{id}")
	public void post(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.post, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.save(entity);

			result.redirectTo(this).view(viewName, entity);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Post("/{viewName}/{id}")
	@Consumes("application/json")
	public void postJson(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.postJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.save(entity);

			result.redirectTo(this).viewJson(viewName, entity);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Put("/{viewName}/{id}")
	public void update(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.post, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.update(entity);

			result.redirectTo(this).view(viewName, entity);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Put("/{viewName}/{id}")
	@Consumes("application/json")
	public void updateJson(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.postJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.update(entity);

			result.redirectTo(this).viewJson(viewName, entity);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Delete("/{viewName}/{id}")
	public void delete(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.post, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.delete(entity);

			result.redirectTo(this).list(viewName);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Delete("/{viewName}/{id}")
	@Consumes("application/json")
	public void deleteJson(@PathVariable("viewName") String viewName, @PathVariable("id") Object entity) {

		System.out.println("CrudController.postJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			result.notFound();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().isAssignableFrom(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(VRaptorController.class).error("@TODO: insert message here");
			result.notFound();

		} else {

			System.out.println("found controller: " + controller);

			controller.delete(entity);

			result.redirectTo(this).listJson(viewName);
		}
	}
}
