package br.com.cd.mvo.web.mvc.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.spring.InjectionBeanPostProcessor;

public class MethodArgumentResolversPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		registerCustomInjectionProcessor(beanFactory);
		registerOn(SpringController.class, (BeanDefinitionRegistry) beanFactory);

		RequestMappingHandlerAdapter handlerAdapter = beanFactory.getBean(RequestMappingHandlerAdapter.class);

		List<HandlerMethodArgumentResolver> oldResolvers = handlerAdapter.getArgumentResolvers().getResolvers();
		List<HandlerMethodArgumentResolver> newResolvers = new ArrayList<HandlerMethodArgumentResolver>();

		Container container = beanFactory.getBean(Container.class);

		newResolvers.add(new ViewNameMethodArgumentResolver());
		newResolvers.add(new EntityRequestBodyMethodArgumentResolver(container, handlerAdapter.getMessageConverters()));
		newResolvers.add(new EntityModelAttributeMethodArgumentResolver(container));

		newResolvers.addAll(oldResolvers);

		handlerAdapter.setArgumentResolvers(newResolvers);
	}

	public void registerOn(Class<?> type, BeanDefinitionRegistry beanFactory) throws BeansException {

		BeanDefinitionReaderUtils.registerWithGeneratedName(new RootBeanDefinition(type), beanFactory);
	}

	private void registerCustomInjectionProcessor(ConfigurableListableBeanFactory beanFactory) {
		RootBeanDefinition definition = new RootBeanDefinition(InjectionBeanPostProcessor.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		definition.getPropertyValues().addPropertyValue("order", Ordered.LOWEST_PRECEDENCE);

		((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, definition);

		beanFactory.addBeanPostProcessor((BeanPostProcessor) beanFactory.getBean(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
	}

}
