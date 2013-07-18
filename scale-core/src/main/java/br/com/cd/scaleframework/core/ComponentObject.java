package br.com.cd.scaleframework.core;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import br.com.cd.scaleframework.core.discovery.ComponentDiscoveryVisitor;
import br.com.cd.scaleframework.ioc.ComponentBeanFactory;

public interface ComponentObject<C extends ComponentConfig> {

	Class<?> getProxy(ComponentBeanFactory applicationContext);

	C getComponentConfig();

	Map<Class<Annotation>, Map<String, Object>> getAnnotationsAttributes();

	void addAnnotation(Class<Annotation> annotation,
			Map<String, Object> attributes);

	List<Class<?>> getAutowireCandidates();

	void addAutowireCandidate(Class<?> autowireCandidate);

	void accept(ComponentDiscoveryVisitor componentRegistry);

	void addObserver(ComponentObserver<?> observer);

	List<ComponentObserver<?>> getObservers();

}
