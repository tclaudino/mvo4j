package br.com.cd.mvo;

import java.util.List;
import java.util.Map;

public interface Application {

	public enum MessageLevel {

		WARNING, ERROR, INFO;
	}

	public String BEAN_NAME = Application.class.getName();

	ApplicationMessage.ThrowableMessage addErrorMessage(Exception ex, String summary);

	ApplicationMessage.ThrowableMessage addErrorMessage(Exception ex, String summary, String clientId);

	ApplicationMessage addErrorMessage(String summary, String detail);

	ApplicationMessage addErrorMessage(String summary, String detail, String clientId);

	List<ApplicationMessage> addErrorMessages(Map<String, String> messages);

	List<ApplicationMessage> addInfoMessages(Map<String, String> messages);

	ApplicationMessage addInfoMessage(String msgSumary, String msgDetail);

	ApplicationMessage addInfoMessage(String msgSumary, String msgDetail, String clientId);

	ApplicationMessage addWarningMessage(String msgSumary, String msgDetail);

	ApplicationMessage addWarningMessage(String msgSumary, String msgDetail, String clientId);

	List<ApplicationMessage> addWarningMessages(Map<String, String> messages);

	List<ApplicationMessage> addSuccessMessages(Map<String, String> messages);

	ApplicationMessage addSuccessMessage(String msgSumary, String msgDetail);

	ApplicationMessage addSuccessMessage(String msgSumary, String msgDetail, String clientId);

	List<ApplicationMessage> getAllMessages();

	List<ApplicationMessage> getAllErrorMessages();

	List<ApplicationMessage.ThrowableMessage> getAllThrowableMessages();

	List<ApplicationMessage> getAllInfoMessages();

	List<ApplicationMessage> getAllWarningMessages();

	List<ApplicationMessage> getAllSuccessMessages();

	// ServletContext getServletContext();
}
