package br.com.cd.mvo.ioc;

import java.util.Properties;

import br.com.cd.mvo.util.ParserUtils;

public class LocalPropertyContainerConfig extends AbstractContainerConfig<Properties> {

	public LocalPropertyContainerConfig(Properties localProperties) {
		super(localProperties, new DefaultContainerListener());
	}

	@Override
	public String getInitParameter(String key) {

		return ParserUtils.parseString(localContainer.get(key));
	}
}
