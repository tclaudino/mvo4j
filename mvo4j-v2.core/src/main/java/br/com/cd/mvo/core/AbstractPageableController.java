package br.com.cd.mvo.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.cd.mvo.ApplicationKeys;
import br.com.cd.mvo.Translator;
import br.com.cd.mvo.util.ParserUtils;

public abstract class AbstractPageableController implements PageableController {

	private Integer pageSize = -1;
	private Integer pageNumber = 1;
	private Long recordsCount = -1L;

	public interface PagerCallback {

		void onRefresh();

		Integer getPageSize();

		Long getRecordsCount();

		Translator getTranslator();
	}

	protected abstract PagerCallback getPagerCallback();

	@Override
	public final Integer getOffset() {
		return (getPageNumber() * getPageSize()) - getPageSize();
	}

	@Override
	public final Integer getLimit() {
		Integer i = getOffset() + getPageSize() - 1;
		long count = recordsCount - 1;
		if (i > count) {
			i = ParserUtils.parseInt(count);
		}
		if (i < 0) {
			i = 0;
		}
		return i;
	}

	@Override
	public final Integer getPageSize() {
		if (this.pageSize == -1) {
			this.pageSize = getPagerCallback().getPageSize();
		}

		return pageSize;
	}

	@Override
	public final void setPageSize(Integer pageSize) {

		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"pageSize: {0}", new Object[] { pageSize });

		this.pageSize = pageSize;
	}

	@Override
	public final void setPageNumber(Integer pageNumber) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"pageNumber: {0}", new Object[] { pageNumber });

		this.pageNumber = pageNumber;
	}

	@Override
	public final Integer getPageNumber() {
		return pageNumber;
	}

	@Override
	public Integer getPagesCount() {
		return recordsCount > 1 && getPageSize() > 0
				&& recordsCount > getPageSize() ? (ParserUtils.parseInt(Math
				.ceil(recordsCount / ParserUtils.parseDouble(getPageSize()))))
				: 1;
	}

	@Override
	public final Long getRecordsCount() {
		if (this.recordsCount == -1) {
			this.recordsCount = getPagerCallback().getRecordsCount();
		}
		return recordsCount;
	}

	@Override
	public void setRecordsCount(Long recordsCount) {
		this.recordsCount = recordsCount;
	}

	@Override
	public String getPageStatus() {
		return getPagerCallback().getTranslator().getMessage(
				ApplicationKeys.Pagination.STATUS,
				new Object[] { getPageNumber(), getPagesCount() });
	}

	@Override
	public final void toFirstPage() {
		if (isHasPreviousPage()) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"firstPage");

			pageNumber = 1;

			refreshPage();
		}
	}

	@Override
	public final void toPreviousPage() {
		if (isHasPreviousPage()) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"previousPage");

			pageNumber--;

			refreshPage();
		}
	}

	@Override
	public final boolean isHasPreviousPage() {
		return getPageNumber() > 1;
	}

	@Override
	public final void toNextPage() {
		if (isHasNextPage()) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"nextPage");

			pageNumber++;

			refreshPage();
		}
	}

	@Override
	public final void toLastPage() {
		if (isHasNextPage()) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"lastPage");

			pageNumber = getPagesCount();
			refreshPage();
		}
	}

	@Override
	public final boolean isHasNextPage() {
		return getPageNumber() < getPagesCount();
	}

	@Override
	public final void refreshPage() {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,
				"refreshPage");

		getPagerCallback().onRefresh();
	}
}