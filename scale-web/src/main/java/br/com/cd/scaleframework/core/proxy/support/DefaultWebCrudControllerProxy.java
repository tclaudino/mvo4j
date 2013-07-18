package br.com.cd.scaleframework.core.proxy.support;

import java.io.Serializable;

import br.com.cd.scaleframework.core.BeanException;
import br.com.cd.scaleframework.core.ConfigurationException;
import br.com.cd.scaleframework.core.RepositoryComponent;
import br.com.cd.scaleframework.core.WebCrudControllerComponent;
import br.com.cd.scaleframework.core.proxy.WebCrudControllerProxy;
import br.com.cd.scaleframework.orm.Repository;
import br.com.cd.scaleframework.orm.RepositoryFactory.SessionFactoryCallback;
import br.com.cd.scaleframework.orm.suport.ServiceFacade;
import br.com.cd.scaleframework.util.ReflectionUtils;
import br.com.cd.scaleframework.util.StringUtils;
import br.com.cd.scaleframework.web.context.WebApplicationContext;
import br.com.cd.scaleframework.web.controller.suport.AbstractWebCrudController;

@SuppressWarnings("rawtypes")
public class DefaultWebCrudControllerProxy extends
		AbstractWebCrudController<Object, Serializable> implements
		WebCrudControllerProxy {

	private ServiceFacade service;

	private WebCrudControllerComponent component;

	private WebApplicationContext applicationContext;

	private DefaultWebCrudControllerProxy(
			WebApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setComponent(WebCrudControllerComponent component) {
		this.component = component;
	}

	@Override
	public WebCrudControllerComponent getComponent() {
		if (component == null) {
			throw new UnsupportedOperationException(PROXY_NO_SETTED);
		}
		return component;
	}

	private Repository dao;

	@SuppressWarnings("unchecked")
	private Repository getRepository(final Class entityClass,
			Object sessionFactory) {

		if (dao == null) {
			if (component == null) {
				throw new UnsupportedOperationException(PROXY_NO_SETTED);
			}

			RepositoryComponent repositoryComponent = this.component
					.getTargetService().getTargetRepository();
			dao = repositoryComponent
					.getComponentProvider()
					.getRepositoryFactory()
					.createRepository(sessionFactory, entityClass,
							new SessionFactoryCallback() {

								@Override
								public void beforeClose(Object entity) {
									for (String property : component
											.getComponentConfig()
											.getLazyProperties().trim()
											.split(",")) {

										if (property.isEmpty()) {
											continue;
										}
										property = property.trim();

										String mtName = "get"
												+ StringUtils
														.toBeanConvencionCase(property);

										try {
											Object getResult = ReflectionUtils
													.getObject(entity, entity
															.getClass()
															.getMethod(mtName));
											if (getResult instanceof Iterable) {
												((Iterable) getResult)
														.iterator();
											} else {
												getResult.toString();
											}
										} catch (Exception e) {
											throw new ConfigurationException(
													"The bean '"
															+ component
																	.getComponentConfig()
																	.getName()
															+ "' has a inválid lazyProperties attribute.",
													e);
										}
									}

								}
							});
		}

		return dao;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public ServiceFacade getService() {
		if (service == null) {
			if (component == null) {
				throw new UnsupportedOperationException(PROXY_NO_SETTED);
			}
			final Repository dao = getRepository(
					this.component.getComponentConfig().getTargetEntity(),
					this.component.getTargetService().getTargetRepository()
							.getComponentProvider()
							.getSessionFactory(applicationContext));
			service = new ServiceFacade<Object, Serializable>() {

				@Override
				public Repository<Object, Serializable> getRepository() {
					return dao;
				}

			};
		}
		return service;
	}

	@Override
	public void setService(ServiceFacade service) {
		this.service = service;
	}

	@Override
	public Object newEntity() {
		if (component == null) {
			throw new UnsupportedOperationException(PROXY_NO_SETTED);
		}
		try {
			return component.getComponentConfig().getTargetEntity()
					.newInstance();
		} catch (Exception e) {
			throw new BeanException(e);
		}
	}
}
