package br.com.cd.scaleframework.core.support;

import br.com.cd.scaleframework.core.BeanException;
import br.com.cd.scaleframework.core.ComponentFactorySupport;
import br.com.cd.scaleframework.core.ComponentObserver;
import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.RepositoryComponentDiscoverySupport;
import br.com.cd.scaleframework.core.dynamic.RepositoryBean;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.orm.SessionFactoryProvider;
import br.com.cd.scaleframework.orm.suport.NoneSessionFactoryProvider;
import br.com.cd.scaleframework.util.StringUtils;

public class DefaultRepositoryComponentDiscoverySupport implements
		RepositoryComponentDiscoverySupport {

	@Override
	public Class<RepositoryBean> getAnnotationType() {
		return RepositoryBean.class;
	}

	@Override
	public Class<?> getTargetEntity(RepositoryBean annotation) {
		return annotation.targetEntity();
	}

	@Override
	public String getSessionFactoryQualifier(RepositoryBean annotation) {
		return annotation.sessionFactoryQualifier();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public RepositoryComponent createComponent(String name,
			String targetEntityClassName, ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		name = StringUtils.addBeginSlash(name.replace("Bean", ""));

		DefaultRepositoryComponentConfig config = new DefaultRepositoryComponentConfig(
				name, targetEntityClassName);

		SessionFactoryProvider provider;
		try {
			provider = factorySupport.getTargetProvider().newInstance();
		} catch (InstantiationException e) {
			throw new BeanException(e);
		} catch (IllegalAccessException e) {
			throw new BeanException(e);
		}

		final RepositoryComponent component = this.createComponent(config,
				provider);

		return component;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public RepositoryComponent createComponent(RepositoryBean annotation,
			ComponentBeanFactory beanFactory,
			ComponentFactorySupport factorySupport) {

		String name = StringUtils.addBeginSlash(annotation.name().replace(
				"Bean", ""));

		final DefaultRepositoryComponentConfig config = new DefaultRepositoryComponentConfig(
				name, annotation.targetEntity());

		SessionFactoryProvider provider;
		try {
			if (!NoneSessionFactoryProvider.class.equals(annotation
					.targetProvider())) {
				provider = factorySupport.getTargetProvider().newInstance();
			} else {
				provider = annotation.targetProvider().newInstance();
			}
		} catch (InstantiationException e) {
			throw new BeanException(e);
		} catch (IllegalAccessException e) {
			throw new BeanException(e);
		}

		final RepositoryComponent component = this.createComponent(config,
				provider);

		return component;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private RepositoryComponent createComponent(
			final DefaultRepositoryComponentConfig config,
			SessionFactoryProvider provider) {

		final DefaultRepositoryComponent component = DefaultRepositoryComponent
				.createComponentProxy(provider, config);

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

		return component;
	}

	@Override
	public void onRegistryComponent(
			@SuppressWarnings("rawtypes") RepositoryComponent component,
			ComponentBeanFactory beanFactory) {
		// TODO Auto-generated method stub

	}

}
