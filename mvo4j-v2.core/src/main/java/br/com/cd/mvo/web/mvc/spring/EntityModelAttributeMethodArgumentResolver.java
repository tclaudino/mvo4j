package br.com.cd.mvo.web.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.util.ParserUtils;
import br.com.cd.mvo.util.ThreadLocalMapUtil;
import br.com.cd.mvo.web.WebCrudController;

public class MvoModelAttributeMethodArgumentResolver extends ModelAttributeMethodProcessor {

	private Container container;

	public MvoModelAttributeMethodArgumentResolver(Container container) {
		super(false);
		this.container = container;
	}

	public static class MvoServletModelAttributeMethodArgumentResolver extends ServletModelAttributeMethodProcessor {

		private Container container;

		public MvoServletModelAttributeMethodArgumentResolver(Container container) {
			super(false);
			this.container = container;
		}

		public Object _createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
				throws Exception {

			String viewName = ParserUtils.parseString(ThreadLocalMapUtil.getThreadVariable(MvoViewNameMethodArgumentResolver.CURRENT_VIEW_NAME));

			if (!viewName.isEmpty() && container.containsBean(viewName)) {
				@SuppressWarnings("rawtypes")
				WebCrudController controller = container.getBean(viewName, WebCrudController.class);

				parameter = new MvoMethodParameter(parameter, controller.getBeanMetaData().targetEntity());
			}

			return super.createAttribute(attributeName, parameter, binderFactory, request);
		}

		@Override
		protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
			super.bindRequestParameters(binder, request);
		}
	}

	public static class MvoMethodParameter extends MethodParameter {

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

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return super.supportsParameter(parameter) && "entity".equals(ModelFactory.getNameForParameter(parameter));
	}

	@Override
	protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
			throws Exception {

		return new MvoServletModelAttributeMethodArgumentResolver(container)._createAttribute(attributeName, parameter, binderFactory, request);
	}

	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {

		new MvoServletModelAttributeMethodArgumentResolver(container).bindRequestParameters(binder, request);
	}
}
