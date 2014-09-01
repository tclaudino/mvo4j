package br.com.cd.mvo.ioc;


public interface ContainerProvider<C extends ContainerConfig<?>> {

	Container getContainer(C config) throws ConfigurationException;

}