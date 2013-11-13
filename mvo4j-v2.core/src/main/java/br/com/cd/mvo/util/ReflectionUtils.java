package br.com.cd.mvo.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionUtils {

	private static final Logger LOG = Logger.getLogger(ReflectionUtils.class.getName());

	public static String getString(Object toInvoke, String attrName) {
		try {
			return ParserUtils.parseString(ReflectionUtils.getObject(toInvoke, attrName));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage() + ", attrName: " + attrName, e);
		}
		return "";
	}

	public static int getInt(Object toInvoke, String attrName) {
		return ParserUtils.parseInt(getString(toInvoke, attrName));
	}

	public static boolean getBoolean(Object toInvoke, String attrName) {
		return ParserUtils.parseBoolean(getString(toInvoke, attrName));
	}

	public static Object getObject(Object toInvoke, String attrName) throws Exception {
		@SuppressWarnings("rawtypes")
		Class klass = toInvoke.getClass();

		Field[] fields = klass.getDeclaredFields();
		Field field = null;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(attrName)) {
				field = fields[i];
				break;
			}
		}

		if (field == null) {
			return null;
		}
		field = changeAttrToPublic(field);

		return field.get(toInvoke);
	}

	public static int getInt(Object toInvoke, Method method, Object... args) {
		return ParserUtils.parseInt(ReflectionUtils.getString(toInvoke, method, args));
	}

	public static boolean getBoolean(Object toInvoke, Method method, Object... args) {
		return ParserUtils.parseBoolean(ReflectionUtils.getString(toInvoke, method, args));
	}

	public static String getString(Object toInvoke, Method method, Object... args) {

		try {
			return ParserUtils.parseString(ReflectionUtils.getObject(toInvoke, method, args));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage() + ", method: " + method, e);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(Class<T> returnType, Object toInvoke, Method method, Object... args) throws Exception {
		return (T) ReflectionUtils.getObject(toInvoke, method, args);
	}

	public static Object getObject(Object toInvoke, Method method, Object... args) throws Exception {
		return method.invoke(toInvoke, args);
	}

	private static Field changeAttrToPublic(Field attr) {
		try {
			if (attr.getModifiers() != Field.PUBLIC) {
				attr.setAccessible(true);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage() + ", Field: " + attr, e);
		}
		return attr;
	}

	private final static int METHOD_CLASS_GETDECLAREDCONSTRUCTOR = 0x01;

	private final static int METHOD_CLASS_GETDECLAREDCONSTRUCTORS = 0x02;

	private final static int METHOD_CLASS_GETDECLAREDMETHOD = 0x03;

	private final static int METHOD_CLASS_GETDECLAREDMETHODS = 0x04;

	private final static int METHOD_CLASS_GETDECLAREDFIELD = 0x05;

	private final static int METHOD_CLASS_GETDECLAREDFIELDS = 0x06;

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> doPrivilegedGetDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		Object obj = AccessController
				.doPrivileged(new PrivilegedActionForClass(clazz, parameterTypes, METHOD_CLASS_GETDECLAREDCONSTRUCTOR));
		if (obj instanceof NoSuchMethodException) {
			throw (NoSuchMethodException) obj;
		}
		return (Constructor<T>) obj;
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] doPrivilegedGetDeclaredConstructors(Class<T> clazz) {
		Object obj = AccessController.doPrivileged(new PrivilegedActionForClass(clazz, null, METHOD_CLASS_GETDECLAREDCONSTRUCTORS));
		return (Constructor<T>[]) obj;
	}

	public static <T> Method doPrivilegedGetDeclaredMethod(Class<T> clazz, String name, Class<?>... parameterTypes)
			throws NoSuchMethodException {
		Object obj = AccessController.doPrivileged(new PrivilegedActionForClass(clazz, new Object[] { name, parameterTypes },
				METHOD_CLASS_GETDECLAREDMETHOD));
		if (obj instanceof NoSuchMethodException) {
			throw (NoSuchMethodException) obj;
		}
		return (Method) obj;
	}

	public static <T> Method[] doPrivilegedGetDeclaredMethods(Class<T> clazz) {
		Object obj = AccessController.doPrivileged(new PrivilegedActionForClass(clazz, null, METHOD_CLASS_GETDECLAREDMETHODS));
		return (Method[]) obj;
	}

	public static <T> Field doPrivilegedGetDeclaredField(Class<T> clazz, String name) throws NoSuchFieldException {
		Object obj = AccessController.doPrivileged(new PrivilegedActionForClass(clazz, name, METHOD_CLASS_GETDECLAREDFIELD));
		if (obj instanceof NoSuchFieldException) {
			throw (NoSuchFieldException) obj;
		}
		return (Field) obj;
	}

	public static <T> Field[] doPrivilegedGetDeclaredFields(Class<T> clazz) {
		Object obj = AccessController.doPrivileged(new PrivilegedActionForClass(clazz, null, METHOD_CLASS_GETDECLAREDFIELDS));
		return (Field[]) obj;
	}

	protected static class PrivilegedActionForClass implements PrivilegedAction<Object> {
		Class<?> clazz;

		Object parameters;

		int method;

		protected PrivilegedActionForClass(Class<?> clazz, Object parameters, int method) {
			this.clazz = clazz;
			this.parameters = parameters;
			this.method = method;
		}

		public Object run() {
			try {
				switch (method) {
				case METHOD_CLASS_GETDECLAREDCONSTRUCTOR:
					return clazz.getDeclaredConstructor((Class<?>[]) parameters);
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
					return new IllegalArgumentException("unknown security method: " + method);
				}
			} catch (Exception exception) {
				return exception;
			}
		}
	}

	public static List<String> getJavaObjectMethods() {

		return new ArrayList<String>() {
			{

				for (Method m : Object.class.getDeclaredMethods()) {
					add(m.getName());
				}
			}
		};
	}

	public static boolean isSameSignature(Method methodA, Method methodB) {

		if (methodA == null || methodB == null) return false;
		if (!methodA.getName().equals(methodB.getName())) return false;

		Class<?>[] parameterTypesA = methodA.getParameterTypes();
		Class<?>[] parameterTypesB = methodB.getParameterTypes();

		if (parameterTypesA.length != parameterTypesB.length) return false;

		for (int i = 0; i < parameterTypesA.length; i++) {
			if (!parameterTypesA[i].equals(parameterTypesB[i])) return false;
		}

		return true;
	}

	private static Map<Method, Boolean> cachedMethods = new HashMap<>();

	public static boolean containsMethod(Method[] methods, Method method) {

		Boolean result = cachedMethods.get(method);
		if (result != null) return result;

		for (Method m : methods) {
			if (isSameSignature(m, method)) {
				cachedMethods.put(method, true);
				return true;
			}
		}
		cachedMethods.put(method, false);
		return false;
	}

}
