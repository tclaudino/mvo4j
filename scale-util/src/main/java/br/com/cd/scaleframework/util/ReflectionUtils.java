package br.com.cd.scaleframework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionUtils {

	private static final Logger LOG = Logger.getLogger(ReflectionUtils.class
			.getName());

	public static String getString(Object toInvoke, String attrName) {
		try {
			return ParserUtils.parseString(ReflectionUtils.getObject(toInvoke,
					attrName));
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

	public static Object getObject(Object toInvoke, String attrName)
			throws Exception {
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
		return ParserUtils.parseInt(ReflectionUtils.getString(toInvoke, method,
				args));
	}

	public static boolean getBoolean(Object toInvoke, Method method,
			Object... args) {
		return ParserUtils.parseBoolean(ReflectionUtils.getString(toInvoke,
				method, args));
	}

	public static String getString(Object toInvoke, Method method,
			Object... args) {

		try {
			return ParserUtils.parseString(ReflectionUtils.getObject(toInvoke,
					method, args));
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage() + ", method: " + method, e);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(Class<T> returnType, Object toInvoke,
			Method method, Object... args) throws Exception {
		return (T) ReflectionUtils.getObject(toInvoke, method, args);
	}

	public static Object getObject(Object toInvoke, Method method,
			Object... args) throws Exception {
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

}
