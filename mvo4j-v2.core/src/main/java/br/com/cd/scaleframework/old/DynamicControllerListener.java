package br.com.cd.scaleframework.beans.dynamic.factory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.CacheManager;
import br.com.cd.scaleframework.context.InitParamKeys;
import br.com.cd.scaleframework.context.KeyValuesProvider;
import br.com.cd.scaleframework.context.Translator;
import br.com.cd.scaleframework.context.support.DefaultApplication;
import br.com.cd.scaleframework.context.support.DefaultCacheManager;
import br.com.cd.scaleframework.context.support.DefaultKeyValuesProvider;
import br.com.cd.scaleframework.context.support.DefaultTranslator;
import br.com.cd.scaleframework.controller.ControllerListener;
import br.com.cd.scaleframework.controller.DataModelFactory;
import br.com.cd.scaleframework.controller.dynamic.ControllerBean;
import br.com.cd.scaleframework.controller.dynamic.WriteablePropertyMap;
import br.com.cd.scaleframework.controller.support.DefaultDataModelFactory;
import br.com.cd.scaleframework.core.orm.Service;
import br.com.cd.scaleframework.core.orm.suppport.AbstractJpaRepository;
import br.com.cd.scaleframework.web.controller.WebCrudController;
import br.com.cd.scaleframework.web.controller.dynamic.WebCrudControllerBeanConfig;
import br.com.cd.scaleframework.web.controller.support.DefaultWebCrudController;
import br.com.cd.scaleframework.web.util.WebUtil;

public class DynamicControllerListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext servletContext = event.getServletContext();
	}

	public void initialize(ServletContext servletContext) {

		int cacheTime = WebUtil.getInitParameter(servletContext,
				InitParamKeys.TRANSLATES_CACHETIME,
				InitParamKeys.DefaultValues.TRANSLATES_CACHETIME);

		CacheManager cacheManager = new DefaultCacheManager(cacheTime);

		KeyValuesProvider keyValuesProvider = new DefaultKeyValuesProvider(
				cacheManager);

		String defaultLocale = "en-US";
		String currentLocale = "en-US";
		List<String> suportedLocaleLanguages = new ArrayList<String>();

		String appBundleName = "";
		Translator appTranslator = new DefaultTranslator(appBundleName,
				keyValuesProvider, new Locale(currentLocale), null);

		Application application = new DefaultApplication(servletContext,
				defaultLocale, suportedLocaleLanguages.toArray(new String[] {}));

		this.initialize(servletContext, application, keyValuesProvider,
				appTranslator);
	}

	public void initialize(ServletContext servletContext,
			Application application, KeyValuesProvider keyValuesProvider,
			Translator appTranslator) {

		List<Class> classes = this.findDynamicClasses(ControllerBean.class);
	}

	public void proxy(ServletContext servletContext, Application application,
			KeyValuesProvider keyValuesProvider, Translator appTranslator,
			Class proxyClass) {

	}

	public void proxy(ServletContext servletContext, Application application,
			KeyValuesProvider keyValuesProvider, Translator appTranslator) {

		WriteablePropertyMap propertyMap = new WriteablePropertyMap();

		Class entityType = null;
		Class<Serializable> entityIdType = null;

		WebCrudControllerBeanConfig config = new WebCrudControllerBeanConfig(
				propertyMap, entityType, entityIdType);

		Translator translator = new DefaultTranslator(config.messageBundle(),
				keyValuesProvider, appTranslator.getCurrentLocale(),
				appTranslator);

		EntityManagerFactory managerFactory = null;

		Service service = new AbstractJpaRepository(managerFactory, entityType);

		service.setListener(this.getActionListener(entityType));

		DataModelFactory modelFactory = new DefaultDataModelFactory();

		WebCrudController controller = new DefaultWebCrudController(
				application, translator, modelFactory, service, config);

		controller.addListener(this.getControllerListener(entityType));
	}

	private <T> ControllerListener<T> getControllerListener(Class<T> entityType) {

		ControllerListener<T> listener = null;

		return listener;
	}

	private <T> Service.ActionListener<T> getActionListener(Class<T> entityType) {

		Service.ActionListener<T> listener = null;

		return listener;
	}

	private List<Class> findDynamicClasses(
			Class<? extends Annotation> annotation) {

		List<Class> classes = new ArrayList<Class>();

		return classes;
	}
}