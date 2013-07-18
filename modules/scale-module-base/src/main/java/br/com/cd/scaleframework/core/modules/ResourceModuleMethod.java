package br.com.cd.scaleframework.core.modules;

import java.lang.reflect.Method;

public class ResourceModuleMethod {

	private final Method method;

	public ResourceModuleMethod(Method method) {
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

}
