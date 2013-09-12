package br.com.cd.scaleframework.context;

public class InitParamKeys {

	public static final String CACHETIME_MAXSIZE = "br.com.cd.scale.CACHETIME_MAXSIZE";
	public static final String IMAGES_FOLDER = "br.com.cd.scale.IMAGES_FOLDER";
	public static final String IMAGES_CACHETIME = "br.com.cd.scale.IMAGES_CACHETIME";
	public static final String TRANSLATES_CACHETIME = "br.com.cd.scale.TRANSLATES_CACHETIME";
	public static final String IMAGES_FILTER_URL_PATTERN = "br.com.cd.scale.IMAGES_FILTER_URL_PATTERN";

	public static class DefaultValues {

		public static final int CACHETIME_MAXSIZE = 600;
		public static final int IMAGES_CACHETIME = 800;
		public static final int TRANSLATES_CACHETIME = 3600;
		public static final String IMAGES_FILTER_URL_PATTERN = "/getImage";
	}
}