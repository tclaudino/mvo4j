package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.cd.scaleframework.core.ComponentConfig;
import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.ComponentObserver;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoveryVisitor;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

public abstract class AbstractComponent<C extends ComponentConfig> implements
		ComponentObject<C> {

	private final C config;
	private final Map<Class<Annotation>, Map<String, Object>> annotationsAttributes;
	private final List<Class<?>> autowireCandidates;

	private Class<?> proxy;

	protected abstract Class<?> createProxy(
			ComponentBeanFactory applicationContext);

	public AbstractComponent(C config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>... autowireCandidates) {
		this.config = config;
		this.annotationsAttributes = annotationsAttributes;
		this.autowireCandidates = Arrays.asList(autowireCandidates);
	}

	public AbstractComponent(C config, Class<?>... autowireCandidates) {
		this(config,
				new LinkedHashMap<Class<Annotation>, Map<String, Object>>(),
				autowireCandidates);
	}

	@Override
	public Class<?> getProxy(ComponentBeanFactory applicationContext) {
		if (proxy == null) {
			proxy = createProxy(applicationContext);
		}
		return proxy;
	}

	@Override
	public C getComponentConfig() {
		return config;
	}

	@Override
	public Map<Class<Annotation>, Map<String, Object>> getAnnotationsAttributes() {
		return annotationsAttributes;
	}

	@Override
	public void addAnnotation(Class<Annotation> annotation,
			Map<String, Object> attributes) {
		this.annotationsAttributes.put(annotation, attributes);
	}

	@Override
	public List<Class<?>> getAutowireCandidates() {
		return autowireCandidates;
	}

	@Override
	public void addAutowireCandidate(Class<?> autowireCandidate) {
		this.autowireCandidates.add(autowireCandidate);
	}

	@Override
	public void accept(ComponentDiscoveryVisitor componentRegistry) {
		componentRegistry.visit(this);
	}

	private List<ComponentObserver<?>> observers = new LinkedList<ComponentObserver<?>>();

	@Override
	public void addObserver(ComponentObserver<?> observer) {
		observers.add(observer);
	}

	@Override
	public List<ComponentObserver<?>> getObservers() {
		return observers;
	}

}
