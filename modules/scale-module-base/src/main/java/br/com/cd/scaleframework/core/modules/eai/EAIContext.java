package br.com.cd.scaleframework.core.modules.eai;

public interface EAIContext {

	EAIDriver getEAIDriver(String driverName);

	boolean containsEAIDriver(String driverName);

	<T> GenericEAIDriver<T> getEAIDriver(String driverName, Class<T> modelType);

}