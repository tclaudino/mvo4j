package br.com.cd.scaleframework.context;

import java.util.List;
import java.util.Map;

public interface Messenger {

	ApplicationMessage ensureAddErrorMessage(Exception ex, String summary);

	ApplicationMessage ensureAddErrorMessage(Exception ex, String summary,
			String clientId);

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

	List<ApplicationMessage> getMessages();

	List<ApplicationMessage> getErrors();

	List<ApplicationMessage> getInfos();

	List<ApplicationMessage> getWarnings();

	List<ApplicationMessage> getSuccesses();

}