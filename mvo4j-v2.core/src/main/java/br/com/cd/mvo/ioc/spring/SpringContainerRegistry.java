package br.com.cd.mvo.ioc.spring;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.Ordered;

import br.com.cd.mvo.ioc.AbstractContainerRegistry;

public class SpringContainerRegistry extends AbstractContainerRegistry<SpringContainer> {

	public SpringContainerRegistry(SpringContainer container) {
		super(container);
	}

	/*
	 * @Override public void
	 * postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws
	 * BeansException {
	 * 
	 * try { this.register(); this.deepRegister(); } catch
	 * (ConfigurationException e) { throw new BeansException(e.getMessage(), e)
	 * { }; } }
	 */

	@Override
	protected void configure() {

		AnnotationConfigUtils.registerAnnotationConfigProcessors((BeanDefinitionRegistry) container.applicationContext.getBeanFactory());
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary((BeanDefinitionRegistry) container.applicationContext
				.getBeanFactory());

		// container.applicationContext.addBeanFactoryPostProcessor(this);
		this.registerCustomComponents();
		this.registerCustomInjectionProcessor();

	}

	private void registerCustomComponents() {

		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) container.applicationContext.getBeanFactory();
		beanFactory
				.setAutowireCandidateResolver(new GenericAutowireCandidateResolver(container, beanFactory.getAutowireCandidateResolver()));
		beanFactory.setInstantiationStrategy(new BeanObjectInstantiationStrategy(container));
	}

	private void registerCustomInjectionProcessor() {
		RootBeanDefinition definition = new RootBeanDefinition(InjectionBeanPostProcessor.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		definition.getPropertyValues().addPropertyValue("order", Ordered.LOWEST_PRECEDENCE);

		((BeanDefinitionRegistry) container.applicationContext.getBeanFactory()).registerBeanDefinition(
				AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, definition);

		container.applicationContext.getBeanFactory().addBeanPostProcessor(
				(BeanPostProcessor) container.getBean(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
	}
}
