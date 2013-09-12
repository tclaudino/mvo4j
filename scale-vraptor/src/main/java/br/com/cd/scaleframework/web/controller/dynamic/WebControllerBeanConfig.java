package br.com.cd.scaleframework.web.controller.dynamic;

import java.io.Serializable;

import br.com.cd.scaleframework.controller.dynamic.ControllerBeanConfig;
import br.com.cd.scaleframework.controller.dynamic.PropertyMap;

public class WebControllerBeanConfig<T, ID extends Serializable> extends
		ControllerBeanConfig<T, ID> {

	public static final String PATH = "path";

	public WebControllerBeanConfig(PropertyMap adaptee, Class<T> entityType,
			Class<ID> entityIdType) {
		super(adaptee, entityType, entityIdType);
	}

	public String path() {
		return this.get(WebControllerBeanConfig.PATH);
	}

}