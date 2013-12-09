package br.com.cd.mvo.web.mvc.spring;

import org.springframework.core.MethodParameter;

public class MvoMethodParameter extends MethodParameter {

	private Class<?> parameterType;

	public MvoMethodParameter(MethodParameter original, Class<?> parameterType) {
		super(original);
		this.parameterType = parameterType;
	}

	@Override
	public Class<?> getParameterType() {
		return parameterType;
	}
}
