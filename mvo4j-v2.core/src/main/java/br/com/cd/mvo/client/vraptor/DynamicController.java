package br.com.cd.mvo.client.vraptor;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.cd.mvo.Application;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.core.Controller;
import br.com.cd.mvo.core.CrudController;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.StringUtils;
import br.com.cd.mvo.web.util.WebUtil;

@Resource
public class DynamicController {

	public static final String CURRENT_BEAN_SINGLETON_NAME = "currentBean";

	public static final String VIEW_PREFIX_PARAM_NAME = "viewPrefix";
	public static final String VIEW_PREFIX_DEFAULT_VALUE = "/";

	public static final String VIEW_SUFIX_PARAM_NAME = "viewSufix";
	public static final String VIEW_SUFIX_DEFAULT_VALUE = ".jsp";

	private String prefix;
	private String sufix;

	private Container beanFactory;
	private Application application;
	private Translator translator;

	private Result result;

	public DynamicController(Container beanFactory, Application application,
			Translator translator, Result result) {
		this.beanFactory = beanFactory;
		this.application = application;
		this.translator = translator;
		this.result = result;
	}

	private Boolean attributesSetted = false;

	@PostConstruct
	public void setServletConfig() {

		ServletContext servletContext = (ServletContext) beanFactory
				.getContainerConfig().getLocalContainer();
		prefix = WebUtil.getInitParameter(servletContext,
				VIEW_PREFIX_PARAM_NAME, VIEW_PREFIX_DEFAULT_VALUE);
		prefix = StringUtils.addBeginSlash(StringUtils.addEndSlash(prefix));

		sufix = WebUtil.getInitParameter(servletContext, VIEW_SUFIX_PARAM_NAME,
				VIEW_SUFIX_DEFAULT_VALUE);

		System.out.println("CrudController.initializing...\nprefix: " + prefix
				+ ", sufix: " + sufix + ", contextPath: "
				+ servletContext.getContextPath());

		synchronized (attributesSetted) {
			if (attributesSetted)
				return;

			this.attributesSetted = true;

			result.include("translator", translator);
			result.include("i18n", translator);
			result.include("application", application);
			result.include("msg", application);
		}

	}

	@SuppressWarnings("rawtypes")
	@Get("/{viewName}/list/{pageNumber}/{pageSize}")
	public void list(@PathVariable("viewName") String viewName,
			@PathVariable("pageNumber") Integer pageNumber,
			@PathVariable("pageSize") Integer pageSize) {

		System.out.println("CrudController.list, viewName : " + viewName);

		if (beanFactory.containsBean(viewName)) {
			Controller bean = beanFactory.getBean(viewName, Controller.class);

			bean.setPageNumber(pageNumber);
			bean.setPageSize(pageSize);
			bean.refreshPage();

			System.out.println("bean: " + bean);

			result.include(bean.getName() + "Bean", bean);
			result.include(CURRENT_BEAN_SINGLETON_NAME, bean);

			result.forwardTo(prefix + viewName + "/list" + sufix);
		}

		result.notFound();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Get("/{viewName}/edit/{id}")
	public void edit(@PathVariable("viewName") String viewName,
			@PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (beanFactory.containsBean(viewName)) {
			CrudController bean = (CrudController) beanFactory
					.getBean(viewName);

			bean.toEditMode();
			if (entityId.getClass().isAssignableFrom(
					bean.getControllerConfig().entityIdType())) {
				bean.setCurrentEntity(entityId);

				System.out.println("bean: " + bean);

				result.include(bean.getName() + "Bean", bean);
				result.include(CURRENT_BEAN_SINGLETON_NAME, bean);

				result.forwardTo(prefix + viewName + "/edit" + sufix);

				return;
			} else {
				LoggerFactory.getLogger(DynamicController.class).error(
						"@TODO: insert message here");
			}
		}

		result.notFound();
	}
}
