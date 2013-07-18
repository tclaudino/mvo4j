package br.com.cd.server.modules.service;

public interface ParametroSistemaService {

	public static class Param<T> {

		public final String KEY;
		public final T DEFAULT;

		public Param(String kEY, T dEFAULT) {
			KEY = kEY;
			DEFAULT = dEFAULT;
		}
	}

	public static final Param<Integer> RELOAD_PARAMS_TIME = new Param<Integer>(
			"RELOAD_PARAMS_TIME", (60 * 24));

	public static final Param<Integer> RELOAD_MODULES_TIME = new Param<Integer>(
			"RELOAD_MODULES_TIME", (60 * 24));

	public static final Param<String> MODULES_PATH = new Param<String>(
			"MODULES_PATH", "/cd/apps/scale/modules");

	public static final Param<String> CLIENT_ID = new Param<String>(
			"CLIENT_ID", "");

	void reload();

	String getString(String key, String defValue);

	String getString(String key);

	Integer getInt(String key, Integer defValue);

	Integer getInt(String key);

	<T> T getObject(Param<T> param, Class<T> returnType);

	Integer getInt(Param<Integer> param);

	String getString(Param<String> param);
}