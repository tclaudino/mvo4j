package br.com.cd.server.modules.service;

import javax.ejb.Local;
import javax.ejb.Remote;

import br.com.cd.scaleframework.core.modules.ModuleBean;
import br.com.cd.server.modules.model.ServerInfo;

@Remote
@Local
public interface ModuleService {

	boolean isValid(ModuleBean module, ServerInfo serverInfo);
}
