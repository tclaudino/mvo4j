package br.com.cd.scaleframework.core.modules.context;

import br.com.cd.scaleframework.context.ServiceContext;
import br.com.cd.scaleframework.core.MapModel;

public interface ModuleContext extends ServiceContext {

	MapModel getAttributes();

}
