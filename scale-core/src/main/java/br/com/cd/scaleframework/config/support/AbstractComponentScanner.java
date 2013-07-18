package br.com.cd.scaleframework.config.support;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import br.com.cd.scaleframework.config.ComponentScanner;
import br.com.cd.scaleframework.context.ApplicationContext;

public abstract class AbstractComponentScanner<C extends ApplicationContext>
		implements ComponentScanner<C> {

	public static final String PACKAGES_TO_SCAN = "br.com.cd.scaleframework";

	private Boolean isInitialized = false;

	protected abstract void onStartScan(C applicationContext);

	@Override
	public void init(C applicationContext) {

		if (this.isInitialized)
			return;
		synchronized (this) {
			if (this.isInitialized)
				return;

			this.isInitialized = true;

			AnnotationConfigUtils
					.registerAnnotationConfigProcessors(applicationContext
							.getBeanDefinitionRegistry());
			AopConfigUtils
					.registerAspectJAnnotationAutoProxyCreatorIfNecessary(applicationContext
							.getBeanDefinitionRegistry());

			onStartScan(applicationContext);

			doScan(applicationContext.getBeanDefinitionRegistry());
		}

	}

	public void doScan(final BeanDefinitionRegistry beanRegistry) {

		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(
				beanRegistry);

		scanner.setIncludeAnnotationConfig(true);
		System.out.println("\n\nApplicationContextAware scaning packages: "
				+ PACKAGES_TO_SCAN);

		scanner.scan(PACKAGES_TO_SCAN);
	}

	public void registerPostProcessor(
			final BeanDefinitionRegistry beanRegistry, Class<?> beanClass) {

		BeanDefinition beanDefinition = BeanDefinitionBuilder
				.rootBeanDefinition(beanClass).getBeanDefinition();
		String beanName = new DefaultBeanNameGenerator().generateBeanName(
				beanDefinition, beanRegistry);

		if (beanRegistry.containsBeanDefinition(beanName))
			return;

		BeanDefinitionReaderUtils.registerBeanDefinition(
				new BeanDefinitionHolder(beanDefinition, beanName),
				beanRegistry);

	}
}
