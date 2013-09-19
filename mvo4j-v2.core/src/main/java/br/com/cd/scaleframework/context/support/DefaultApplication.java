package br.com.cd.scaleframework.context.support;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cd.scaleframework.context.Application;
import br.com.cd.scaleframework.context.ApplicationMessage;
import br.com.cd.scaleframework.context.ApplicationMessage.Severity;

public class DefaultApplication implements Application {

	private List<ApplicationMessage> messages = new LinkedList<ApplicationMessage>();

	@Override
	public ApplicationMessage.ThrowableMessage addErrorMessage(Exception ex,
			String summary) {
		ApplicationMessage.ThrowableMessage message = ApplicationMessage
				.createErrorMessage(ex, summary);
		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage.ThrowableMessage addErrorMessage(Exception ex,
			String summary, String clientId) {
		ApplicationMessage.ThrowableMessage message = ApplicationMessage
				.createErrorMessage(ex, summary, clientId);
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

			messageList.add(message);
			this.messages.add(message);
		}

		return messageList;
	}

	@Override
	public ApplicationMessage addErrorMessage(String summary, String detail) {
		ApplicationMessage message = ApplicationMessage.createErrorMessage(
				summary, detail);

		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addErrorMessage(String summary, String detail,
			String clientId) {
		ApplicationMessage message = ApplicationMessage.createErrorMessage(
				summary, detail, clientId);

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

		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addSuccessMessage(String msgSumary,
			String msgDetail, String clientId) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail, clientId);

		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addWarningMessage(String msgSumary,
			String msgDetail) {
		ApplicationMessage message = ApplicationMessage.createWarningMessage(
				msgSumary, msgDetail);

		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addWarningMessage(String msgSumary,
			String msgDetail, String clientId) {
		ApplicationMessage message = ApplicationMessage.createWarningMessage(
				msgSumary, msgDetail, clientId);

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

			messageList.add(message);
			this.messages.add(message);
		}
		return messageList;
	}

	@Override
	public ApplicationMessage addInfoMessage(String msgSumary, String msgDetail) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail);

		messages.add(message);
		return message;
	}

	@Override
	public ApplicationMessage addInfoMessage(String msgSumary,
			String msgDetail, String clientId) {
		ApplicationMessage message = ApplicationMessage.createInfoMessage(
				msgSumary, msgDetail, clientId);

		messages.add(message);
		return message;
	}

	@Override
	public List<ApplicationMessage> getAllMessages() {
		return messages;
	}

	@Override
	public List<ApplicationMessage> getAllErrorMessages() {
		return getMessages(Severity.SEVERITY_ERROR);
	}

	@Override
	public List<ApplicationMessage> getAllInfoMessages() {
		return getMessages(Severity.SEVERITY_INFO);
	}

	@Override
	public List<ApplicationMessage> getAllWarningMessages() {
		return getMessages(Severity.SEVERITY_WARN);
	}

	@Override
	public List<ApplicationMessage> getAllSuccessMessages() {
		return getMessages(Severity.SEVERITY_SUCCESS);
	}

	@Override
	public List<ApplicationMessage.ThrowableMessage> getAllThrowableMessages() {
		return (List<ApplicationMessage.ThrowableMessage>) this
				.getThrowableMessages(Severity.SEVERITY_ERROR);
	}

	protected List<ApplicationMessage> getMessages(Severity severity) {
		List<ApplicationMessage> msgs = new LinkedList<ApplicationMessage>();
		for (ApplicationMessage msg : this.messages) {
			if (severity.equals(msg.getSeverity()))
				msgs.add(msg);
		}
		return msgs;
	}

	protected List<ApplicationMessage.ThrowableMessage> getThrowableMessages(
			Severity severity) {
		List<ApplicationMessage.ThrowableMessage> msgs = new LinkedList<ApplicationMessage.ThrowableMessage>();
		for (ApplicationMessage msg : this.messages) {
			if (severity.equals(msg.getSeverity()) && msg.isThrowable())
				msgs.add((ApplicationMessage.ThrowableMessage) msg);
		}
		return msgs;
	}

}