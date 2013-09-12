package br.com.cd.scaleframework.context;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

public interface Application {

	public enum MessageLevel {

		WARNING, ERROR, INFO;
	}

	ApplicationMessage.ThrowableMessage addErrorMessage(Exception ex,
			String summary);

	ApplicationMessage.ThrowableMessage addErrorMessage(Exception ex,
			String summary, String clientId);

	ApplicationMessage addErrorMessage(String summary, String detail);

	ApplicationMessage addErrorMessage(String summary, String detail,
			String clientId);

	List<ApplicationMessage> addErrorMessages(Map<String, String> messages);

	List<ApplicationMessage> addInfoMessages(Map<String, String> messages);

	ApplicationMessage addInfoMessage(String msgSumary, String msgDetail);

	ApplicationMessage addInfoMessage(String msgSumary, String msgDetail,
			String clientId);

	ApplicationMessage addWarningMessage(String msgSumary, String msgDetail);

	ApplicationMessage addWarningMessage(String msgSumary, String msgDetail,
			String clientId);

	List<ApplicationMessage> addWarningMessages(Map<String, String> messages);

	List<ApplicationMessage> addSuccessMessages(Map<String, String> messages);

	ApplicationMessage addSuccessMessage(String msgSumary, String msgDetail);

	ApplicationMessage addSuccessMessage(String msgSumary, String msgDetail,
			String clientId);

	List<ApplicationMessage> getAllMessages();

	List<ApplicationMessage> getAllErrorMessages();

	List<ApplicationMessage.ThrowableMessage> getAllThrowableMessages();

	List<ApplicationMessage> getAllInfoMessages();

	List<ApplicationMessage> getAllWarningMessages();

	List<ApplicationMessage> getAllSuccessMessages();

	List<Locale> getSupportedLocales();

	Locale getDefaulLocale();

	String getMessageBundle();

	String getParameter(String parameterName);

	String getParameter(String parameterName, String defaultValue);

	<T> T getParameter(String parameterName, Class<T> resultType);

	<T> T getParameter(String parameterName, T defaultValue);

	ServletContext getServletContext();

}