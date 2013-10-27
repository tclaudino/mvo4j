package br.com.cd.mvo;

import br.com.cd.mvo.ioc.support.CglibProxifier;
import br.com.cd.mvo.orm.impl.JpaRepositoryFactory;
import br.com.cd.mvo.web.ioc.spring.SpringWebContainerProvider;

public class ConfigParamKeys {

	public static final String CACHE_MANAGER_MAX_SIZE = "br.com.cd.CACHE_MANAGER_MAX_SIZE";
	public static final String I18N_CACHE_TIME = "br.com.cd.I18N_CACHE_TIME";
	public static final String IMAGES_FOLDER = "br.com.cd.IMAGES_FOLDER";
	public static final String IMAGES_CACHETIME = "br.com.cd.IMAGES_CACHETIME";
	public static final String IMAGES_FILTER_URL_PATTERN = "br.com.cd.IMAGES_FILTER_URL_PATTERN";
	public static final String DEFAULT_LOCALE = "br.com.cd.DEFAULT_LOCALE";
	public static final String SUPORTED_LOCALES = "br.com.cd.SUPORTED_LOCALES";
	public static final String BUNDLE_NAME = "br.com.cd.BUNDLE_NAME";
	public static final String REPOSITORY_FACTORY_CLASS = "br.com.cd.PERSISTENCE_IMPL_CLASS";
	public static final String INITIAL_PAGESIZE = "br.com.cd.INITIAL_PAGESIZE";
	public static final String PERSISTENCE_MANAGER_FACTORY_BEAN_NAME = "br.com.cd.PERSISTENCE_FACTORY_BEAN_NAME";
	public static final String DEFAULT_SCOPE_NAME = "br.com.cd.SCOPE_NAME";
	public static final String PACKAGES_TO_SCAN = "br.com.cd.PACKAGES_TO_SCAN";
	public static final String PROVIDER_CLASS = "br.com.cd.PROVIDER_CLASS";
	public static final String BEAN_INSTANTIATION_STRATEGY_CLASS = "br.com.cd.BEAN_INSTANTIATION_STRATEGY_CLASS";
	public static final String SCOPE_SINGLETON_NAME = "br.com.cd.SCOPE_SINGLETON_NAME";
	public static final String SCOPE_REQUEST_NAME = "br.com.cd.SCOPE_REQUEST_NAME";
	public static final String SCOPE_APPLICATION_NAME = "br.com.cd.SCOPE_APPLICATION_NAME";
	public static final String SCOPE_SESSION_NAME = "br.com.cd.SCOPE_SESSION_NAME";
	public static final String SCOPE_CONVERSATION_NAME = "br.com.cd.SCOPE_CONVERSATION_NAME";

	public static class DefaultValues {

		public static final long CACHE_MANAGER_MAX_SIZE = 6000L;
		public static final long I18N_CACHE_TIME = 3200L;
		public static final long IMAGES_CACHETIME = 800L;
		public static final String IMAGES_FILTER_URL_PATTERN = "/getImage";
		public static final String DEFAULT_LOCALE = "en_US";
		public static final String SUPORTED_LOCALES = "en_US,pt_BR";
		public static final String BUNDLE_NAME = "i18n";
		public static final String REPOSITORY_FACTORY_CLASS = JpaRepositoryFactory.class
				.getName();
		public static final int INITIAL_PAGESIZE = 10;
		public static final String PERSISTENCE_MANAGER_FACTORY_BEAN_NAME = "entityManagerFactory";
		public static final String SCOPE_NAME = "singleton";
		public static final String PROVIDER_CLASS = SpringWebContainerProvider.class
				.getName();
		public static final String BEAN_INSTANTIATION_STRATEGY_CLASS = CglibProxifier.class
				.getName();
		public static final String SCOPE_SINGLETON_NAME = "singleton";
		public static final String SCOPE_REQUEST_NAME = "request";
		public static final String SCOPE_APPLICATION_NAME = "application";
		public static final String SCOPE_SESSION_NAME = "session";
		public static final String SCOPE_CONVERSATION_NAME = "conversation";
	}
}