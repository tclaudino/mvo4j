package br.com.cd.mvo.orm.multitenancy;

import br.com.cd.mvo.util.ThreadLocalMapUtil;

public class DataSourceContextHolder {

	private static final String THREAD_VARIABLE_NAME = DataSourceContextHolder.class.getName();

	public static void setTargetDataSource(String targetDataSource) {
		ThreadLocalMapUtil.setThreadVariable(THREAD_VARIABLE_NAME, targetDataSource);
	}

	public static String getTargetDataSource() {
		return (String) ThreadLocalMapUtil.getThreadVariable(THREAD_VARIABLE_NAME);
	}

	public static void clearTargetDataSource() {
		ThreadLocalMapUtil.removeThreadVariable(THREAD_VARIABLE_NAME);
	}
}
