package br.com.cd.scaleframework.context;

public class ConfigParamKeys {

	public static final String CACHE_MANAGER_MAX_SIZE = "br.com.cd.CACHE_MANAGER_MAX_SIZE";
	public static final String I18N_CACHE_TIME = "br.com.cd.I18N_CACHE_TIME";
	public static final String IMAGES_FOLDER = "br.com.cd.IMAGES_FOLDER";
	public static final String IMAGES_CACHETIME = "br.com.cd.IMAGES_CACHETIME";
	public static final String IMAGES_FILTER_URL_PATTERN = "br.com.cd.IMAGES_FILTER_URL_PATTERN";
	public static final String DEFAULT_LOCALE = "br.com.cd.DEFAULT_LOCALE";
	public static final String SUPORTED_LOCALES = "br.com.cd.SUPORTED_LOCALES";
	public static final String BUNDLE_NAME = "br.com.cd.BUNDLE_NAME";

	public static class DefaultValues {

		public static final long CACHE_MANAGER_MAX_SIZE = 6000L;
		public static final long I18N_CACHE_TIME = 3200L;
		public static final long IMAGES_CACHETIME = 800L;
		public static final String IMAGES_FILTER_URL_PATTERN = "/getImage";
		public static final String DEFAULT_LOCALE = "en_US";
		public static final String SUPORTED_LOCALES = "en_US,pt_BR";
		public static final String BUNDLE_NAME = "i18n";
	}
}