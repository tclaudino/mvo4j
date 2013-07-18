package br.com.cd.scaleframework.core.modules;

import static br.com.caelum.vraptor.view.Results.http;

import java.lang.reflect.Method;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorHandlerFactory;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.cd.scaleframework.core.modules.context.ModuleContext;
import br.com.cd.scaleframework.core.modules.service.ModuleFactory;
import br.com.cd.scaleframework.module.Module;
import br.com.cd.scaleframework.module.PublishEvent;

@Intercepts(before = ExecuteMethodInterceptor.class, after = ParametersInstantiatorInterceptor.class)
public class ModuleViewInterceptor implements Interceptor {

	private final String NEW_LINE = ";\n";

	private ModuleFactory moduleFactory;
	private Container container;
	private ModuleContext moduleContext;
	private RequestInfo requestInfo;
	private MethodInfo methodInfo;
	private InterceptorHandlerFactory interceptorFactory;
	private Result result;
	private Router router;

	public ModuleViewInterceptor(ModuleFactory moduleFactory,
			Container container, ModuleContext moduleContext,
			RequestInfo requestInfo, MethodInfo methodInfo,
			InterceptorHandlerFactory interceptorFactory, Result result,
			Router router) {
		this.moduleFactory = moduleFactory;
		this.container = container;
		this.moduleContext = moduleContext;
		this.requestInfo = requestInfo;
		this.methodInfo = methodInfo;
		this.interceptorFactory = interceptorFactory;
		this.result = result;
		this.router = router;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {

		Module module = method.getResource().getType()
				.getAnnotation(Module.class);

		ModuleDefinition moduleBean = new ModuleDefinition(module, method
				.getResource().getType());
		if (!moduleFactory.isActive(moduleBean)) {
			throw new InterceptionException("module '" + moduleBean
					+ "' is not active in '"
					+ resourceInstance.getClass().getName() + "'");
		}

		if (method.getMethod().isAnnotationPresent(PublishEvent.class)) {

			StringBuilder builder = new StringBuilder();

			builder.append(String.format(
					"var %s = new Module('%s', '%s', '%s'))%s", module.name(),
					module.id(), module.name(), module.version(), NEW_LINE));
			builder.append(String.format("ModuleContext.addModule(%s)%s",
					module.name(), NEW_LINE));

			PublishEvent publishEvent = method.getMethod().getAnnotation(
					PublishEvent.class);

			for (ModuleDependent dependent : moduleFactory.getDependentModules(
					module, publishEvent.eventType())) {

				String route = router.urlFor(dependent.getModule()
						.getResourceClass(), dependent.getMethod(), methodInfo
						.getParameters());

				builder.append(String
						.format("%s.addDependent(new DepedentModule('%s', '%s', '%s', '%s', '%s', '%s'))%s",
								module.name(), dependent.getModule().getId(),
								dependent.getModule().getName(), dependent
										.getModule().getVersion(), module.id(),
								dependent.getSelector(), route, NEW_LINE));

				// ResourceMethod rm = router.parse(route,
				// HttpMethod.of(requestInfo.getRequest()),
				// requestInfo.getRequest());
				//
				// DefaultInterceptorStack newStack = new
				// DefaultInterceptorStack(
				// interceptorFactory);
				//
				// newStack.add(ParametersInstantiatorInterceptor.class);
				// newStack.add(ExecuteMethodInterceptor.class);
				// newStack.add(ForwardToDefaultViewInterceptor.class);
				//
				// this.moduleContext.getAttributes().add("TESTE",
				// "Eu sou zica mesmo!");

				// Object obj = container.instanceFor(dependent.getModule()
				// .getResourceClass());
				// newStack.next(rm, obj);
			}
			result.use(http()).body("" + builder.toString() + "");
			return;
		}

		stack.next(method, resourceInstance);
	}

	private boolean checkIfEqualsParameters(Method method) {
		if (method.getParameterTypes().length != methodInfo.getParameters().length)
			return false;

		for (int i = 0; i < method.getParameterTypes().length; i++) {
			if (!methodInfo.getParameters()[i].getClass().isAssignableFrom(
					method.getParameterTypes()[i]))
				return false;
		}
		return true;
	}

	@Override
	public boolean accepts(ResourceMethod method) {

		return method.getResource().getType().isAnnotationPresent(Module.class);
	}

}
