package br.com.cd.mvo;

public class ApplicationMessage {

	public enum Severity {
		SEVERITY_SUCCESS("SUCCESS"), SEVERITY_INFO("INFO"), SEVERITY_WARN(
				"WARN"), SEVERITY_ERROR("ERROR");

		private String severity;

		Severity(String severity) {
			this.severity = severity;
		}

		public String toString() {
			return severity;
		}

		public String getSeverity() {
			return severity;
		}

	}

	public static class ThrowableMessage extends ApplicationMessage {

		public ThrowableMessage(String summary) {
			super(summary);
		}

		private Throwable throwable;

		public ThrowableMessage(String summary, String detail, String clientId,
				Throwable throwable) {
			super(summary, detail, clientId);
			this.throwable = throwable;
		}

		public ThrowableMessage(String summary, String detail, String id,
				Severity severity, Throwable throwable) {
			super(summary, detail, severity);
			this.throwable = throwable;
		}

		public ThrowableMessage(String summary, String clientId,
				Throwable throwable) {
			super(summary, clientId);
			this.throwable = throwable;
		}

		public ThrowableMessage(String summary, String clientId,
				Throwable throwable, Severity severity) {
			super(summary, clientId, severity);
			this.throwable = throwable;
		}

		public ThrowableMessage(String summary, Throwable throwable) {
			super(summary);
			this.throwable = throwable;
		}

		public ThrowableMessage(String summary, Exception ex, Severity severity) {
			super(summary, severity);
		}

		public Throwable getThrowable() {
			return throwable;
		}

		public boolean isThrowable() {
			return true;
		}

	}

	private String summary;
	private String detail;
	private String clientId;
	private Severity severity;

	public ApplicationMessage(String summary) {
		this.summary = summary;
	}

	public ApplicationMessage(String summary, String detail) {
		this(summary);
		this.detail = detail;
	}

	public ApplicationMessage(String summary, String detail, String clientId) {
		this(summary, detail);
		this.clientId = clientId;
	}

	public ApplicationMessage(String summary, String detail, String clientId,
			Severity severity) {
		this(summary, detail, clientId);
		this.severity = severity;
	}

	public ApplicationMessage(String summary, String clientId, Severity severity) {
		this(summary, clientId);
		this.severity = severity;
	}

	public ApplicationMessage(String summary, Severity severity) {
		this(summary);
		this.severity = severity;
	}

	public static ThrowableMessage createErrorMessage(Exception ex,
			String summary) {
		return new ThrowableMessage(summary, ex, Severity.SEVERITY_ERROR);
	}

	public static ThrowableMessage createErrorMessage(Exception ex,
			String summary, String clientId) {
		return new ThrowableMessage(summary, clientId, ex,
				Severity.SEVERITY_ERROR);
	}

	public static ApplicationMessage createErrorMessage(String summary,
			String detail) {
		return new ApplicationMessage(summary, detail, Severity.SEVERITY_ERROR);
	}

	public static ApplicationMessage createErrorMessage(String summary,
			String detail, String clientId) {
		return new ApplicationMessage(summary, detail, clientId,
				Severity.SEVERITY_ERROR);
	}

	public static ApplicationMessage createSuccessMessage(String msgSumary,
			String msgDetail) {
		return new ApplicationMessage(msgSumary, msgDetail,
				Severity.SEVERITY_SUCCESS);
	}

	public static ApplicationMessage createSuccessMessage(String msgSumary,
			String msgDetail, String clientId) {
		return new ApplicationMessage(msgSumary, msgDetail, clientId,
				Severity.SEVERITY_SUCCESS);
	}

	public static ApplicationMessage createWarningMessage(String msgSumary,
			String msgDetail) {
		return new ApplicationMessage(msgSumary, msgDetail,
				Severity.SEVERITY_WARN);
	}

	public static ApplicationMessage createWarningMessage(String msgSumary,
			String msgDetail, String clientId) {
		return new ApplicationMessage(msgSumary, msgDetail, clientId,
				Severity.SEVERITY_WARN);
	}

	public static ApplicationMessage createInfoMessage(String msgSumary,
			String msgDetail) {
		return new ApplicationMessage(msgSumary, msgDetail,
				Severity.SEVERITY_INFO);
	}

	public static ApplicationMessage createInfoMessage(String msgSumary,
			String msgDetail, String clientId) {
		return new ApplicationMessage(msgSumary, msgDetail, clientId,
				Severity.SEVERITY_INFO);
	}

	public String getSummary() {
		return summary;
	}

	public String getDetail() {
		return detail;
	}

	public String getClientId() {
		return clientId;
	}

	public Severity getSeverity() {
		return severity;
	}

	public boolean isThrowable() {
		return false;
	}

}