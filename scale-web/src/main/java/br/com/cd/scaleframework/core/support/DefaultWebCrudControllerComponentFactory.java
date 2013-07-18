package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.core.WebCrudControllerComponent;
import br.com.cd.scaleframework.core.WebCrudControllerComponentFactory;
import br.com.cd.scaleframework.core.dynamic.WebCrudControllerBean;
import br.com.cd.scaleframework.core.proxy.WebCrudControllerProxy;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.web.beans.WebControllerBeanProperty;

public class DefaultWebCrudControllerComponentFactory
		extends
		AbstractWebControllerComponentFactory<WebCrudControllerComponent, WebCrudControllerProxy, WebCrudControllerBean>
		implements WebCrudControllerComponentFactory {

	public DefaultWebCrudControllerComponentFactory() {
		super(WebCrudControllerProxy.class);
	}

	public DefaultWebCrudControllerComponentFactory(
			Map<String, WebCrudControllerComponent> components) {
		super(WebCrudControllerProxy.class, components);
	}

	@Override
	protected Map<Class<? extends Annotation>, Map<String, Object>> createAnnotationsAttributes(
			WebCrudControllerComponent component) {

		Map<Class<? extends Annotation>, Map<String, Object>> annotations = new HashMap<Class<? extends Annotation>, Map<String, Object>>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WebControllerBeanProperty.NAME.getProperty(), ParserUtils
				.parseString(component.getComponentConfig().getName()));
		params.put(WebControllerBeanProperty.PATH.getProperty(), ParserUtils
				.parseString(((WebControllerComponentConfig) component
						.getComponentConfig()).getPath()));
		params.put(WebControllerBeanProperty.MESSAGE_BUNDLE.getProperty(),
				ParserUtils
						.parseString(((WebControllerComponentConfig) component
								.getComponentConfig()).getMessageBundle()));
		params.put(WebControllerBeanProperty.INITIAL_PAGE_SIZE.getProperty(),
				ParserUtils.parseInt(((WebControllerComponentConfig) component
						.getComponentConfig()).getInitialPageSize()));

		annotations.put(WebCrudControllerBean.class, params);

		annotations.put(Component.class, new HashMap<String, Object>());

		params = new HashMap<String, Object>();
		params.put("value", component.getComponentConfig().getScope());

		annotations.put(Scope.class, params);

		return annotations;
	}

}