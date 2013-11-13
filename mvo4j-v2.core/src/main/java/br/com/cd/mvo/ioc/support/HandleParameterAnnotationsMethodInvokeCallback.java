package br.com.cd.mvo.ioc.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import br.com.cd.mvo.util.ThreadLocalMapUtil;

public class HandleParameterAnnotationsMethodInvokeCallback extends AbstractMethodInvokeCallback {

	public static final String PARAMETER_ANNOTATIONS = "PARAMETER_ANNOTATIONS";

	@Override
	public boolean beforeInvoke(Method method) {

		ThreadLocalMapUtil.removeThreadVariable(PARAMETER_ANNOTATIONS);

		return super.beforeInvoke(method);
	}

	@Override
	public void afterInvoke(Method method) {

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		if (parameterAnnotations.length > 0 && parameterAnnotations[0].length > 0)
			ThreadLocalMapUtil.setThreadVariable(PARAMETER_ANNOTATIONS, joinAnnotations(method.getParameterAnnotations()));
	}

	private Annotation[] joinAnnotations(Annotation[][] annotationsArray) {

		List<Annotation> list = new LinkedList<>();
		for (Annotation[] annotations : annotationsArray) {
			if (annotations.length > 0) list.addAll(Arrays.asList(annotations));
		}
		return list.toArray(new Annotation[list.size()]);
	}
}
