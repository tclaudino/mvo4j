package br.com.cd.scaleframework.core.support;

import java.util.LinkedList;
import java.util.List;

import br.com.cd.scaleframework.core.ComponentObject;
import br.com.cd.scaleframework.core.ComponentObserver;
import br.com.cd.scaleframework.core.discovery.ComponentDiscoveryVisitor;

public class DefaultComponentRegistryVisitor implements
		ComponentDiscoveryVisitor {

	private List<ComponentObject<?>> components = new LinkedList<ComponentObject<?>>();

	@SuppressWarnings("unchecked")
	public void visit(ComponentObject<?> target) {
		for (ComponentObject<?> component : components) {
			for (@SuppressWarnings("rawtypes")
			ComponentObserver observer : component.getObservers()) {
				if (observer.getComponentType().isAssignableFrom(
						target.getClass())) {
					if (observer.getTargetEntity().isAssignableFrom(
							target.getComponentConfig().getTargetEntity())) {
						observer.onDiscoverTarget(target);
					}
				}
			}
		}
		components.add(target);
	}

}