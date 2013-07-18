package br.com.cd.scaleframework.context;

public final class TranslatorKeys {

	public static final String OK = "ok";
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String WAIT = "wait";

	public String getOK() {
		return OK;
	}

	public String getNO() {
		return NO;
	}

	public String getYES() {
		return YES;
	}

	public String getWAIT() {
		return WAIT;
	}

	public Pagination getPagination() {
		return new Pagination();
	}

	public PersistAction getPersistActions() {
		return new PersistAction();
	}

	public Publisher getPublisher() {
		return new Publisher();
	}

	public Entity getEntity() {
		return new Entity();
	}

	public static class Entity {

		public static final String NOT_FOUND_SUMARY = "model.notfound";
		public static final String NOT_FOUND_MSG = "model.{0}.notfound";

		public String getNOT_FOUND_MSG() {
			return NOT_FOUND_MSG;
		}

		public String getNOT_FOUND_SUMARY() {
			return NOT_FOUND_SUMARY;
		}
	}

	public static class Publisher {

		public static final String NOTHING_SELECTED = "publisher.nothingSelected";
		public static final String CHECK_ALL = "publisher.checkAll";
		public static final String UNCHECK_ALL = "publisher.uncheckAll";
		public static final String SEARCH = "publisher.search";
		public static final String ENTER_SEARCH_TEXT = "publisher.enterSearchText";

		public String getNOTHING_SELECTED() {
			return NOTHING_SELECTED;
		}

		public String getCHECK_ALL() {
			return CHECK_ALL;
		}

		public String getUNCHECK_ALL() {
			return UNCHECK_ALL;
		}

		public String getENTER_SEARCH_TEXT() {
			return ENTER_SEARCH_TEXT;
		}

		public String getSEARCH() {
			return SEARCH;
		}

		public AcccessDenied getAcccessDenied() {
			return new AcccessDenied();
		}

		public Menu getMenu() {
			return new Menu();
		}

		public Action getActions() {
			return new Action();
		}

		public static class AcccessDenied {

			public static final String SUMARY = "publisher.accessdenied.sumary";
			public static final String VIEW = "publisher.accessdenied.view";
			public static final String INSERT = "publisher.accessdenied.insert";
			public static final String DELETE = "publisher.accessdenied.delete";
			public static final String UPDATE = "publisher.accessdenied.update";

			public String getDELETE() {
				return DELETE;
			}

			public String getINSERT() {
				return INSERT;
			}

			public String getUPDATE() {
				return UPDATE;
			}

			public String getVIEW() {
				return VIEW;
			}
		}

		public static class Action {

			public Delete getDelete() {
				return new Delete();
			}

			public static class Delete {

				public static final String CONFIRM = "publisher.delete.confirm";
				public static final String WAIT = "publisher.delete.wait";

				public String getCONFIRM() {
					return CONFIRM;
				}

				public String getWAIT() {
					return WAIT;
				}
			}
		}

		public static class Menu {

			public static final String NEW = "publisher.menu.new";
			public static final String EDIT = "publisher.menu.edit";
			public static final String SAVE = "publisher.menu.save";
			public static final String CANCEL = "publisher.menu.cancel";
			public static final String DELETE = "publisher.menu.delete";
			public static final String TO_LIST = "publisher.menu.toList";

			public String getCANCEL() {
				return CANCEL;
			}

			public String getDELETE() {
				return DELETE;
			}

			public String getEDIT() {
				return EDIT;
			}

			public String getNEW() {
				return NEW;
			}

			public String getSAVE() {
				return SAVE;
			}

			public String getTO_LIST() {
				return TO_LIST;
			}
		}
	}

	public static class Pagination {

		public static final String STATUS = "pagination.status";
		public static final String PAGE = "pagination.page";
		public static final String TO = "pagination.to";
		public static final String FIRST = "pagination.first";
		public static final String PREVIOUS = "pagination.previous";
		public static final String NEXT = "pagination.next";
		public static final String LAST = "pagination.last";
		public static final String SELECT_PAGE_SIZE = "pagination.select.pageSize";
		public static final String SELECT_PAGE_GO = "pagination.select.go";
		public static final String QUERYSTRING_PAGE_NUMBER = "pagination.querystring.pageNumber";
		public static final String QUERYSTRING_PAGE_SIZE = "pagination.querystring.pageSize";

		public String getSTATUS() {
			return STATUS;
		}

		public String getPAGE() {
			return PAGE;
		}

		public String getTO() {
			return TO;
		}

		public String getFIRST() {
			return FIRST;
		}

		public String getLAST() {
			return LAST;
		}

		public String getNEXT() {
			return NEXT;
		}

		public String getPREVIOUS() {
			return PREVIOUS;
		}

		public String getSELECT_PAGE_SIZE() {
			return SELECT_PAGE_SIZE;
		}

		public String getSELECT_PAGE_GO() {
			return SELECT_PAGE_GO;
		}

		public String getQUERYSTRING_PAGE_NUMBER() {
			return QUERYSTRING_PAGE_NUMBER;
		}

		public String getQUERYSTRING_PAGE_SIZE() {
			return QUERYSTRING_PAGE_SIZE;
		}
	}

	public static class PersistAction {

		public static final String EXECUTE_ERROR_SUMARY = "database.error.sumary";
		public static final String EXECUTE_ERROR_MSG = "database.error.message";
		public static final String SAVE_SUCCESS_SUMARY = "save.success.sumary";
		public static final String SAVE_SUCCESS_MESSAGE = "save.success.message";
		public static final String UPDATE_SUCCESS_SUMARY = "update.success.sumary";
		public static final String UPDATE_SUCCESS_MESSAGE = "update.success.message";
		public static final String DELETE_SUCCESS_SUMARY = "delete.success.sumary";
		public static final String DELETE_SUCCESS_MESSAGE = "delete.success.message";
		public static final String SAVE_ERROR_SUMARY = "save.error.sumary";
		public static final String SAVE_ERROR_MESSAGE = "save.error.message";
		public static final String UPDATE_ERROR_SUMARY = "update.error.sumary";
		public static final String UPDATE_ERROR_MESSAGE = "update.error.message";
		public static final String DELETE_ERROR_SUMARY = "delete.error.sumary";
		public static final String DELETE_ERROR_MESSAGE = "delete.error.message";

		public String getDELETE_ERROR_MESSAGE() {
			return DELETE_ERROR_MESSAGE;
		}

		public String getDELETE_ERROR_SUMARY() {
			return DELETE_ERROR_SUMARY;
		}

		public String getDELETE_SUCCESS_MESSAGE() {
			return DELETE_SUCCESS_MESSAGE;
		}

		public String getDELETE_SUCCESS_SUMARY() {
			return DELETE_SUCCESS_SUMARY;
		}

		public String getSAVE_ERROR_MESSAGE() {
			return SAVE_ERROR_MESSAGE;
		}

		public String getSAVE_ERROR_SUMARY() {
			return SAVE_ERROR_SUMARY;
		}

		public String getSAVE_SUCCESS_MESSAGE() {
			return SAVE_SUCCESS_MESSAGE;
		}

		public String getSAVE_SUCCESS_SUMARY() {
			return SAVE_SUCCESS_SUMARY;
		}
	}
}
