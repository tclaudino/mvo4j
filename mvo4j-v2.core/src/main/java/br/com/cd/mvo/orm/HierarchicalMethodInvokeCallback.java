package br.com.cd.mvo.orm;

import java.lang.reflect.Method;

import br.com.cd.mvo.ioc.AbstractMethodInvokeCallback;
import br.com.cd.mvo.ioc.MethodInvokeCallback;

public abstract class HierarchicalMethodInvokeCallback extends AbstractMethodInvokeCallback {

	private MethodInvokeCallback parent;

	public HierarchicalMethodInvokeCallback(MethodInvokeCallback parent) {
		this.parent = parent;
	}

	@Override
	public Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable {

		return parent.beforeInvoke(target, method, invokeCallback, args);
	}

	@Override
	public void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable {

		parent.afterInvoke(target, method, result, args);
	}

}
