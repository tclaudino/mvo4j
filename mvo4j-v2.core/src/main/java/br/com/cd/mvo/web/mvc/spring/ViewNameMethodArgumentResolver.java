package br.com.cd.mvo.web.mvc.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import br.com.cd.mvo.web.mvc.DynamicController;
import br.com.cd.util.ThreadLocalMapUtil;

public class ViewNameMethodArgumentResolver extends PathVariableMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return super.supportsParameter(parameter)
				&& DynamicController.PATH_VARIABLE_VIEW_NAME.equals(((PathVariable) parameter.getParameterAnnotation(PathVariable.class)).value());
	}

	protected void handleResolvedValue(Object arg, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request) {

		super.handleResolvedValue(arg, name, parameter, mavContainer, request);

		ThreadLocalMapUtil.setThreadVariable(DynamicController.CURRENT_VIEW_NAME, arg);
	}

}
