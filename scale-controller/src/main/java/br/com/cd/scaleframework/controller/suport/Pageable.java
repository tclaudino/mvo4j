package br.com.cd.scaleframework.controller.suport;

public interface Pageable {

	Integer getOffset();

	Integer getLimit();

	void toFirstPage();

	void toPreviousPage();

	boolean isHasPreviousPage();

	void toNextPage();

	void toLastPage();

	void refreshPage();

	boolean isHasNextPage();

	Integer getPageSize();

	void setPageSize(Integer pageSize);

	void setPageNumber(Integer pageNumber);

	Integer getPageNumber();

	Long getRecordsCount();

	Integer getPagesCount();

	void setRecordsCount(Long recordsCount);

	String getPageStatus();

	Integer getInitialPageSize();

	void resetPager();

}
