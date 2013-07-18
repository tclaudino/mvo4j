package br.com.cd.scaleframework.core.support;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.core.WebControllerComponent;
import br.com.cd.scaleframework.core.WebControllerComponentFactory;
import br.com.cd.scaleframework.core.dynamic.WebControllerBean;
import br.com.cd.scaleframework.core.proxy.WebControllerProxy;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.web.beans.WebControllerBeanProperty;

public class DefaultWebControllerComponentFactory
		extends
		AbstractWebControllerComponentFactory<WebControllerComponent, WebControllerProxy, WebControllerBean>
		implements WebControllerComponentFactory {

	public DefaultWebControllerComponentFactory() {
		super(WebControllerProxy.class);
	}

	public DefaultWebControllerComponentFactory(
			Map<String, WebControllerComponent> components) {
		super(WebControllerProxy.class, components);
	}

	@Override
	protected Map<Class<? extends Annotation>, Map<String, Object>> createAnnotationsAttributes(
			WebControllerComponent component) {

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

		annotations.put(WebControllerBean.class, params);

		annotations.put(Component.class, new HashMap<String, Object>());

		params = new HashMap<String, Object>();
		params.put("value", component.getComponentConfig().getScope());

		annotations.put(Scope.class, params);

		return annotations;
	}

}