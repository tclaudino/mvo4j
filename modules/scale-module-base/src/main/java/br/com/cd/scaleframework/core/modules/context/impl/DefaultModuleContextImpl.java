package br.com.cd.scaleframework.core.modules.context.impl;

import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.core.MapModel;
import br.com.cd.scaleframework.core.modules.context.ModuleContext;
import br.com.cd.scaleframework.core.modules.eai.EAIContext;

@Component
public class DefaultModuleContextImpl implements ModuleContext {

	@Override
	public EAIContext getEAIContext() {
		// TODO Auto-generated method stub
		return null;
	}

	private MapModel attributes = new MapModel();

	@Override
	public MapModel getAttributes() {
		return attributes;
	}

}
