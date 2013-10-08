package br.com.cd.mvo.ioc.spring;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.Ordered;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.AbstractContainerRegistry;
import br.com.cd.mvo.ioc.ComponentFactory;

public class SpringContainerRegistry extends
		AbstractContainerRegistry<SpringContainer> implements
		BeanFactoryPostProcessor {

	public SpringContainerRegistry(SpringContainer container) {
		super(container);
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {

		try {
			this.register();
			this.container.deepRegister();
		} catch (ConfigurationException e) {
			throw new BeansException(e.getMessage(), e) {
			};
		}
	}

	@Override
	protected <T> Object getSingletonBeanFactory(ComponentFactory<T> dbf) {
		return new ComponentFactoryBean<T>(dbf);
	}

	private void registerCustomInjectionProcessor() {
		RootBeanDefinition definition = new RootBeanDefinition(
				InjectionBeanPostProcessor.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		definition.getPropertyValues().addPropertyValue("order",
				Ordered.LOWEST_PRECEDENCE);
		((BeanDefinitionRegistry) container.applicationContext.getBeanFactory())
				.registerBeanDefinition(
						AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,
						definition);
	}

	@Override
	protected void registerCustomComponents() {

		// container.registerSingleton(
		// GenericAutowireCandidateResolver.class.getName(),
		// new GenericAutowireCandidateResolver(container));

		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) container.applicationContext
				.getBeanFactory();
		beanFactory
				.setAutowireCandidateResolver(new GenericAutowireCandidateResolver(
						container, beanFactory.getAutowireCandidateResolver()));

		this.registerCustomInjectionProcessor();
	}

	@Override
	protected void setup() {

		AnnotationConfigUtils
				.registerAnnotationConfigProcessors((BeanDefinitionRegistry) container.applicationContext
						.getBeanFactory());
		AopConfigUtils
				.registerAspectJAnnotationAutoProxyCreatorIfNecessary((BeanDefinitionRegistry) container.applicationContext
						.getBeanFactory());

		container.getContainerConfig().getContainerListener().setup(container);
	}
}
