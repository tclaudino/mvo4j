package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.context.ApplicationContext;
import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentObserver;
import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.WebControllerComponent;
import br.com.cd.scaleframework.core.WebControllerComponentDiscoverySupport;
import br.com.cd.scaleframework.core.WebControllerComponentFactory;
import br.com.cd.scaleframework.core.dynamic.WebControllerBean;
import br.com.cd.scaleframework.core.proxy.WebControllerProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.context.WebApplicationContext;

public class DefaultWebControllerComponentDiscoverySupport
		extends
		AbstractControllerComponentDiscoverySupport<WebControllerComponent, WebControllerProxy, WebControllerBean, WebControllerComponentFactory>
		implements WebControllerComponentDiscoverySupport {

	@Override
	public Class<WebControllerBean> getAnnotationType() {
		return WebControllerBean.class;
	}

	@Override
	public Class<?> getTargetEntity(WebControllerBean annotation) {
		return annotation.targetEntity();
	}

	@Override
	public String getSessionFactoryQualifier(WebControllerBean annotation) {
		return annotation.sessionFactoryQualifier();
	}

	@Override
	public WebControllerComponent createComponent(WebControllerBean annotation,
			ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		// TODO:
		String path = StringUtils
				.removeEndSlash(((WebApplicationContext) beanFactory)
						.getServletContext().getContextPath());

		path += StringUtils
				.addBeginSlash(annotation.name().replace("Bean", ""));

		WebControllerComponentConfig config = new WebControllerComponentConfig(
				annotation.name(), path, "", ParserUtils.parseInt(
						annotation.initialPageSize(),
						factorySupport.getInitialPageSize()));

		config.setLazyProperties(annotation.lazyProperties());
		config.setMakeList(annotation.makeList());
		config.setScope(annotation.scope());
		config.setTargetEntity(annotation.targetEntity());
		config.setEntityIdType(annotation.entityIdType());

		// TODO:
		WebControllerComponent component = this.createComponent(config,
				(ApplicationContext) factorySupport);

		addRelationMaps(component, annotation.relationMaps());

		return component;
	}

	@Override
	public WebControllerComponent createComponent(String name,
			String targetEntityClassName, ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		// TODO:
		String path = StringUtils
				.removeEndSlash(((WebApplicationContext) beanFactory)
						.getServletContext().getContextPath());

		path += StringUtils.addBeginSlash(name.replace("Bean", ""));

		WebControllerComponentConfig config = new WebControllerComponentConfig(
				name, path, targetEntityClassName,
				factorySupport.getInitialPageSize());

		// TODO:
		WebControllerComponent component = this.createComponent(config,
				(ApplicationContext) factorySupport);

		return component;
	}

	@SuppressWarnings("rawtypes")
	protected WebControllerComponent createComponent(
			final WebControllerComponentConfig config,
			ApplicationContext applicationContext) {

		final WebControllerComponent component = new DefaultWebControllerComponent<WebControllerComponentConfig, WebControllerProxy>(
				WebControllerProxy.class, config);

		this.setListeners(component, applicationContext);

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
	public void onRegistryComponent(WebControllerComponent component,
			ComponentBeanFactory beanFactory) {

		String path = StringUtils
				.removeEndSlash(((WebApplicationContext) beanFactory)
						.getServletContext().getContextPath());

		path += StringUtils
				.addBeginSlash(((WebControllerComponentConfig) component
						.getComponentConfig()).getPath());
		((WebControllerComponentConfig) component.getComponentConfig())
				.setPath(path);

		// beanProxy.getComponentConfig().setSessionFactoryQualifier(
		// ParserUtils.parseString(
		// this.getSessionFactoryQualifier(),
		// RepositoryComponentConfig.SESSION_FACTORY_QUALIFIER));
	}

}