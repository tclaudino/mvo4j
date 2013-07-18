package br.com.cd.scaleframework.core;

public interface ServiceComponent<C extends ServiceComponentConfig> extends
		ComponentObject<C> {

	void setTargetRepository(
			@SuppressWarnings("rawtypes") RepositoryComponent componentObject);

	@SuppressWarnings("rawtypes")
	RepositoryComponent getTargetRepository();

	ServiceComponent<C> getTargetService();

	void setTargetService(ServiceComponent<C> targetService);
}
