package br.com.cd.mvo.ioc.spring;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.AbstractContainer;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.ContainerConfig;

public class SpringContainer extends AbstractContainer {

	ConfigurableApplicationContext applicationContext;

	private Map<String, Object> singletonObjects = new TreeMap<>();
	private Collection<BeanDefinitionHolder> beanDefinitionHolderList = new TreeSet<>();
	private Map<String, BeanDefinition> beanDefinitionList = new TreeMap<>();
	private SpringContainerRegistry registry = new SpringContainerRegistry(this);

	public SpringContainer(
			ConfigurableApplicationContext parentApplicationContext,
			ContainerConfig<?> applicationConfig) {
		super(applicationConfig);
		this.applicationContext = parentApplicationContext;
	}

	@Override
	public void start() throws ConfigurationException {

		registry.register();

		// ((DefaultListableBeanFactory)parentApplicationContext.getBeanFactory()).setInstantiationStrategy(instantiationStrategy);
		// applicationContext.refresh();
		// applicationContext.start();
	}

	@Override
	public void stop() {
		// applicationContext.stop();
	}

	@Override
	public boolean containsBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	@Override
	public Object getBean(String beanName) throws NoSuchBeanDefinitionException {

		try {
			return applicationContext.getBean(beanName);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> beanType)
			throws NoSuchBeanDefinitionException {

		Object bean;
		try {
			bean = applicationContext.getBean(beanName);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}

		try {
			return (T) bean;
		} catch (ClassCastException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public <T> Collection<T> getBeansOfType(Class<T> beanType)
			throws NoSuchBeanDefinitionException {
		try {
			return applicationContext.getBeansOfType(beanType).values();
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public <T> T getBean(Class<T> beanType)
			throws NoSuchBeanDefinitionException {
		try {
			return applicationContext.getBean(beanType);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) {

		if (applicationContext.isActive()) {
			applicationContext.getBeanFactory().registerSingleton(beanName,
					singletonObject);
		} else {
			this.singletonObjects.put(beanName, singletonObject);
		}
	}

	@Override
	public <T> Object getSingletonBeanFactory(ComponentFactory<T> cf) {
		return new ComponentFactoryBean<T>(cf);
	}

	@Override
	protected void doRegister(String beanName, String scope, Class<?> type) {

		AnnotatedGenericBeanDefinition definition = new AnnotatedGenericBeanDefinition(
				type);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
		definition.setPrimary(true);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

		ScopeMetadata scopeMetadata = new ScopeMetadata();
		scopeMetadata.setScopeName(scope);

		BeanDefinitionHolder definitionHolder = applyScopeOn(
				new BeanDefinitionHolder(definition, beanName), scopeMetadata);
		doRegister(definitionHolder);
	}

	private void doRegister(BeanDefinitionHolder definitionHolder) {

		if (applicationContext.isActive()) {
			BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder,
					(BeanDefinitionRegistry) applicationContext
							.getBeanFactory());
		} else {
			beanDefinitionHolderList.add(definitionHolder);
		}

		/*
		 * ConstructorArgumentValues constructorArgumentValues = new
		 * ConstructorArgumentValues();
		 * 
		 * ConstructorArgumentValues.ValueHolder holder = new
		 * DymanicValueHolder();
		 * 
		 * constructorArgumentValues.addGenericArgumentValue(holder);
		 * 
		 * definition.setConstructorArgumentValues(constructorArgumentValues);
		 */
	}

	private BeanDefinitionHolder applyScopeOn(BeanDefinitionHolder definition,
			ScopeMetadata scopeMetadata) {
		String scope = scopeMetadata.getScopeName();
		ScopedProxyMode proxyMode = scopeMetadata.getScopedProxyMode();
		definition.getBeanDefinition().setScope(scope);
		if (BeanDefinition.SCOPE_SINGLETON.equals(scope)
				|| BeanDefinition.SCOPE_PROTOTYPE.equals(scope)
				|| proxyMode.equals(ScopedProxyMode.NO)) {
			return definition;
		} else {
			boolean proxyTargetClass = proxyMode
					.equals(ScopedProxyMode.TARGET_CLASS);
			return ScopedProxyUtils.createScopedProxy(definition,
					(BeanDefinitionRegistry) applicationContext
							.getBeanFactory(), proxyTargetClass);
		}
	}

	@Override
	public void registerBean(Class<?> beanType, String beanName) {
		RootBeanDefinition definition = new RootBeanDefinition(beanType);
		definition.setRole(BeanDefinition.ROLE_APPLICATION);
		// definition.getPropertyValues().addPropertyValue("order",
		// Ordered.LOWEST_PRECEDENCE);

		if (applicationContext.isActive()) {
			((BeanDefinitionRegistry) applicationContext.getBeanFactory())
					.registerBeanDefinition(beanName, definition);
		} else {
			beanDefinitionList.put(beanName, definition);
		}
	}

	@Override
	public void deepRegister() {

		for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionList
				.entrySet()) {
			((BeanDefinitionRegistry) applicationContext.getBeanFactory())
					.registerBeanDefinition(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, Object> entry : this.singletonObjects.entrySet()) {
			applicationContext.getBeanFactory().registerSingleton(
					entry.getKey(), entry.getValue());
		}
		for (BeanDefinitionHolder beanDefinitionHolder : this.beanDefinitionHolderList) {
			BeanDefinitionReaderUtils.registerBeanDefinition(
					beanDefinitionHolder,
					(BeanDefinitionRegistry) applicationContext
							.getBeanFactory());
		}
	}
}