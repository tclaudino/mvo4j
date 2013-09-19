package org.springview.client;

import org.springview.client.model.Client;

import br.com.cd.scaleframework.core.orm.dynamic.WebCrudControllerBean;

public class CrudConfigurator {

	@WebCrudControllerBean(name = "clientBean", path = "client", targetEntity = Client.class, entityIdType = Integer.class, makeList = "", lazyProperties = "clientContractList")
	public void loadClient() {
	}
}