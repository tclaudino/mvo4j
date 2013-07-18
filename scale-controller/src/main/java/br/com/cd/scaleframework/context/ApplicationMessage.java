package br.com.cd.scaleframework.context;

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

	private String summary;
	private String detail;
	private String clientId;
	private Throwable throwable;
	private Severity severity;

	public ApplicationMessage(String summary, String detail, String id,
			Severity severity, Throwable throwable) {
		this.summary = summary;
		this.detail = detail;
		this.clientId = id;
		this.severity = severity;
		this.throwable = throwable;
	}

	public ApplicationMessage(String summary) {
		super();
		this.summary = summary;
	}

	public ApplicationMessage(String summary, String detail) {
		super();
		this.summary = summary;
		this.detail = detail;
	}

	public ApplicationMessage(String summary, String detail, String clientId) {
		super();
		this.summary = summary;
		this.detail = detail;
		this.clientId = clientId;
	}

	public ApplicationMessage(String summary, String detail, String clientId,
			Severity severity) {
		super();
		this.summary = summary;
		this.detail = detail;
		this.clientId = clientId;
		this.severity = severity;
	}

	public ApplicationMessage(String summary, String detail, String clientId,
			Throwable throwable) {
		super();
		this.summary = summary;
		this.detail = detail;
		this.clientId = clientId;
		this.throwable = throwable;
	}

	public ApplicationMessage(String summary, String clientId, Severity severity) {
		super();
		this.summary = summary;
		this.clientId = clientId;
		this.severity = severity;
	}

	public ApplicationMessage(String summary, String clientId,
			Throwable throwable) {
		super();
		this.summary = summary;
		this.clientId = clientId;
		this.throwable = throwable;
	}

	public ApplicationMessage(String summary, Throwable throwable,
			Severity severity) {
		super();
		this.summary = summary;
		this.throwable = throwable;
		this.severity = severity;
	}

	public ApplicationMessage(String summary, Severity severity) {
		super();
		this.summary = summary;
		this.severity = severity;
	}

	public ApplicationMessage(String summary, Throwable throwable) {
		super();
		this.summary = summary;
		this.throwable = throwable;
	}

	public static ApplicationMessage createErrorMessage(Exception ex,
			String summary) {
		return new ApplicationMessage(summary, ex, Severity.SEVERITY_ERROR);
	}

	public static ApplicationMessage createErrorMessage(Exception ex,
			String summary, String clientId) {
		return new ApplicationMessage(summary, summary, clientId,
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

	public Throwable getThrowable() {
		return throwable;
	}

	public Severity getSeverity() {
		return severity;
	}
}