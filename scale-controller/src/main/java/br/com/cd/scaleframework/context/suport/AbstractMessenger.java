package br.com.cd.scaleframework.context.suport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cd.scaleframework.context.ApplicationMessage;
import br.com.cd.scaleframework.context.Messenger;
import br.com.cd.scaleframework.context.ApplicationMessage.Severity;

public abstract class AbstractMessenger implements Messenger {

	private List<ApplicationMessage> messages = new LinkedList<ApplicationMessage>();

	protected abstract void onAddMessage(ApplicationMessage message);

	@Override
	public ApplicationMessage ensureAddErrorMessage(Exception ex, String summary) {
		ApplicationMessage message = ApplicationMessage.createErrorMessage(ex,
				summary);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage ensureAddErrorMessage(Exception ex,
			String summary, String clientId) {
		ApplicationMessage message = ApplicationMessage.createErrorMessage(
				summary, summary, clientId);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public List<ApplicationMessage> addErrorMessages(
			Map<String, String> messages) {
		Iterator<Entry<String, String>> iterator = messages.entrySet()
				.iterator();

		List<ApplicationMessage> messageList = new LinkedList<ApplicationMessage>();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			ApplicationMessage message = ApplicationMessage.createErrorMessage(
					entry.getKey(), entry.getValue());
			onAddMessage(message);
			messageList.add(message);
			this.messages.add(message);
		}

		return messageList;
	}

	@Override
	public ApplicationMessage addErrorMessage(String summary, String detail) {
		ApplicationMessage message = ApplicationMessage.createErrorMessage(
				summary, detail);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addErrorMessage(String summary, String detail,
			String clientId) {
		ApplicationMessage message = ApplicationMessage.createErrorMessage(
				summary, detail, clientId);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public List<ApplicationMessage> addSuccessMessages(
			Map<String, String> messages) {
		List<ApplicationMessage> messageList = new LinkedList<ApplicationMessage>();

		Iterator<Entry<String, String>> iterator = messages.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			ApplicationMessage message = ApplicationMessage
					.createSuccessMessage(entry.getKey(), entry.getValue());
			onAddMessage(message);
			messageList.add(message);
			this.messages.add(message);
		}
		return messageList;
	}

	@Override
	public ApplicationMessage addSuccessMessage(String msgSumary,
			String msgDetail) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addSuccessMessage(String msgSumary,
			String msgDetail, String clientId) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail, clientId);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addWarningMessage(String msgSumary,
			String msgDetail) {
		ApplicationMessage message = ApplicationMessage.createWarningMessage(
				msgSumary, msgDetail);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addWarningMessage(String msgSumary,
			String msgDetail, String clientId) {
		ApplicationMessage message = ApplicationMessage.createWarningMessage(
				msgSumary, msgDetail, clientId);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public List<ApplicationMessage> addWarningMessages(
			Map<String, String> messages) {
		List<ApplicationMessage> messageList = new LinkedList<ApplicationMessage>();

		Iterator<Entry<String, String>> iterator = messages.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			ApplicationMessage message = ApplicationMessage
					.createWarningMessage(entry.getKey(), entry.getValue());
			onAddMessage(message);
			messageList.add(message);
			this.messages.add(message);
		}
		return messageList;
	}

	@Override
	public List<ApplicationMessage> addInfoMessages(Map<String, String> messages) {
		List<ApplicationMessage> messageList = new LinkedList<ApplicationMessage>();

		Iterator<Entry<String, String>> iterator = messages.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			ApplicationMessage message = ApplicationMessage.createInfoMessage(
					entry.getKey(), entry.getValue());
			onAddMessage(message);
			messageList.add(message);
			this.messages.add(message);
		}
		return messageList;
	}

	@Override
	public ApplicationMessage addInfoMessage(String msgSumary, String msgDetail) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addInfoMessage(String msgSumary,
			String msgDetail, String clientId) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail, clientId);
		onAddMessage(message);
		messages.add(message);
		return message;
	}

	@Override
	public List<ApplicationMessage> getMessages() {
		return messages;
	}

	@Override
	public List<ApplicationMessage> getErrors() {
		return getMessages(Severity.SEVERITY_ERROR);
	}

	@Override
	public List<ApplicationMessage> getInfos() {
		return getMessages(Severity.SEVERITY_INFO);
	}

	@Override
	public List<ApplicationMessage> getWarnings() {
		return getMessages(Severity.SEVERITY_WARN);
	}

	@Override
	public List<ApplicationMessage> getSuccesses() {
		return getMessages(Severity.SEVERITY_SUCCESS);
	}

	public List<ApplicationMessage> getMessages(Severity severity) {
		List<ApplicationMessage> msgs = new LinkedList<ApplicationMessage>();
		for (ApplicationMessage msg : messages) {
			if (severity.equals(msg.getSeverity()))
				msgs.add(msg);
		}
		return msgs;
	}

}