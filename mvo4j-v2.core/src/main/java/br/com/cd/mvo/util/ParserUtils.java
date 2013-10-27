package br.com.cd.mvo.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserUtils {

	private static final Logger LOG = Logger.getLogger(ParserUtils.class
			.getName());

	public static <T> T parseObject(Class<T> returnType, Object value) {
		return ParserUtils.parseObject(returnType, value, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T parseObject(Object value, T defaultValue) {
		return ParserUtils.parseObject((Class<T>) defaultValue.getClass(),
				value, defaultValue);
	}

	@SuppressWarnings("unchecked")
	public static <T> T parseObject(Class<T> returnType, Object value,
			T defaultValue) {
		if (value != null) {
			if (returnType.equals(String.class)) {
				return (T) ParserUtils
						.parseString(value, (String) defaultValue);
			} else if (returnType.equals(Date.class)) {
				return (T) ParserUtils.parseDate(value, (Date) defaultValue);
			} else if (returnType.equals(Integer.class)) {
				return (T) new Integer(ParserUtils.parseInt(value,
						(Integer) (defaultValue != null ? defaultValue : 0)));
			} else if (returnType.equals(Double.class)) {
				return (T) new Double(ParserUtils.parseDouble(value,
						(Double) (defaultValue != null ? defaultValue : 0D)));
			} else if (returnType.equals(Float.class)) {
				return (T) new Float(ParserUtils.parseFloat(value,
						(Float) (defaultValue != null ? defaultValue : 0F)));
			} else if (returnType.equals(Long.class)) {
				return (T) new Long(ParserUtils.parseLong(value,
						(Long) (defaultValue != null ? defaultValue : 0L)));
			} else if (returnType.equals(Boolean.class)) {
				return (T) new Boolean(
						ParserUtils.parseBoolean(value,
								(Boolean) (defaultValue != null ? defaultValue
										: false)));
			} else if (returnType.isAssignableFrom(value.getClass())) {
				return (T) value;
			}
		}
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] parserAll(Class<T> returnType, String... tokens) {
		List<T> list = new ArrayList<T>(tokens.length);
		for (int i = 0; i < tokens.length; i++) {
			list.add(ParserUtils.parseObject(returnType, tokens[i]));
		}
		return (T[]) list.toArray();
	}

	public static String parseString(Object value) {
		return ParserUtils.parseString(value, "");
	}

	public static String parseString(Object value, String defaultValue) {
		System.out.println(ParserUtils.class.getName()
				+ ".parseString, value: " + value + ", default: "
				+ defaultValue);

		if (value != null) {
			if (value instanceof Date) {
				return ParserUtils.parseString((Date) value);
			}
			if (!"".equals(value)) {
				return "" + value;
			}
		}
		return defaultValue != null ? defaultValue : "";
	}

	private static String parseString(Date value) {
		return ParserUtils.parseString(value, DateFormat.getInstance());
	}

	public static String parseString(Date value, DateFormat dateFormat) {
		if (value != null) {
			dateFormat = dateFormat != null ? dateFormat : DateFormat
					.getInstance();

			try {
				return dateFormat.format(value);
			} catch (Exception e) {
				LOG.log(Level.WARNING,
						"ParseUtils.parseDate() Exception, Value: {0}, Message: {1}",
						new Object[] { value, e.getMessage() });
			}
		}
		return "";
	}

	public static int parseInt(Object value) {
		return ParserUtils.parseInt(value, 0);
	}

	public static int parseInt(Object value, int defaultValue) {
		if (value != null) {
			String newValue = ParserUtils.parseString(value).trim();
			if (!"".equals(newValue)) {
				try {
					if (value instanceof Double) {
						return (int) Double.parseDouble(newValue);
					}
					if (value instanceof Float) {
						return (int) Float.parseFloat(newValue);
					}
					if (value instanceof Long) {
						return (int) Long.parseLong(newValue);
					}
					return Integer.parseInt(newValue);
				} catch (Exception e) {
					LOG.log(Level.WARNING,
							"ParseUtils.parseInt() Exception, Value: {0}, Message: {1}",
							new Object[] { value, e.getMessage() });
				}
			}
		}
		return defaultValue;
	}

	public static boolean parseBoolean(Object value) {
		return ParserUtils.parseBoolean(value, false);
	}

	public static boolean parseBoolean(Object value, boolean defaultValue) {
		if (value != null) {
			String newValue = ParserUtils.parseString(value).trim();
			newValue = "0".equals(newValue) ? "false"
					: "1".equals(newValue) ? "true" : newValue;

			if ("false".equals(newValue.toLowerCase())
					|| "true".equals(newValue.toLowerCase())) {
				try {
					return Boolean.parseBoolean(newValue);
				} catch (Exception e) {
					LOG.log(Level.WARNING,
							"ParseUtils.parseBoolean Exception, Message: {0}",
							e.getMessage());
				}
			}
		}
		return defaultValue;
	}

	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="parseDouble's">
	public static double parseDouble(Object value) {
		return ParserUtils.parseDouble(value, 0d);
	}

	public static double parseDouble(Object value, double defaultValue) {
		if (value != null) {
			String newValue = ParserUtils.parseString(value).trim();
			if (!"".equals(newValue)) {
				try {
					return Double.parseDouble(newValue);
				} catch (Exception e) {
					LOG.log(Level.WARNING,
							"ParseUtils.parseDouble() Exception, Value: {0}, Message: {1}",
							new Object[] { value, e.getMessage() });
				}
			}
		}
		return defaultValue;
	}

	public static float parseFloat(Object value) {
		return ParserUtils.parseFloat(value, 0f);
	}

	public static float parseFloat(Object value, float defaultValue) {
		if (value != null) {
			String newValue = ParserUtils.parseString(value).trim();
			if (!"".equals(newValue)) {
				try {
					return Float.parseFloat(newValue);
				} catch (Exception e) {
					LOG.log(Level.WARNING,
							"ParserUtils.parseFloat() Exception, Value: {0}, Message: {1}",
							new Object[] { value, e.getMessage() });
				}
			}
		}
		return defaultValue;
	}

	public static long parseLong(Object value) {
		return ParserUtils.parseLong(value, 0l);
	}

	public static long parseLong(Object value, long defaultValue) {
		if (value != null) {
			String newValue = ParserUtils.parseString(value).trim();
			if (!"".equals(newValue)) {
				try {
					return Long.parseLong(newValue);
				} catch (Exception e) {
					// LOG.log(Level.WARNING,
					// "IdemParse.parseLong() Exception, Value: {0}, Message: {1}",
					// new Object[] { value, e.getMessage() });
				}
			}
		}
		return defaultValue;
	}

	public static Date parseDate(Object value) {
		return ParserUtils.parseDate(value, new Date());
	}

	public static Date parseDate(Object value, DateFormat dateFormat) {
		return ParserUtils.parseDate(value, dateFormat, new Date());
	}

	public static Date parseDate(Object value, Date defaultValue) {
		return ParserUtils.parseDate(value, DateFormat.getInstance(),
				defaultValue);
	}

	public static Date parseDate(Object value, DateFormat dateFormat,
			Date defaultValue) {
		if (value != null) {
			if (value instanceof Date) {
				return (Date) value;
			}
			dateFormat = dateFormat != null ? dateFormat : DateFormat
					.getInstance();

			String newValue = ParserUtils.parseString(value);
			if (!"".equals(newValue)) {
				try {
					return dateFormat.parse(newValue);
				} catch (Exception e) {
					LOG.log(Level.WARNING,
							ParserUtils.class.getName()
									+ ".parseDate() Exception, Value: {0}, Message: {1}",
							new Object[] { value, e.getMessage() });
				}
			}
		}
		return defaultValue;
	}

	public static <T> T assertNotEquals(T o1, T o2, T def) {
		if (o1 != null) {
			return o2 == null || !o1.equals(o2) ? o1 : def;
		}
		return def;
	}

	public static <T> T assertEquals(T o1, T o2, T def) {
		if (o1 != null) {
			return o2 != null && o1.equals(o2) ? o1 : def;
		}
		return def;
	}

	public static String assertEmpty(String o1, String def) {
		return (o1 == null || o1.isEmpty()) ? "" : def;
	}

	public static String assertNotEmpty(String o1, String def) {
		return (o1 != null && !o1.isEmpty()) ? o1 : def;
	}

	public static Object assertNull(Object o1, Object def) {
		return (o1 == null) ? o1 : def;
	}

	public static Object assertNotNull(Object o1, Object def) {
		return (o1 != null) ? o1 : def;
	}

	public static <T> List<T> arrayToList(T[] arr) {
		if (arr == null) {
			return new ArrayList<T>();
		}
		return Arrays.asList(arr);
	}

	public static <T> Set<T> arrayToSet(T[] arr) {
		if (arr == null) {
			return new HashSet<T>();
		}
		return new HashSet<T>(Arrays.asList(arr));
	}

	public static <T> List<T> setToList(Set<T> set) {
		return new ArrayList<T>(set);
	}
}
