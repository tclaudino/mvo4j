package br.com.cd.server.modules;

import javax.ejb.EJB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.core.modules.ModuleBean;
import br.com.cd.scaleframework.core.modules.ModuleValidator;
import br.com.cd.server.modules.model.ServerInfo;
import br.com.cd.server.modules.service.ModuleService;
import br.com.cd.server.modules.service.ParametroSistemaService;

@Component
public class ModuleValidatorImpl implements ModuleValidator {

	@EJB
	ModuleService serviceEJB;

	@Autowired
	ParametroSistemaService sistemaService;

	public boolean isValid(ModuleBean module) {

		String clientID = sistemaService
				.getString(ParametroSistemaService.CLIENT_ID);

		ServerInfo info = new ServerInfo();

		return serviceEJB.isValid(module, info);
	}

}
