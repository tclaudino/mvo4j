package br.com.cd.scaleframework.core.discovery;

import br.com.cd.scaleframework.core.ComponentObject;

public interface ComponentDiscoveryVisitor {

	void visit(ComponentObject<?> component);

}