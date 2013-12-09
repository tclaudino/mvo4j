package br.com.cd.mvo.web.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import br.com.cd.mvo.util.ThreadLocalMapUtil;

public class MvoViewNameMethodArgumentResolver extends PathVariableMethodArgumentResolver {

	public static final String CURRENT_VIEW_NAME = MvoViewNameMethodArgumentResolver.class.getName() + ".currentViewName";

	public MvoViewNameMethodArgumentResolver() {
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return super.supportsParameter(parameter) && "viewName".equals(((PathVariable) parameter.getParameterAnnotation(PathVariable.class)).value());
	}

	protected void handleResolvedValue(Object arg, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request) {

		super.handleResolvedValue(arg, name, parameter, mavContainer, request);

		ThreadLocalMapUtil.setThreadVariable(CURRENT_VIEW_NAME, arg);
	}

}
