package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanCreationException;

import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.proxy.support.DefaultRepositoryProxy;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;
import br.com.cd.scaleframework.orm.SessionFactoryProvider;
import br.com.cd.scaleframework.proxy.ProxyFactory;

@SuppressWarnings("rawtypes")
public class DefaultRepositoryComponent<F extends SessionFactoryProvider>
		extends AbstractComponent<DefaultRepositoryComponentConfig> implements
		RepositoryComponent<DefaultRepositoryComponentConfig, F> {

	private final F provider;

	private RepositoryComponent<DefaultRepositoryComponentConfig, F> targetRepository;

	public DefaultRepositoryComponent(F provider,
			DefaultRepositoryComponentConfig config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>... autowireCandidates) {
		super(config,
				new LinkedHashMap<Class<Annotation>, Map<String, Object>>(),
				autowireCandidates);
		this.provider = provider;
		this.provider.setComponent(this);
	}

	public DefaultRepositoryComponent(F provider,
			DefaultRepositoryComponentConfig config,
			Class<?>... autowireCandidates) {
		this(provider, config,
				new LinkedHashMap<Class<Annotation>, Map<String, Object>>(),
				autowireCandidates);
	}

	public static <T extends SessionFactoryProvider> DefaultRepositoryComponent<T> createComponentProxy(
			T provider, DefaultRepositoryComponentConfig config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>... autowireCandidates) {

		return new DefaultRepositoryComponent<T>(provider, config,
				annotationsAttributes, autowireCandidates);
	}

	public static <T extends SessionFactoryProvider> DefaultRepositoryComponent<T> createComponentProxy(
			T provider, DefaultRepositoryComponentConfig config,
			Class<?>... autowireCandidates) {

		return new DefaultRepositoryComponent<T>(provider, config,
				autowireCandidates);
	}

	@Override
	public F getComponentProvider() {
		return provider;
	}

	@Override
	public RepositoryComponent<DefaultRepositoryComponentConfig, F> getTargetRepository() {
		return this.targetRepository;
	}

	@Override
	public void setTargetRepository(
			RepositoryComponent<DefaultRepositoryComponentConfig, F> targetRepository) {
		this.targetRepository = targetRepository;
	}

	@Override
	protected Class<?> createProxy(ComponentBeanFactory applicationContext) {
		String beanName = this.getComponentConfig().getBeanName();

		System.out.println("\n" + this.getClass().getName()
				+ " | registering bean, beanName: " + beanName
				+ ", proxyBeanName: " + this.getComponentConfig().getName());

		ProxyFactory<DefaultRepositoryProxy> factory = ProxyFactory
				.createProxyFactory(DefaultRepositoryProxy.class);

		if (this.getTargetRepository() != null) {

			// TODO: factory.setThisClass(this.getTargetRepository()
			// .getComponentConfig().getTargetComponent());
		}

		try {

			Class<DefaultRepositoryProxy> annotatedProxyType = factory
					.getProxyClass();

			System.out.println("\n\nstoring proxyfied  bean: "
					+ annotatedProxyType + ", in spring context with name: "
					+ this.getComponentConfig().getName() + "\n\n\n");

			applicationContext.registerBean(
					this.getComponentConfig().getName(), annotatedProxyType);

			if (this.getTargetRepository() != null) {

				applicationContext.registerBean(this.getTargetRepository()
						.getComponentConfig().getName(), annotatedProxyType);
			}

			return annotatedProxyType;

		} catch (Exception e) {
			throw new BeanCreationException("Can'not create proxy for target '"
					+ DefaultRepositoryProxy.class, e);
		}
	}

}
