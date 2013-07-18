package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanCreationException;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.ServiceComponent;
import br.com.cd.scaleframework.core.proxy.support.DefaultRepositoryProxy;
import br.com.cd.scaleframework.core.proxy.support.DefaultServiceProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.proxy.ProxyFactory;

public class DefaultServiceComponent extends
		AbstractComponent<DefaultServiceComponentConfig> implements
		ServiceComponent<DefaultServiceComponentConfig> {

	@SuppressWarnings("rawtypes")
	private RepositoryComponent targetRepository;

	private ServiceComponent<DefaultServiceComponentConfig> targetService;

	public DefaultServiceComponent(DefaultServiceComponentConfig config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>... autowireCandidates) {
		super(config,
				new LinkedHashMap<Class<Annotation>, Map<String, Object>>(),
				autowireCandidates);
	}

	public DefaultServiceComponent(DefaultServiceComponentConfig config,
			Class<?>... autowireCandidates) {
		this(config,
				new LinkedHashMap<Class<Annotation>, Map<String, Object>>(),
				autowireCandidates);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public RepositoryComponent getTargetRepository() {
		return this.targetRepository;
	}

	@Override
	public void setTargetRepository(
			@SuppressWarnings("rawtypes") RepositoryComponent targetRepository) {
		this.targetRepository = targetRepository;
	}

	@Override
	public ServiceComponent<DefaultServiceComponentConfig> getTargetService() {
		return this.targetService;
	}

	@Override
	public void setTargetService(
			ServiceComponent<DefaultServiceComponentConfig> targetService) {
		this.targetService = targetService;
	}

	@Override
	protected Class<?> createProxy(ComponentBeanFactory applicationContext) {

		String beanName = this.getComponentConfig().getBeanName();

		System.out.println("\n" + this.getClass().getName()
				+ " | registering bean, beanName: " + beanName
				+ ", proxyBeanName: " + this.getComponentConfig().getName());

		if (this.getTargetRepository() != null) {
			try {

				@SuppressWarnings("rawtypes")
				ProxyFactory<DefaultRepositoryProxy> factory = ProxyFactory
						.createProxyFactory(DefaultRepositoryProxy.class);

				// factory.setThisClass(this.getTargetRepository()
				// .getComponentConfig().getTargetComponent());

				applicationContext.registerBean(this.getTargetRepository()
						.getComponentConfig().getName(),
						factory.getProxyClass());

			} catch (Exception e) {
				throw new BeanCreationException(
						"Can'not create proxy for target '"
								+ this.getTargetRepository()
										.getComponentConfig(), e);
			}
		}

		ProxyFactory<DefaultServiceProxy> factory = ProxyFactory
				.createProxyFactory(DefaultServiceProxy.class);

		if (this.getTargetService() != null) {

			// TODO:
			// factory.setThisClass(this.getTargetService().getComponentConfig()
			// .getTargetComponent());
		}

		try {

			Class<DefaultServiceProxy> annotatedProxyType = factory
					.getProxyClass();

			System.out.println("\n\nstoring proxyfied  bean: "
					+ annotatedProxyType + ", in spring context with name: "
					+ this.getComponentConfig().getName() + "\n\n\n");

			applicationContext.registerBean(
					this.getComponentConfig().getName(), annotatedProxyType);

			if (this.getTargetService() != null) {

				applicationContext.registerBean(this.getTargetService()
						.getComponentConfig().getName(), annotatedProxyType);
			}

			return annotatedProxyType;

		} catch (Exception e) {
			throw new BeanCreationException("Can'not create proxy for target '"
					+ DefaultServiceProxy.class, e);
		}
	}

}
