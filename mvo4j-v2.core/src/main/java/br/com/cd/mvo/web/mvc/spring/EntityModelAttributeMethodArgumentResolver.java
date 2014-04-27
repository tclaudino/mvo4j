package br.com.cd.mvo.web.mvc.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.mvc.DynamicController;
import br.com.cd.util.ParserUtils;
import br.com.cd.util.ThreadLocalMapUtil;

public class EntityModelAttributeMethodArgumentResolver extends ModelAttributeMethodProcessor {

	private Container container;

	public EntityModelAttributeMethodArgumentResolver(Container container) {
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

			String viewName = ParserUtils.parseString(ThreadLocalMapUtil.getThreadVariable(DynamicController.CURRENT_VIEW_NAME));

			if (!viewName.isEmpty() && container.containsBean(viewName)) {
				@SuppressWarnings("rawtypes")
				WebCrudController controller = container.getBean(viewName, WebCrudController.class);

				parameter = new TypedMethodParameter(parameter, controller.getBeanMetaData().targetEntity());
			}

			return super.createAttribute(attributeName, parameter, binderFactory, request);
		}

		@Override
		protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
			super.bindRequestParameters(binder, request);
		}
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return super.supportsParameter(parameter) && DynamicController.PATH_VARIABLE_ENTITY.equals(ModelFactory.getNameForParameter(parameter));
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
