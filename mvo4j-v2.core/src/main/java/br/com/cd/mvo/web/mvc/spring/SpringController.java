package br.com.cd.mvo.web.mvc.spring;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.cd.mvo.Controller;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.mvc.DynamicController;
import br.com.cd.util.ParserUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.stereotype.Controller
public class SpringController extends DynamicController {

	public SpringController(Container container) {
		super(container);
	}

	private void setAttributes(Controller<?> controller, ModelMap map) {

		map.addAttribute(controller.getBeanMetaData().name(), controller);
		map.addAttribute(VIEW_CURRENT_BEAN_VARIABLE_NAME, controller);

		map.addAttribute(VIEW_TRANSLATOR_VARIABLE_NAME, controller.getTranslator());
		map.addAttribute(VIEW_APPLICATION_VARIABLE_NAME, controller.getApplication());
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
	}

	@RequestMapping("/{viewName}/list")
	public String list(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, ModelMap map) {

		return this.list(viewName, -1, -1, map);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/{viewName}/list/{pageNumber}/{pageSize}")
	public String list(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @PathVariable("pageNumber") Integer pageNumber,
			@PathVariable("pageSize") Integer pageSize, ModelMap map) {

		System.out.println("SpringController.list, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		System.out.println("found controller: " + controller);

		pageNumber = pageNumber != null && pageNumber > 0 ? pageNumber : 1;
		pageSize = pageSize != null && pageSize > 0 ? pageSize : controller.getInitialPageSize();
		controller.setPageNumber(pageNumber);
		controller.setPageSize(pageSize);
		controller.refreshPage();
		controller.toViewMode();

		setAttributes(controller, map);

		return VIEW_PREFIX + controller.getListView() + VIEW_SUFFIX;
	}

	@RequestMapping("/{viewName}/list.json")
	public ResponseEntity<?> listJson(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName) {

		return this.listJson(viewName, -1, -1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/{viewName}/list.json/{pageNumber}/{pageSize}")
	public ResponseEntity<?> listJson(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName,
			@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {

		System.out.println("SpringController.listJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		System.out.println("found controller: " + controller);

		pageNumber = pageNumber != null && pageNumber > 0 ? pageNumber : 1;
		pageSize = pageSize != null && pageSize > 0 ? pageSize : controller.getInitialPageSize();
		controller.setPageNumber(pageNumber);
		controller.setPageSize(pageSize);
		controller.refreshPage();
		controller.toViewMode();

		return new ResponseEntity(controller.getEntityList(), HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/{viewName}/edit/{id}")
	public String edit(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @PathVariable("id") Serializable entityId, ModelMap map) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		Class<Serializable> entityIdType = controller.getBeanMetaData().entityIdType();
		Serializable parsedId = ParserUtils.parseObject(entityIdType, entityId);
		if (parsedId == null || !parsedId.getClass().isAssignableFrom(entityIdType)) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(parsedId);
			controller.toEditMode();

			setAttributes(controller, map);

			return VIEW_PREFIX + controller.getEditView() + VIEW_SUFFIX;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/{viewName}/{id}")
	public String view(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @PathVariable("id") Serializable entityId, ModelMap map) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		Class<Serializable> entityIdType = controller.getBeanMetaData().entityIdType();
		Serializable parsedId = ParserUtils.parseObject(entityIdType, entityId);
		if (parsedId == null || !parsedId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(parsedId);
			controller.toViewMode();

			setAttributes(controller, map);

			return VIEW_PREFIX + controller.getEditView() + VIEW_SUFFIX;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/{viewName}.json/{id}")
	public ResponseEntity<?> viewJson(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.editJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		Class<Serializable> entityIdType = controller.getBeanMetaData().entityIdType();
		Serializable parsedId = ParserUtils.parseObject(entityIdType, entityId);
		if (parsedId == null || !parsedId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();

		} else {

			System.out.println("found controller: " + controller);

			controller.setCurrentEntity(parsedId);
			controller.toViewMode();

			return new ResponseEntity(controller.getCurrentEntity(), HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{viewName}", method = RequestMethod.POST)
	public String post(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @ModelAttribute(PATH_VARIABLE_ENTITY) Object entity,
			ModelMap map) {

		System.out.println("CrudController.post, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().equals(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();
		}

		System.out.println("found controller: " + controller);

		controller.save(entity);
		controller.setCurrentEntity(entity);
		controller.toViewMode();

		setAttributes(controller, map);

		return VIEW_PREFIX + controller.getEditView() + VIEW_SUFFIX;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{viewName}.json", method = RequestMethod.POST)
	public ResponseEntity<?> postJson(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @RequestBody String entityAsString) {

		System.out.println("CrudController.postJson, viewName : " + viewName);

		Object entity;
		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		// if
		// (!entity.getClass().equals(controller.getBeanMetaData().targetEntity()))
		// {

		try {
			entity = new ObjectMapper().readValue(entityAsString, controller.getBeanMetaData().targetEntity());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResourceNotFoundException();
		}

		// LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
		// throw new ResourceNotFoundException();
		// }

		System.out.println("found controller: " + controller);

		controller.save(entity);
		controller.setCurrentEntity(entity);
		controller.toViewMode();

		return new ResponseEntity(controller.getCurrentEntity(), HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{viewName}", method = RequestMethod.PUT)
	public String update(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @ModelAttribute(PATH_VARIABLE_ENTITY) Object entity,
			ModelMap map) {

		System.out.println("CrudController.post, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().equals(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();
		}

		System.out.println("found controller: " + controller);

		controller.update(entity);
		controller.toViewMode();

		setAttributes(controller, map);

		return VIEW_PREFIX + controller.getEditView() + VIEW_SUFFIX;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{viewName}.json", method = RequestMethod.PUT)
	public ResponseEntity<?> updateJson(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName,
			@ModelAttribute(PATH_VARIABLE_ENTITY) @RequestBody Object entity) {

		System.out.println("CrudController.postJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		if (!entity.getClass().equals(controller.getBeanMetaData().targetEntity())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();
		}

		System.out.println("found controller: " + controller);

		controller.update(entity);

		return new ResponseEntity(controller.getCurrentEntity(), HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{viewName}/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @PathVariable("id") Serializable entityId, ModelMap map) {

		System.out.println("CrudController.post, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		Class<Serializable> entityIdType = controller.getBeanMetaData().entityIdType();
		Serializable parsedId = ParserUtils.parseObject(entityIdType, entityId);
		if (parsedId == null || !parsedId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();

		} else {

			System.out.println("found controller: " + controller);

			controller.delete(controller.getService().find(entityId));

			controller.toViewMode();

			setAttributes(controller, map);

			return VIEW_PREFIX + controller.getListView() + VIEW_SUFFIX;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{viewName}.json/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteJson(@PathVariable(PATH_VARIABLE_VIEW_NAME) String viewName, @PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.postJson, viewName : " + viewName);

		if (!container.containsBean(viewName))
			throw new ResourceNotFoundException();

		WebCrudController controller = container.getBean(viewName, WebCrudController.class);

		Class<Serializable> entityIdType = controller.getBeanMetaData().entityIdType();
		Serializable parsedId = ParserUtils.parseObject(entityIdType, entityId);
		if (parsedId == null || !parsedId.getClass().isAssignableFrom(controller.getBeanMetaData().entityIdType())) {

			LoggerFactory.getLogger(SpringController.class).error("@TODO: insert message here");
			throw new ResourceNotFoundException();

		} else {

			System.out.println("found controller: " + controller);

			controller.delete(controller.getService().find(entityId));

			return new ResponseEntity(controller.getEntityList(), HttpStatus.OK);
		}
	}
}
