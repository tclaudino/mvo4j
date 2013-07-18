package br.com.cd.scaleframework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class SecurityUtils {

	private final static int METHOD_CLASS_GETDECLAREDCONSTRUCTOR = 0x01;

	private final static int METHOD_CLASS_GETDECLAREDCONSTRUCTORS = 0x02;

	private final static int METHOD_CLASS_GETDECLAREDMETHOD = 0x03;

	private final static int METHOD_CLASS_GETDECLAREDMETHODS = 0x04;

	private final static int METHOD_CLASS_GETDECLAREDFIELD = 0x05;

	private final static int METHOD_CLASS_GETDECLAREDFIELDS = 0x06;

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> doPrivilegedGetDeclaredConstructor(
			Class<T> clazz, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz,
						parameterTypes, METHOD_CLASS_GETDECLAREDCONSTRUCTOR));
		if (obj instanceof NoSuchMethodException) {
			throw (NoSuchMethodException) obj;
		}
		return (Constructor<T>) obj;
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] doPrivilegedGetDeclaredConstructors(
			Class<T> clazz) {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz, null,
						METHOD_CLASS_GETDECLAREDCONSTRUCTORS));
		return (Constructor<T>[]) obj;
	}

	public static <T> Method doPrivilegedGetDeclaredMethod(Class<T> clazz,
			String name, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz, new Object[] {
						name, parameterTypes }, METHOD_CLASS_GETDECLAREDMETHOD));
		if (obj instanceof NoSuchMethodException) {
			throw (NoSuchMethodException) obj;
		}
		return (Method) obj;
	}

	public static <T> Method[] doPrivilegedGetDeclaredMethods(Class<T> clazz) {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz, null,
						METHOD_CLASS_GETDECLAREDMETHODS));
		return (Method[]) obj;
	}

	public static <T> Field doPrivilegedGetDeclaredField(Class<T> clazz,
			String name) throws NoSuchFieldException {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz, name,
						METHOD_CLASS_GETDECLAREDFIELD));
		if (obj instanceof NoSuchFieldException) {
			throw (NoSuchFieldException) obj;
		}
		return (Field) obj;
	}

	public static <T> Field[] doPrivilegedGetDeclaredFields(Class<T> clazz) {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz, null,
						METHOD_CLASS_GETDECLAREDFIELDS));
		return (Field[]) obj;
	}

	protected static class PrivilegedActionForClass implements
			PrivilegedAction<Object> {
		Class<?> clazz;

		Object parameters;

		int method;

		protected PrivilegedActionForClass(Class<?> clazz, Object parameters,
				int method) {
			this.clazz = clazz;
			this.parameters = parameters;
			this.method = method;
		}

		public Object run() {
			try {
				switch (method) {
				case METHOD_CLASS_GETDECLAREDCONSTRUCTOR:
					return clazz
							.getDeclaredConstructor((Class<?>[]) parameters);
				case METHOD_CLASS_GETDECLAREDCONSTRUCTORS:
					return clazz.getDeclaredConstructors();
				case METHOD_CLASS_GETDECLAREDMETHOD:
					String name = (String) ((Object[]) parameters)[0];
					Class<?>[] realParameters = (Class<?>[]) ((Object[]) parameters)[1];
					return clazz.getDeclaredMethod(name, realParameters);
				case METHOD_CLASS_GETDECLAREDMETHODS:
					return clazz.getDeclaredMethods();
				case METHOD_CLASS_GETDECLAREDFIELD:
					return clazz.getDeclaredField((String) parameters);
				case METHOD_CLASS_GETDECLAREDFIELDS:
					return clazz.getDeclaredFields();

				default:
					return new IllegalArgumentException(
							"unknown security method: " + method);
				}
			} catch (Exception exception) {
				return exception;
			}
		}

	}

}
