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
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;

import br.com.cd.mvo.ioc.AbstractContainer;
import br.com.cd.mvo.ioc.ComponentFactory;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.ContainerRegistry;
import br.com.cd.mvo.ioc.NoSuchBeanDefinitionException;

public class SpringContainer extends AbstractContainer {

	ConfigurableApplicationContext applicationContext;

	private Map<String, Object> singletonList = new TreeMap<>();
	private Map<String, String> aliasList = new TreeMap<>();
	private Collection<BeanDefinitionHolder> beanDefinitionHolderList = new TreeSet<>();
	private Map<String, BeanDefinition> beanDefinitionList = new TreeMap<>();
	private SpringContainerRegistry registry = new SpringContainerRegistry(this);

	public SpringContainer(ConfigurableApplicationContext parentApplicationContext, ContainerConfig<?> applicationConfig) {
		super(applicationConfig);
		this.applicationContext = parentApplicationContext;
	}

	private static final class BeanRegistrationProcessor implements BeanFactoryPostProcessor {
		private final ContainerRegistry<SpringContainer> registry;

		public BeanRegistrationProcessor(ContainerRegistry<SpringContainer> registry) {
			this.registry = registry;
		}

		public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {

			try {
				registry.register();
			} catch (ConfigurationException e) {
				throw new ApplicationContextException(e.getMessage(), e);
			}
		}

	}

	@Override
	public void start() throws ConfigurationException {

		applicationContext.addBeanFactoryPostProcessor(new BeanRegistrationProcessor(this.registry));
		applicationContext.refresh();
		applicationContext.start();

		// registry.register();
	}

	@Override
	public void stop() {
		applicationContext.stop();
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
	public <T> T getBean(String beanName, Class<T> beanType) throws NoSuchBeanDefinitionException {

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
	public <T> Collection<T> getBeansOfType(Class<T> beanType) throws NoSuchBeanDefinitionException {
		try {
			return applicationContext.getBeansOfType(beanType).values();
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException {
		try {
			return applicationContext.getBean(beanType);
		} catch (BeansException e) {
			throw new NoSuchBeanDefinitionException(e);
		}
	}

	@Override
	public void registerSingleton(String beanName, Object singletonObject) {

		if (applicationContext.isActive()) {
			applicationContext.getBeanFactory().registerSingleton(beanName, singletonObject);
		} else {
			this.singletonList.put(beanName, singletonObject);
		}
	}

	@Override
	public void registerAlias(String beanName, String alias) {

		if (applicationContext.isActive()) {
			applicationContext.getBeanFactory().registerAlias(beanName, alias);
		} else {
			this.aliasList.put(beanName, alias);
		}
	}

	@Override
	public <T> Object newSingletonFactoryBean(ComponentFactory<T> cf) {
		return new ComponentFactoryBean<T>(cf);
	}

	@Override
	protected void doRegisterBean(String beanName, String scope, Class<?> beanType) {

		AnnotatedGenericBeanDefinition definition = new AnnotatedGenericBeanDefinition(beanType);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
		definition.setPrimary(true);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

		ScopeMetadata scopeMetadata = new ScopeMetadata();
		scopeMetadata.setScopeName(scope);

		BeanDefinitionHolder definitionHolder = applyScopeOn(new BeanDefinitionHolder(definition, beanName), scopeMetadata);
		doRegisterBean(definitionHolder);
	}

	private void doRegisterBean(BeanDefinitionHolder definitionHolder) {

		if (applicationContext.isActive()) {
			BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, (BeanDefinitionRegistry) applicationContext.getBeanFactory());
		} else {
			beanDefinitionHolderList.add(definitionHolder);
		}
	}

	private BeanDefinitionHolder applyScopeOn(BeanDefinitionHolder definition, ScopeMetadata scopeMetadata) {
		String scope = scopeMetadata.getScopeName();
		ScopedProxyMode proxyMode = scopeMetadata.getScopedProxyMode();
		definition.getBeanDefinition().setScope(scope);
		if (BeanDefinition.SCOPE_SINGLETON.equals(scope) || BeanDefinition.SCOPE_PROTOTYPE.equals(scope) || proxyMode.equals(ScopedProxyMode.NO)) {
			return definition;
		} else {
			boolean proxyTargetClass = proxyMode.equals(ScopedProxyMode.TARGET_CLASS);
			return ScopedProxyUtils.createScopedProxy(definition, (BeanDefinitionRegistry) applicationContext.getBeanFactory(), proxyTargetClass);
		}
	}

	@Override
	public void registerBean(String beanName, Class<?> beanType) {
		RootBeanDefinition definition = new RootBeanDefinition(beanType);
		definition.setRole(BeanDefinition.ROLE_APPLICATION);
		// definition.getPropertyValues().addPropertyValue("order",
		// Ordered.LOWEST_PRECEDENCE);

		if (applicationContext.isActive()) {
			((BeanDefinitionRegistry) applicationContext.getBeanFactory()).registerBeanDefinition(beanName, definition);
		} else {
			beanDefinitionList.put(beanName, definition);
		}
	}

	@Override
	protected void doConfigure() {

		// nothing?
	}

	@Override
	protected void doDeepRegister() {

		for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionList.entrySet()) {
			((BeanDefinitionRegistry) applicationContext.getBeanFactory()).registerBeanDefinition(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, Object> entry : this.singletonList.entrySet()) {
			applicationContext.getBeanFactory().registerSingleton(entry.getKey(), entry.getValue());
		}
		for (BeanDefinitionHolder beanDefinitionHolder : this.beanDefinitionHolderList) {
			BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, (BeanDefinitionRegistry) applicationContext.getBeanFactory());
		}
		for (Map.Entry<String, String> entry : this.aliasList.entrySet()) {
			applicationContext.getBeanFactory().registerAlias(entry.getKey(), entry.getValue());
		}
	}
}