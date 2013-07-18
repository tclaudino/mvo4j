package br.com.cd.scaleframework.core;

public interface ControllerComponent<C extends ControllerComponentConfig<?>>
		extends ComponentObject<C> {

	void setTargetService(
			@SuppressWarnings("rawtypes") ServiceComponent componentObject);

	@SuppressWarnings("rawtypes")
	ServiceComponent getTargetService();

	ControllerComponent<C> getTargetController();

	void setTargetController(ControllerComponent<C> targetController);

}