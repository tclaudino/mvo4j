package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentObserver;
import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.WebCrudControllerComponent;
import br.com.cd.scaleframework.core.WebCrudControllerComponentDiscoverySupport;
import br.com.cd.scaleframework.core.WebCrudControllerComponentFactory;
import br.com.cd.scaleframework.core.dynamic.WebCrudControllerBean;
import br.com.cd.scaleframework.core.proxy.WebCrudControllerProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.context.WebApplicationContext;

@SuppressWarnings("rawtypes")
public class DefaultWebCrudControllerComponentDiscoverySupport
		extends
		AbstractControllerComponentDiscoverySupport<WebCrudControllerComponent, WebCrudControllerProxy, WebCrudControllerBean, WebCrudControllerComponentFactory>
		implements WebCrudControllerComponentDiscoverySupport {

	@Override
	public Class<WebCrudControllerBean> getAnnotationType() {
		return WebCrudControllerBean.class;
	}

	@Override
	public Class<?> getTargetEntity(WebCrudControllerBean annotation) {
		return annotation.targetEntity();
	}

	@Override
	public String getSessionFactoryQualifier(WebCrudControllerBean annotation) {
		return annotation.sessionFactoryQualifier();
	}

	@Override
	public WebCrudControllerComponent createComponent(
			WebCrudControllerBean annotation, ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		final WebCrudControllerComponentConfig config = new WebCrudControllerComponentConfig(
				annotation.name(), annotation.path(), "", ParserUtils.parseInt(
						annotation.initialPageSize(),
						factorySupport.getInitialPageSize()));

		config.setLazyProperties(annotation.lazyProperties());
		config.setMakeList(annotation.makeList());
		config.setScope(annotation.scope());
		config.setTargetEntity(annotation.targetEntity());
		config.setEntityIdType(annotation.entityIdType());

		config.setCreateViewName(annotation.createViewName());
		config.setEditViewName(annotation.editViewName());
		config.setListViewName(annotation.listViewName());

		// TODO:
		WebCrudControllerComponent component = this.createComponent(config,
				(ApplicationContext) factorySupport);

		addRelationMaps(component, annotation.relationMaps());

		setListeners(component, (ApplicationContext) factorySupport);

		return component;
	}

	@Override
	public WebCrudControllerComponent createComponent(String name,
			String targetEntityClassName, ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		final WebCrudControllerComponentConfig config = new WebCrudControllerComponentConfig(
				name, "", targetEntityClassName,
				factorySupport.getInitialPageSize());

		// TODO:
		WebCrudControllerComponent component = this.createComponent(config,
				(ApplicationContext) factorySupport);

		setListeners(component, (ApplicationContext) factorySupport);

		return component;
	}

	private WebCrudControllerComponent createComponent(
			final WebCrudControllerComponentConfig config,
			ApplicationContext applicationContext) {

		@SuppressWarnings("unchecked")
		final WebCrudControllerComponent component = (WebCrudControllerComponent) new DefaultWebControllerComponent(
				WebCrudControllerProxy.class, config);

		component.addObserver(new ComponentObserver<ServiceComponent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDiscoverTarget(ServiceComponent component) {
				component.setTargetService(component);
			}

			@Override
			public Class<ServiceComponent> getComponentType() {
				return ServiceComponent.class;
			}

			@Override
			public Class<?> getTargetEntity() {
				return config.getTargetEntity();
			}
		});

		component
				.addObserver(new ComponentObserver<DefaultWebControllerComponent>() {

					@Override
					public void onDiscoverTarget(
							DefaultWebControllerComponent component) {
						component.setTargetController(component);
					}

					@Override
					public Class<DefaultWebControllerComponent> getComponentType() {
						return DefaultWebControllerComponent.class;
					}

					@Override
					public Class<?> getTargetEntity() {
						return config.getTargetEntity();
					}
				});

		return component;
	}

	@Override
	public void onRegistryComponent(WebCrudControllerComponent component,
			ComponentBeanFactory beanFactory) {

		String path = StringUtils
				.removeEndSlash(((WebApplicationContext) beanFactory)
						.getServletContext().getContextPath());

		path += StringUtils
				.addBeginSlash(((WebCrudControllerComponentConfig) component
						.getComponentConfig()).getPath());
		((WebCrudControllerComponentConfig) component.getComponentConfig())
				.setPath(path);

		// beanProxy.getComponentConfig().setSessionFactoryQualifier(
		// ParserUtils.parseString(
		// this.getSessionFactoryQualifier(),
		// RepositoryComponentConfig.SESSION_FACTORY_QUALIFIER));

	}

}