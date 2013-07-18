package br.com.scaleframework.vraptor;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.web.bind.annotation.PathVariable;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.controller.Controller;
import br.com.cd.scaleframework.core.dynamic.WebCrudControllerBean;
import br.com.cd.scaleframework.core.proxy.CrudControllerProxy;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.context.WebApplicationContext;
import br.com.cd.scaleframework.web.util.WebUtil;

@Resource
public class DynamicController {

	public static final String CURRENT_BEAN_SINGLETON_NAME = "currentBean";

	public static final String VIEW_PREFIX_PARAM_NAME = "viewPrefix";
	public static final String VIEW_PREFIX_DEFAULT_VALUE = "/";

	public static final String VIEW_SUFIX_PARAM_NAME = "viewSufix";
	public static final String VIEW_SUFIX_DEFAULT_VALUE = ".jsp";

	private String prefix;
	private String sufix;

	private WebApplicationContext applicationContext;
	private Translator translator;
	private Messenger messenger;

	private Result result;

	public DynamicController(WebApplicationContext applicationContext,
			Translator translator, Messenger messenger, Result result) {
		this.applicationContext = applicationContext;
		this.translator = translator;
		this.messenger = messenger;
		this.result = result;
	}

	private Boolean attributesSetted = false;

	@PostConstruct
	public void setServletConfig() {

		ServletContext servletContext = applicationContext.getServletContext();
		prefix = WebUtil.getContextParameter(servletContext,
				VIEW_PREFIX_PARAM_NAME, VIEW_PREFIX_DEFAULT_VALUE);
		prefix = StringUtils.addBeginSlash(StringUtils.addEndSlash(prefix));

		sufix = WebUtil.getContextParameter(servletContext,
				VIEW_SUFIX_PARAM_NAME, VIEW_SUFIX_DEFAULT_VALUE);

		System.out.println("CrudController.initializing...\nprefix: " + prefix
				+ ", sufix: " + sufix + ", contextPath: "
				+ servletContext.getContextPath());

		synchronized (attributesSetted) {
			if (attributesSetted)
				return;

			this.attributesSetted = true;

			result.include("translator", translator);
			result.include("i18n", translator);
			result.include("messenger", messenger);
			result.include("msg", messenger);
		}

	}

	@SuppressWarnings("rawtypes")
	@Get("/{viewName}/list/{pageNumber}/{pageSize}")
	public void list(@PathVariable("viewName") String viewName,
			@PathVariable("pageNumber") Integer pageNumber,
			@PathVariable("pageSize") Integer pageSize) {

		System.out.println("CrudController.list, viewName : " + viewName);

		if (applicationContext.containsBean(viewName)) {
			Controller bean = applicationContext.getBean(viewName,
					Controller.class);

			bean.setPageNumber(pageNumber);
			bean.setPageSize(pageSize);
			bean.refreshPage();

			System.out.println("bean: " + bean);

			String sigletonName = CURRENT_BEAN_SINGLETON_NAME;
			if (bean.getClass()
					.isAnnotationPresent(WebCrudControllerBean.class)) {
				sigletonName = bean.getClass()
						.getAnnotation(WebCrudControllerBean.class).name();
			}
			result.include(sigletonName, bean);

			result.forwardTo(prefix + viewName + "/list" + sufix);
		}

		result.notFound();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Get("/{viewName}/edit/{id}")
	public void edit(@PathVariable("viewName") String viewName,
			@PathVariable("id") Serializable entityId) {

		System.out.println("CrudController.edit, viewName : " + viewName);

		if (applicationContext.containsBean(viewName)) {
			CrudControllerProxy bean = (CrudControllerProxy) applicationContext
					.getBean(viewName);

			Class entityClass = bean.newEntity().getClass();

			System.out.println("entityClass: " + entityClass);

			Serializable id = ParserUtils.parseObject(bean.getComponent()
					.getComponentConfig().getEntityIdType(), entityId);

			bean.toUpdateMode();
			bean.setCurrentEntity(id);

			System.out.println("bean: " + bean);

			String sigletonName = CURRENT_BEAN_SINGLETON_NAME;
			if (bean.getClass()
					.isAnnotationPresent(WebCrudControllerBean.class)) {
				sigletonName = bean.getClass()
						.getAnnotation(WebCrudControllerBean.class).name();
			}
			result.include(sigletonName, bean);
			result.forwardTo(prefix + viewName + "/edit" + sufix);
		}

		result.notFound();
	}
}
