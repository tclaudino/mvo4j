package br.com.cd.scaleframework.core.support;

public class WebCrudControllerComponentConfig extends
		WebControllerComponentConfig {

	private String listViewName = "list";

	private String editViewName = "edit";

	private String createViewName = "new";

	public WebCrudControllerComponentConfig(String name, String path,
			String targetEntityClassName) {
		super(name, targetEntityClassName, path);
	}

	public WebCrudControllerComponentConfig(String name, String path,
			String targetEntityClassName, int initialPageSize) {
		super(name, path, targetEntityClassName, initialPageSize);
	}

	public WebCrudControllerComponentConfig(String name, String path,
			Class<?> targetEntity, int initialPageSize) {
		super(name, path, targetEntity, initialPageSize);
	}

	public WebCrudControllerComponentConfig(String name, String path,
			String targetEntityClassName, int initialPageSize,
			String listViewName, String editViewName, String createViewName) {
		this(name, path, targetEntityClassName, initialPageSize);
		this.listViewName = listViewName;
		this.editViewName = editViewName;
		this.createViewName = createViewName;
	}

	public WebCrudControllerComponentConfig(String name, String path,
			Class<?> targetEntity, int initialPageSize, String listViewName,
			String editViewName, String createViewName) {
		this(name, path, targetEntity, initialPageSize);
		this.listViewName = listViewName;
		this.editViewName = editViewName;
		this.createViewName = createViewName;
	}

	public String getListViewName() {
		return listViewName;
	}

	public void setListViewName(String listViewName) {
		this.listViewName = listViewName;
	}

	public String getEditViewName() {
		return editViewName;
	}

	public void setEditViewName(String editViewName) {
		this.editViewName = editViewName;
	}

	public String getCreateViewName() {
		return createViewName;
	}

	public void setCreateViewName(String createViewName) {
		this.createViewName = createViewName;
	}
}