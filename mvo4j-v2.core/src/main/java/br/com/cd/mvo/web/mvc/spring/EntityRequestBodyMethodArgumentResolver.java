package br.com.cd.mvo.web.mvc.spring;

import java.io.IOException;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.mvc.DynamicController;
import br.com.cd.util.ParserUtils;
import br.com.cd.util.ThreadLocalMapUtil;

public class EntityRequestBodyMethodArgumentResolver extends RequestResponseBodyMethodProcessor {

	private Container container;

	public EntityRequestBodyMethodArgumentResolver(Container container, List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
		this.container = container;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return super.supportsParameter(parameter) && DynamicController.PATH_VARIABLE_ENTITY.equals(ModelFactory.getNameForParameter(parameter));
	}

	public boolean supportsReturnType(MethodParameter returnType) {

		return super.supportsReturnType(returnType) && DynamicController.PATH_VARIABLE_ENTITY.equals(ModelFactory.getNameForParameter(returnType));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
			throws Exception {

		String viewName = ParserUtils.parseString(ThreadLocalMapUtil.getThreadVariable(DynamicController.CURRENT_VIEW_NAME));

		if (!viewName.isEmpty() && container.containsBean(viewName)) {
			@SuppressWarnings("rawtypes")
			WebCrudController controller = container.getBean(viewName, WebCrudController.class);

			parameter = new TypedMethodParameter(parameter, controller.getBeanMetaData().targetEntity());
		}

		return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
			throws IOException, HttpMediaTypeNotAcceptableException {

		String viewName = ParserUtils.parseString(ThreadLocalMapUtil.getThreadVariable(DynamicController.CURRENT_VIEW_NAME));

		if (!viewName.isEmpty() && container.containsBean(viewName)) {
			@SuppressWarnings("rawtypes")
			WebCrudController controller = container.getBean(viewName, WebCrudController.class);

			returnType = new TypedMethodParameter(returnType, controller.getBeanMetaData().targetEntity());
		}

		super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}

}
