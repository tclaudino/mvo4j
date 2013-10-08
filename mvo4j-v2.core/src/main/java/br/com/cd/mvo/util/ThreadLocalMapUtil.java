package br.com.cd.mvo.util;

import java.util.Map;
import java.util.TreeMap;

public class ThreadLocalMapUtil {

	public interface InitialValue {

		abstract Object create();

	}

	private final static ThreadLocal<Map<String, Object>> THREAD_VARIABLES = new ThreadLocal<Map<String, Object>>() {

		/**
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Map<String, Object> initialValue() {
			return new TreeMap<String, Object>();
		}
	};

	public static Object getThreadVariable(String name) {
		return THREAD_VARIABLES.get().get(name);
	}

	public static Object getThreadVariable(String name,
			InitialValue initialValue) {
		Object o = THREAD_VARIABLES.get().get(name);
		if (o == null) {
			THREAD_VARIABLES.get().put(name, initialValue.create());
			return getThreadVariable(name);
		} else {
			return o;
		}
	}

	public static void setThreadVariable(String name, Object value) {
		THREAD_VARIABLES.get().put(name, value);
	}

	public static void removeThreadVariable(String name) {
		THREAD_VARIABLES.get().remove(name);
	}

	public static void destroy() {
		THREAD_VARIABLES.remove();
	}
}
