package br.com.cd.mvo.orm;

import java.lang.reflect.Method;

import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.CrudServiceListener;
import br.com.cd.mvo.ioc.Container;

public class ServiceListenerMethodInterceptor<T> extends AbstractListenerMethodInterceptor<T, CrudService<T>, CrudServiceListener<T>> {

	public ServiceListenerMethodInterceptor(CrudService<T> bean, Container container) {
		super(bean, container);
	}

	@Override
	public Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable {

		switch (method.getName()) {
		// case "delete":
		// return beforeDelete((T) args[0], invokeCallback);
		default:
			return super.beforeInvoke(target, method, invokeCallback, args);
		}
	}

	@Override
	public void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable {
		switch (method.getName()) {
		// case "save":
		// afterSave((T) result);
		// break;
		default:
			super.afterInvoke(target, method, args);
			break;
		}
	}
}
