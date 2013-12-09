package br.com.cd.mvo.web.mvc;

import br.com.cd.mvo.ConfigParamKeys;
import br.com.cd.mvo.ioc.Container;

public abstract class DynamicController {

	public static final String CURRENT_VIEW_NAME = DynamicController.class.getName() + ".currentViewName";

	protected Container container;
	protected String VIEW_PREFIX;
	protected String VIEW_SUFFIX;
	protected String VIEW_CURRENT_BEAN_VARIABLE_NAME;

	protected final String VIEW_TRANSLATOR_VARIABLE_NAME;
	protected final String VIEW_APPLICATION_VARIABLE_NAME;

	public static final String PATH_VARIABLE_ENTITY = "entity";
	public static final String PATH_VARIABLE_VIEW_NAME = "viewName";

	public DynamicController(Container container) {
		this.container = container;

		VIEW_TRANSLATOR_VARIABLE_NAME = container.getContainerConfig().getInitParameter(ConfigParamKeys.VIEW_TRANSLATOR_VARIABLE_NAME,
				ConfigParamKeys.DefaultValues.VIEW_TRANSLATOR_VARIABLE_NAME);
		VIEW_APPLICATION_VARIABLE_NAME = container.getContainerConfig().getInitParameter(ConfigParamKeys.VIEW_APPLICATION_VARIABLE_NAME,
				ConfigParamKeys.DefaultValues.VIEW_APPLICATION_VARIABLE_NAME);

		VIEW_PREFIX = container.getContainerConfig().getInitParameter(ConfigParamKeys.VIEW_PREFIX, ConfigParamKeys.DefaultValues.VIEW_PREFIX);
		VIEW_SUFFIX = container.getContainerConfig().getInitParameter(ConfigParamKeys.VIEW_SUFFIX, ConfigParamKeys.DefaultValues.VIEW_SUFFIX);
		VIEW_CURRENT_BEAN_VARIABLE_NAME = container.getContainerConfig().getInitParameter(ConfigParamKeys.VIEW_CURRENT_BEAN_VARIABLE_NAME,
				ConfigParamKeys.DefaultValues.VIEW_CURRENT_BEAN_VARIABLE_NAME);
	}

}
