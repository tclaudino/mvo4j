package br.com.cd.scaleframework.core.modules.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import br.com.cd.scaleframework.core.modules.service.ModuleFactoryRegistry;
import br.com.cd.scaleframework.core.modules.service.support.AbstractModuleDiscovery;
import br.com.cd.scaleframework.core.resources.service.ResourcesService;

@Component
public class DefaultModuleDiscoveryImpl extends AbstractModuleDiscovery {

	@Autowired
	public DefaultModuleDiscoveryImpl(ModuleFactoryRegistry registry,
			ResourcesService resourcesService, WebApplicationContext context) {
		super(registry, resourcesService, context);
	}

}
