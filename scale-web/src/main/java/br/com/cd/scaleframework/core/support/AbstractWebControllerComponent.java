package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.Map;

import br.com.cd.scaleframework.core.WebControllerComponent;

public abstract class AbstractWebControllerComponent<C extends WebControllerComponentConfig>
		extends AbstractControllerComponent<C> implements
		WebControllerComponent<C> {

	public AbstractWebControllerComponent(C config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>[] autowireCandidates) {
		super(config, annotationsAttributes, autowireCandidates);
	}

	public AbstractWebControllerComponent(C config,
			Class<?>[] autowireCandidates) {
		super(config, autowireCandidates);
	}

}
