package br.com.cd.scaleframework.web.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import br.com.cd.scaleframework.util.ParserUtils;

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

	public static boolean getContextBooleanParameter(ServletContext context,
			String key, boolean defaultValue) {
		return WebUtil.getContextParameter(context, key, defaultValue);
	}

	public static boolean getContextBooleanParameter(ServletContext context,
			String key) {
		return WebUtil.getContextBooleanParameter(context, key, false);
	}

	public static int getContextIntParameter(ServletContext context,
			String key, int defaultValue) {
		return WebUtil.getContextParameter(context, key, defaultValue);
	}

	public static int getContextIntParameter(ServletContext context, String key) {
		return WebUtil.getContextIntParameter(context, key, 0);
	}

	public static <T> T getContextParameter(ServletContext context, String key,
			T defaultValue) {
		return ParserUtils.parseObject(
				WebUtil.getContextParameter(context, key), defaultValue);
	}

	public static String getContextParameter(ServletContext context, String key) {
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
