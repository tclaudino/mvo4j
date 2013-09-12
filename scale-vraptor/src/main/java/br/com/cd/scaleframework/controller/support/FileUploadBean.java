package br.com.cd.scaleframework.controller.support;

import java.util.EventObject;

public interface FileUploadBean<T extends EventObject> {

	void handleFileUpload(T event);

	void clearUploadFiles();

	void persistFiles();
}
