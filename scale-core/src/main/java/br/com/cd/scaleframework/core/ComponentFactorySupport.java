package br.com.cd.scaleframework.core;

import br.com.cd.scaleframework.orm.SessionFactoryProvider;
import br.com.cd.scaleframework.orm.suport.NoneSessionFactoryProvider;
import br.com.cd.scaleframework.util.StringUtils;

public class ComponentFactorySupport {

	public static final Class<? extends SessionFactoryProvider<?>> defaultProvider = NoneSessionFactoryProvider.class;

	public static final String SESSION_FACTORY_QUALIFIER = "sessionFactory";

	public static final int INITIAL_PAGE_SIZE = 10;

	private String providerClass = "";

	private String packagesToScan = "";

	private String sessionFactoryQualifier = SESSION_FACTORY_QUALIFIER;

	private String scope = "singleton";

	private int initialPageSize = INITIAL_PAGE_SIZE;

	public String getProviderClass() {
		return providerClass;
	}

	public void setProviderClass(String providerClass) {
		this.providerClass = providerClass;
	}

	public String getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String getSessionFactoryQualifier() {
		return sessionFactoryQualifier;
	}

	public void setSessionFactoryQualifier(String sessionFactoryQualifier) {
		this.sessionFactoryQualifier = sessionFactoryQualifier;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends SessionFactoryProvider<?>> getTargetProvider()
			throws ConfigurationException {
		if (hasProvider()) {
			try {
				Class<? extends SessionFactoryProvider<?>> providerType = (Class<? extends SessionFactoryProvider<?>>) Class
						.forName(providerClass);

				if (!NoneSessionFactoryProvider.class.equals(providerType)) {
					providerType = br.com.cd.scaleframework.orm.suport.HibernateSessionFactoryProvider.class;
				}
				return providerType;

			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(e);
			}
		}
		return defaultProvider;
	}

	public boolean hasProvider() {
		return !StringUtils.isNullOrEmpty(this.providerClass);
	}

	public int getInitialPageSize() {
		return initialPageSize;
	}

	public void setInitialPageSize(int initialPageSize) {
		this.initialPageSize = initialPageSize;
	}

}
