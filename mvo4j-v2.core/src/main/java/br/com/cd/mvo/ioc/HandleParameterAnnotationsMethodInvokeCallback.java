package br.com.cd.mvo.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import br.com.cd.mvo.orm.HierarchicalMethodInvokeCallback;
import br.com.cd.util.ThreadLocalMapUtil;

public class HandleParameterAnnotationsMethodInvokeCallback extends HierarchicalMethodInvokeCallback {

	public HandleParameterAnnotationsMethodInvokeCallback(MethodInvokeCallback parent) {
		super(parent);
	}

	public static final String PARAMETER_ANNOTATIONS = "PARAMETER_ANNOTATIONS";

	@Override
	public Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable {

		ThreadLocalMapUtil.removeThreadVariable(PARAMETER_ANNOTATIONS);

		return super.beforeInvoke(target, method, invokeCallback, args);
	}

	@Override
	public void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable {

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		if (parameterAnnotations.length > 0 && parameterAnnotations[0].length > 0)
			ThreadLocalMapUtil.setThreadVariable(PARAMETER_ANNOTATIONS, joinAnnotations(method.getParameterAnnotations()));

		super.afterInvoke(target, method, result, args);
	}

	private Annotation[] joinAnnotations(Annotation[][] annotationsArray) {

		List<Annotation> list = new LinkedList<>();
		for (Annotation[] annotations : annotationsArray) {
			if (annotations.length > 0)
				list.addAll(Arrays.asList(annotations));
		}
		return list.toArray(new Annotation[list.size()]);
	}
}
