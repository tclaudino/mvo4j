package br.com.cd.mvo.util;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

	public static String cammelCase(String txt) {
		return isNullOrEmpty(txt) ? "" : txt.substring(0, 1).toLowerCase()
				+ txt.substring(1);
	}

	public static boolean containsAll(String value, String[] sequence) {
		if (value == null || sequence == null) {
			return false;
		}

		for (String seq : sequence) {
			if (!value.contains(seq)) {
				return false;
			}
		}
		return true;
	}

	public static boolean contain(Object value, Object[] sequence) {
		if (value == null || sequence == null) {
			return false;
		}

		for (Object seq : sequence) {
			if (value.equals(seq)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsNo(String value, String[] sequence) {
		if (value == null || sequence == null) {
			return false;
		}

		for (String seq : sequence) {
			if (value.contains(seq)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.equals("");
	}

	public static boolean isNullOrZero(Integer value) {
		return value == null || value == 0;
	}

	public static String addBeginSlash(String value) {
		return "/" + removeBeginSlash(value);
	}

	public static String addEndSlash(String value) {
		return removeEndSlash(value) + "/";
	}

	public static String removeBeginSlash(String value) {
		if (value == null || "".equals(value)) {
			return "";
		}
		value = value.replaceAll("//", "/");
		value = "/".equals(value) ? "" : value;

		return value.startsWith("/") ? (value.length() > 1 ? value.substring(1)
				: "") : value;
	}

	public static String removeEndSlash(String value) {
		if (value == null || "".equals(value)) {
			return "";
		}
		value = value.replaceAll("//", "/");
		value = "/".equals(value) ? "" : value;

		return value.endsWith("/") ? ((value.length() - 1 > -1) ? value
				.substring(0, value.length() - 1) : "") : value;
	}

	public static String addBeginInvSlash(String value) {
		return "\\" + removeBeginInvSlash(value);
	}

	public static String addEndInvSlash(String value) {
		return removeEndInvSlash(value) + "\\";
	}

	public static String removeBeginInvSlash(String value) {
		if (value == null || "".equals(value)) {
			return "";
		}
		value = value.replaceAll("\\\\", "\\");
		value = "\\".equals(value) ? "" : value;

		return value.startsWith("\\") ? (value.length() > 1 ? value
				.substring(1) : "") : value;
	}

	public static String removeEndInvSlash(String value) {
		if (value == null || "".equals(value)) {
			return "";
		}
		value = value.replaceAll("\\\\", "\\");
		value = "\\".equals(value) ? "" : value;

		return value.endsWith("\\") ? ((value.length() - 1 > -1) ? value
				.substring(0, value.length() - 1) : "") : value;
	}

	public static String getBeforeSlash(String value) {
		value = StringUtils.removeEndSlash(StringUtils
				.removeBeginSlash((value != null ? value : "")));

		if (value.contains("/")) {
			value = ((value.indexOf("/") > 0) ? value.substring(0,
					value.indexOf("/")) : value).replaceAll("/", "");
		}
		return value;
	}

	public static String getBeforeInvSlash(String value) {
		value = StringUtils.removeEndSlash(StringUtils
				.removeBeginSlash((value != null ? value : "")));

		if (value.contains("\\")) {
			value = ((value.indexOf("\\") > 0) ? value.substring(0,
					value.indexOf("\\")) : value).replaceAll("\\", "");
		}
		return value;
	}

	public static boolean arrayContains(int val, int[] array) {
		for (int arrayValue : array) {
			if (val == arrayValue) {
				return (true);
			}
		}
		return (false);
	}

	public static boolean arrayContains(String val, String[] array) {
		return arrayContains(val, array, true);
	}

	public static boolean arrayContains(String val, String[] array,
			boolean caseSensitive) {
		for (String arrayValue : array) {
			if (caseSensitive) {
				if (arrayValue.equals(val)) {
					return (true);
				}
			} else {
				if (arrayValue.toLowerCase().equals(val.toLowerCase())) {
					return (true);
				}
			}
		}
		return (false);
	}

	public static boolean arrayContains(char val, char[] array) {
		for (char arrayValue : array) {
			if (val == arrayValue) {
				return (true);
			}
		}
		return (false);
	}

	public static String removeHtmlTags(String text) {
		text = java.util.regex.Pattern
				.compile("<i>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("</i>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("<b>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("</b>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("<u>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("</u>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("<br>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("<br/>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");
		text = java.util.regex.Pattern
				.compile("<[^>]+>", java.util.regex.Pattern.CASE_INSENSITIVE)
				.matcher(text).replaceAll("");

		return (text);
	}

	public static String clearString(String body) {

		body = body.trim();

		String httpUrl = "";
		if (body.toLowerCase().indexOf("http://") >= 0) {
			body = body.substring(body.toLowerCase().indexOf("http://") + 7);
			httpUrl = "http://";
		}

		body = body.replaceAll("[áäàâã]", "a");
		body = body.replaceAll("[éëèê]", "e");
		body = body.replaceAll("[íïìî]", "i");
		body = body.replaceAll("[óöòôõ]", "o");
		body = body.replaceAll("[úüùû]", "u");
		body = body.replaceAll("[ç]", "c");
		body = body.replaceAll("[ñ]", "n");

		body = body.replaceAll("[ÁÄÀÂÃ]", "a");
		body = body.replaceAll("[ÉËÈÊẼ]", "e");
		body = body.replaceAll("[Í?ÌÎĨ]", "i");
		body = body.replaceAll("[ÓÖÒÔÕ]", "o");
		body = body.replaceAll("[ÚÜÙÛŨ]", "u");
		body = body.replaceAll("[Ç]", "c");
		body = body.replaceAll("[Ñ]", "n");

		body = body.replaceAll("[']", "");
		body = body.replaceAll("[;]", "");
		body = body.replaceAll("[,]", "");
		body = body.replaceAll(":", "");
		body = body.replaceAll("^", "");
		body = body.replaceAll("º", "");
		body = body.replaceAll("[,]", "");
		body = body.replaceAll("ª", "-");
		body = body.replaceAll("`", "");
		body = body.replaceAll("´", "");
		body = body.replaceAll("[{]", "");
		body = body.replaceAll("[}]", "");
		body = body.replaceAll("¨", "");
		body = body.replaceAll("%", "");
		body = body.replaceAll("$", "");
		body = body.replaceAll("#", "");
		body = body.replaceAll("@", "");
		body = body.replaceAll("\\s+", " ");
		body = body.replaceAll("\"", "");

		body = body.replace("\\", "");
		body = body.replace("+", "");
		body = body.replace("=", "");
		body = body.replace(".", "");
		body = body.replace("(", "");
		body = body.replace(")", "");
		body = body.replace("[", "");
		body = body.replace("]", "");
		body = body.replace("!", "");

		body = body.replace(" X ", "-x-");
		body = body.replace("  x  ", "-x-");
		body = body.replace(" - ", "-");
		body = body.replace("- ", "-");
		body = body.replace(" -", "-");
		body = body.replace(" ", "-");

		body = httpUrl + body;

		return (body);
	}

	public static String escapeUrl(String text) {
		text = removeHtmlTags(text).replace("http://", "http")
				.replace("https://", "https").replace("ftp://", "ftp")
				.replace("#", "").replace("@", "").replace("?", "")
				.replace("¿", "").replace("!", "").replace("¡", "")
				.replace("/", "_").replace("\n", "\\n");
		return text;
	}

	public static String toUrlFormat(String url) {
		url = removeHtmlTags(url).replace("\\", "/").replace("\\", "/")
				.replace("//", "/").replace("//", "/").replace("&", "-e-")
				.replace("'", "").replace("`", "").replace("´", "")
				.replace("'", "").replace("´", "");
		return escapeJavaScript(clearString(url));
	}

	public static String escapeJavaScript(String text) {
		String newText = text.replaceAll("`", "\\`");
		newText = newText.replaceAll("´", "\\´");
		newText = newText.replaceAll("'", "\\'");
		newText = newText.replaceAll("\"", "\\\"");
		return newText.replace("\n", "\\n");
	}

	public static String format(String s, Object... arguments) {
		int i = 0;
		while (i < arguments.length) {
			String delimiter = "{" + i + "}";
			while (s.contains(delimiter)) {
				s = s.replace(delimiter, ParserUtils.parseString(arguments[i]));
			}
			i++;
		}
		return s;
	}

	private static Map<String, Double> uniqueStrings = new HashMap<>();

	public static String getUniqueString(String className) {

		String _className = org.apache.commons.lang3.StringUtils
				.capitalize(className);

		String classNameKey = _className.substring(_className.lastIndexOf("."))
				.replaceAll("[$&#@.]", "_").replaceAll("__", "_");

		synchronized (uniqueStrings) {
			if (uniqueStrings.containsKey(classNameKey)) {
				double size = uniqueStrings.get(_className);
				uniqueStrings.put(classNameKey, ++size);
				_className += size;
			}
		}
		return _className;
	}
}
