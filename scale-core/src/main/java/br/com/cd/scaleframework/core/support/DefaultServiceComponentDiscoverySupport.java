package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentObserver;
import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.ServiceComponentDiscoverySupport;
import br.com.cd.scaleframework.core.dynamic.ServiceBean;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.util.StringUtils;

public class DefaultServiceComponentDiscoverySupport implements
		ServiceComponentDiscoverySupport {

	@Override
	public Class<ServiceBean> getAnnotationType() {
		return ServiceBean.class;
	}

	@Override
	public Class<?> getTargetEntity(ServiceBean annotation) {
		return annotation.targetEntity();
	}

	@Override
	public String getSessionFactoryQualifier(ServiceBean annotation) {
		return annotation.sessionFactoryQualifier();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public ServiceComponent createComponent(String name,
			String targetEntityClassName, ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		name = StringUtils.addBeginSlash(name.replace("Bean", ""));

		DefaultServiceComponentConfig config = new DefaultServiceComponentConfig(
				name, targetEntityClassName);

		final ServiceComponent component = this.createComponent(config);

		return component;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public ServiceComponent createComponent(ServiceBean annotation,
			ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		String name = StringUtils.addBeginSlash(annotation.name().replace(
				"Bean", ""));

		final DefaultServiceComponentConfig config = new DefaultServiceComponentConfig(
				name, annotation.targetEntity());

		final ServiceComponent component = this.createComponent(config);

		return component;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ServiceComponent createComponent(
			final DefaultServiceComponentConfig config) {

		final ServiceComponent component = new DefaultServiceComponent(config);

		component.addObserver(new ComponentObserver<RepositoryComponent>() {

			@Override
			public void onDiscoverTarget(RepositoryComponent component) {
				component.setTargetRepository(component);
			}

			@Override
			public Class<RepositoryComponent> getComponentType() {
				return RepositoryComponent.class;
			}

			@Override
			public Class<?> getTargetEntity() {
				return config.getTargetEntity();
			}
		});

		component.addObserver(new ComponentObserver<ServiceComponent>() {

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

		return component;
	}

	@Override
	public void onRegistryComponent(
			@SuppressWarnings("rawtypes") ServiceComponent component,
			ComponentBeanFactory beanFactory) {
		// TODO Auto-generated method stub

	}

}
