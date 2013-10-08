package br.com.cd.mvo.web.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import br.com.cd.mvo.util.ParserUtils;

public class WebUtil {

	public static boolean getRequestBooleanParameter(ServletRequest request,
			String key) {
		return WebUtil.getRequestBooleanParameter(request, key, false);
	}

	public static boolean getRequestBooleanParameter(ServletRequest request,
			String key, boolean defaultValue) {
		return WebUtil.getRequestParameter(request, key, defaultValue);
	}

	public static int getRequestIntParameter(ServletRequest request, String key) {
		return WebUtil.getRequestIntParameter(request, key, 0);
	}

	public static int getRequestIntParameter(ServletRequest request,
			String key, int defaultValue) {
		return WebUtil.getRequestParameter(request, key, defaultValue);
	}

	public static <T> T getRequestParameter(ServletRequest request, String key,
			T defaultValue) {
		return ParserUtils.parseObject(
				WebUtil.getRequestParameter(request, key), defaultValue);
	}

	public static <T> T getRequestParameter(ServletRequest request, String key,
			Class<T> resultType) {
		return ParserUtils.parseObject(resultType,
				WebUtil.getRequestParameter(request, key));
	}

	public static <T> T getRequestParameter(ServletRequest request, String key,
			Class<T> resultType, T defaultValue) {
		return ParserUtils.parseObject(resultType,
				WebUtil.getRequestParameter(request, key), defaultValue);
	}

	public static String getRequestParameter(ServletRequest request, String key) {
		try {
			if (request != null) {
				return ParserUtils.parseString(request.getParameter(key));
			}
		} catch (Exception e) {
			System.out.println("\n\ngetRequestParameter Exception, \nkey: "
					+ key + "\nmessage:" + e.getMessage());
		}
		return "";
	}

	public static boolean getInitBooleanParameter(ServletConfig context,
			String key, boolean defaultValue) {
		return WebUtil.getInitParameter(context, key, defaultValue);
	}

	public static boolean getInitBooleanParameter(ServletConfig context,
			String key) {
		return WebUtil.getInitBooleanParameter(context, key, false);
	}

	public static int getInitIntParameter(ServletConfig context, String key,
			int defaultValue) {
		return WebUtil.getInitParameter(context, key, defaultValue);
	}

	public static int getInitIntParameter(ServletConfig context, String key) {
		return WebUtil.getInitIntParameter(context, key, 0);
	}

	public static <T> T getInitParameter(ServletConfig context, String key,
			T defaultValue) {
		return ParserUtils.parseObject(WebUtil.getInitParameter(context, key),
				defaultValue);
	}

	public static <T> T getInitParameter(ServletConfig context, String key,
			Class<T> resultType) {
		return ParserUtils.parseObject(resultType,
				WebUtil.getInitParameter(context, key));
	}

	public static <T> T getInitParameter(ServletConfig context, String key,
			Class<T> resultType, T defaultValue) {
		return ParserUtils.parseObject(resultType,
				WebUtil.getInitParameter(context, key), defaultValue);
	}

	public static String getInitParameter(ServletConfig context, String key) {
		System.out.println("\n\ngetInitParameter, context: " + context
				+ ", key: " + key);
		try {
			if (context != null) {
				return ParserUtils.parseString(context.getInitParameter(key));
			}
		} catch (Exception e) {
			System.out.println("\n\ngetInitParameter Exception, \nname: " + key
					+ "\nmessage: " + e.getMessage());
		}
		return "";
	}

	public static boolean getInitBooleanParameter(ServletContext context,
			String key, boolean defaultValue) {
		return WebUtil.getInitParameter(context, key, defaultValue);
	}

	public static boolean getInitBooleanParameter(ServletContext context,
			String key) {
		return WebUtil.getInitBooleanParameter(context, key, false);
	}

	public static int getInitIntParameter(ServletContext context, String key,
			int defaultValue) {
		return WebUtil.getInitParameter(context, key, defaultValue);
	}

	public static int getInitIntParameter(ServletContext context, String key) {
		return WebUtil.getInitIntParameter(context, key, 0);
	}

	public static <T> T getInitParameter(ServletContext context, String key,
			T defaultValue) {
		return ParserUtils.parseObject(WebUtil.getInitParameter(context, key),
				defaultValue);
	}

	public static <T> T getInitParameter(ServletContext context, String key,
			Class<T> resultType) {
		return ParserUtils.parseObject(resultType,
				WebUtil.getInitParameter(context, key));
	}

	public static <T> T getInitParameter(ServletContext context, String key,
			Class<T> resultType, T defaultValue) {
		return ParserUtils.parseObject(resultType,
				WebUtil.getInitParameter(context, key), defaultValue);
	}

	public static String getInitParameter(ServletContext context, String key) {
		System.out.println("\n\ngetInitParameter, context: " + context
				+ ", key: " + key);
		try {
			if (context != null) {
				return ParserUtils.parseString(context.getInitParameter(key));
			}
		} catch (Exception e) {
			System.out.println("\n\ngetInitParameter Exception, \nname: " + key
					+ "\nmessage: " + e.getMessage());
		}
		return "";
	}

}
