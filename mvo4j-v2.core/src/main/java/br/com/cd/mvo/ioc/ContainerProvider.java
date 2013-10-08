package br.com.cd.mvo.ioc;

import br.com.cd.mvo.core.ConfigurationException;

public interface ContainerProvider<C extends ContainerConfig<?>> {

	Container getContainer(C config) throws ConfigurationException;

}