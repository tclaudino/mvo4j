package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.Map;

import br.com.cd.scaleframework.core.ControllerComponent;
import br.com.cd.scaleframework.core.ControllerComponentConfig;
import br.com.cd.scaleframework.core.ServiceComponent;

public abstract class AbstractControllerComponent<C extends ControllerComponentConfig<?>>
		extends AbstractComponent<C> implements ControllerComponent<C> {

	@SuppressWarnings("rawtypes")
	private ServiceComponent targetService;

	private ControllerComponent<C> targetController;

	public AbstractControllerComponent(C config,
			Map<Class<Annotation>, Map<String, Object>> annotationsAttributes,
			Class<?>[] autowireCandidates) {
		super(config, annotationsAttributes, autowireCandidates);
	}

	public AbstractControllerComponent(C config, Class<?>[] autowireCandidates) {
		super(config, autowireCandidates);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public ServiceComponent getTargetService() {
		return targetService;
	}

	@Override
	public void setTargetService(
			@SuppressWarnings("rawtypes") ServiceComponent targetService) {
		this.targetService = targetService;
	}

	@Override
	public ControllerComponent<C> getTargetController() {
		return targetController;
	}

	@Override
	public void setTargetController(ControllerComponent<C> targetController) {
		this.targetController = targetController;
	}

}
