package br.com.cd.mvo.ioc.scan;

import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.ioc.Container;

public interface ComponentScanner extends Comparable<ComponentScanner> {

	void scan(Scanner scanner, Container container) throws ConfigurationException;

	int getOrder();

	void addMetaDataFactories(BeanMetaDataFactory<?, ?> metaDataFactory);
}