package br.com.cd.mvo.web.mvc.spring;

import org.springframework.core.MethodParameter;

public class TypedMethodParameter extends MethodParameter {

	private Class<?> parameterType;

	public TypedMethodParameter(MethodParameter original, Class<?> parameterType) {
		super(original);
		this.parameterType = parameterType;
	}

	@Override
	public Class<?> getParameterType() {
		return parameterType;
	}
}
