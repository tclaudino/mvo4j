package br.com.cd.mvo.orm;

public class OrderBy {

	public static enum OrderByDirection {

		ASC, DESC, NONE
	}

	private final String orderField;
	private final OrderByDirection orderDirection;

	public OrderBy(String field, OrderByDirection direction) {
		this.orderField = field;
		this.orderDirection = direction;
	}

	public static OrderBy orderBy(String orderField, OrderByDirection orderDirection) {
		return new OrderBy(orderField, orderDirection);
	}

	public static OrderBy orderBy(String orderField) {
		return orderBy(orderField, OrderByDirection.NONE);
	}

	public String getOrderField() {
		return orderField;
	}

	public OrderByDirection getOrderDirection() {
		return orderDirection;
	}

}