package br.com.cd.scaleframework.web.context;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.cd.scaleframework.context.ApplicationMessage;
import br.com.cd.scaleframework.context.suport.AbstractMessenger;

@Component
@Scope("request")
public class WebMessenger extends AbstractMessenger {

	@Override
	protected void onAddMessage(ApplicationMessage message) {

		// validator.add(new ValidationMessage(message.getDetail(), message
		// .getSummary()));

		switch (message.getSeverity()) {
		case SEVERITY_ERROR:

		case SEVERITY_INFO:

		case SEVERITY_SUCCESS:

		case SEVERITY_WARN:

		}
	}

}